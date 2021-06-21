package com.autoparts.autoparts.controllers;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
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
import com.autoparts.autoparts.services.AccountService;
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
	public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, AccountService userService,
			EmailSenderService emailService) {

		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
		this.emailService = emailService;
	}

	// Return registration form template
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, Account user) {
		modelAndView.addObject("account", user);
		modelAndView.setViewName("signup");
		return modelAndView;
	}

	// Process form input data
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView processRegistrationForm(ModelAndView modelAndView,
			@ModelAttribute("account") @Valid Account user, BindingResult bindingResult, HttpServletRequest request,
			@RequestParam("username") String username, @RequestParam(name = "g-recaptcha-response") String resp,
			Model model) {
		if (validator.validateCaptcha(resp)) {
			try {
				Account exists = userService.getOneAccount(username);
				modelAndView.addObject("exists", "Email already in use, try a different one");
				modelAndView.setViewName("signup");

			} catch (NoSuchElementException e) {
				if (bindingResult.hasErrors()) {
					modelAndView.setViewName("signup");
				}

				else { // new user so we create user and send confirmation e-mail
					user.setUsername(username);
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

		return modelAndView;
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

		Account user = userService.findByConfirmationToken(token);

		if (user == null) { // No token found in DB
			modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
		} else { // Token found
			modelAndView.addObject("confirmationToken", user.getConfirmationToken());
		}

		modelAndView.setViewName("confirm");
		return modelAndView;
	}

	// Process confirmation link
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult,
			@RequestParam Map requestParams, RedirectAttributes redir) {
		modelAndView.setViewName("confirm");

		Zxcvbn passwordCheck = new Zxcvbn();

		Strength strength = passwordCheck.measure((String) requestParams.get("password"));

		if (strength.getScore() < 3) {
			bindingResult.reject("password");

			redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

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
		System.out.println(requestParams.get("password"));
		userService.addAccount(user);
		System.out.println(user.getPassword());
		modelAndView.addObject("successMessage", "Your password has been set!");
		return modelAndView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage(Model model, String error, String logout) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			model.addAttribute("loggedin", auth.getName() + "is currently logged in");
			return "login";
		}

		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
			model.addAttribute("loggedin", auth.getName() + "is currently logged in");
			return "login";
		}

		if (error != null) {
			model.addAttribute("errorMsg", "Your username and password are invalid.");
		}

		if (logout != null) {
			model.addAttribute("msg", "You have been logged out successfully.");
		}

		return "login";
	}

}