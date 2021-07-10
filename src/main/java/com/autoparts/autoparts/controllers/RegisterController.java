package com.autoparts.autoparts.controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.classes.Another;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.AnotherService;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.EmailSenderService;
import com.autoparts.autoparts.services.ReCaptchaValidationService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

@Controller
public class RegisterController {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private AccountService userService;
	private EmailSenderService emailService;

	@Autowired
	ReCaptchaValidationService validator;

	@Autowired
    AnotherService newsletterService;

	@Autowired
    BusinessDetailsService businessDetailsService;


	@Autowired
	public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, AccountService userService,
			EmailSenderService emailService) {

		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
		this.emailService = emailService;
	}

	// Return registration form template
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, Account user, Model model) {
		modelAndView.addObject("account", user);
		model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
		modelAndView.setViewName("signup");
		return modelAndView;
	}

	//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
	// Process form input data
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView processRegistrationForm(ModelAndView modelAndView,
			@ModelAttribute("account") @Valid Account user, BindingResult bindingResult, HttpServletRequest request,
			@RequestParam("username") String username, @RequestParam(name = "g-recaptcha-response") String resp,
			Model model, @RequestParam(name = "phoneNumber") String phn) {
		if (validator.validateCaptcha(resp)) {
			try {
				Account exists = userService.getOneAccount(username);
				model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
				modelAndView.addObject("exists", "Email already in use, try a different one");
				modelAndView.setViewName("signup");

			} catch (NoSuchElementException e) {
				Pattern pattern = Pattern.compile("^\\d{10}$");
    			Matcher matcher = pattern.matcher(phn);
				
				if (!matcher.matches()){
					modelAndView.addObject("phnerr", "Phone has to be atleast 10 digits");
					modelAndView.setViewName("signup");
					model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
					return modelAndView;
				}

				if (bindingResult.hasErrors()) {
					modelAndView.setViewName("signup");
				}
				else { // new user so we create user and send confirmation e-mail
					user.setUsername(username);
					// news
					Another another = new Another();
					another.setEmail(username);
					// check if customer had subscribed
					try {
						Another exists1 = newsletterService.getOneNewsletter(username);
					} catch (NoSuchElementException ee) {
						newsletterService.addNewsletter(another);
					}
					// news
					// Disable user until they click on confirmation link in email
					user.setEnabled(false);
					// Generate random 36-character string token for confirmation link
					user.setConfirmationToken(UUID.randomUUID().toString());

					userService.addAccount(user);

					String appUrl = request.getScheme() + "://" + request.getServerName();

					SimpleMailMessage registrationEmail = new SimpleMailMessage();
					registrationEmail.setTo(user.getUsername());
					registrationEmail.setSubject("Registration Confirmation");
					registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl
							+ "/confirm?token=" + user.getConfirmationToken());
					registrationEmail.setFrom("noreply@domain.com");

					emailService.sendEmail(registrationEmail);

					modelAndView.addObject("confirmationMessage",
							"A confirmation e-mail has been sent to " + user.getUsername());
					modelAndView.setViewName("signup");
				}
			}
		} else {
			modelAndView.addObject("capmessage", "ReCaptcha failed! Please try again");
			modelAndView.setViewName("signup");
		}
		model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
		return modelAndView;
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

		Account user = userService.findByConfirmationToken(token);

		if (user == null) { // No token found in DB
			modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
		} else { // Token found
			// create date
			LocalDateTime sentOn = user.getCreatedDate();
			LocalDateTime receivedOn = LocalDateTime.now();
			long diffInMillies = ChronoUnit.MINUTES.between(sentOn, receivedOn);
			if (diffInMillies > 5) {
				modelAndView.addObject("expiredToken", "Expired token!");
				modelAndView.setViewName("confirm");
				return modelAndView;
			}
			modelAndView.addObject("validtoken", true);
			modelAndView.addObject("confirmationToken", user.getConfirmationToken());
		}

		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map requestParams, RedirectAttributes redir, @RequestParam("password") String pass, @RequestParam("ConfirmPassword") String conpass) {
		modelAndView.setViewName("confirm");

		Zxcvbn passwordCheck = new Zxcvbn();

		Strength strength = passwordCheck.measure((String) requestParams.get("password"));

		if (strength.getScore() < 3) {
			bindingResult.reject("password");

			redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

			modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
			return modelAndView;
		}

		if (!pass.equals(conpass)) {
			redir.addFlashAttribute("noMatch", "Password not matching!");
			modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
			return modelAndView;
		}

		// Find the user associated with the reset token
		Account user = userService.findByConfirmationToken((String) requestParams.get("token"));

		// Set new password
		user.setPassword(bCryptPasswordEncoder.encode((CharSequence) requestParams.get("password")));

		// Set user to enabled
		user.setEnabled(true);

		// Save user
		userService.addAccount(user);
		modelAndView.addObject("successMessage", "Your password has been set!");
		return modelAndView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("errorMsg", "Your username and password are invalid");
		}

		if (logout != null) {
			model.addAttribute("msg", "You have been logged out successfully");
		}

		return "login";
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
	public String resetPass(Model model){
		Account user = new Account();
		model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
		model.addAttribute("user", user);
		// show email box
		model.addAttribute("showsendemail", true);
		return "resetpass";
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	public String processResetPass(Model model, @RequestParam Map requestParams, RedirectAttributes attributes){
		Zxcvbn passwordCheck = new Zxcvbn();

		Strength strength = passwordCheck.measure((String) requestParams.get("password"));

		if (strength.getScore() < 3) {

			attributes.addFlashAttribute("errorMessage", "Your password is too weak. Choose a stronger one.");

			return "redirect:/myaccount";
		}
		SecurityContext context = SecurityContextHolder.getContext();
        Account user = userService.getOneAccount(context.getAuthentication().getName());
		user.setPassword(bCryptPasswordEncoder.encode((CharSequence) requestParams.get("password")));
		userService.addAccount(user);
		attributes.addFlashAttribute("successpass", "Password updated!");
		return "redirect:/myaccount";
	}

	@RequestMapping(value = "/confirmemail", method = RequestMethod.POST)
	public String confirmEmail(@ModelAttribute("user") Account user, BindingResult bindingResult){
		// find user by email
		// if exists
		// show reset form
		// hide email form

		return "resetpass";
	}

}