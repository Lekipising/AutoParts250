// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.EmailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
	@Autowired
	EmailSenderService emailService;
	@Autowired
	AccountService accountService;

	// Return registration form template
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, Account user) {
		modelAndView.addObject("account", user);
		modelAndView.setViewName("create");
		return modelAndView;
	}

	// Process form input data
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView processRegistrationForm(ModelAndView modelAndView,
			@ModelAttribute("account") @Valid Account user, BindingResult bindingResult, HttpServletRequest request,
			@RequestParam("username") String username) {
		try {
			Account exists = accountService.getOneAccount(username);
			modelAndView.addObject("exists", "Email already in use, try a different one");
			modelAndView.setViewName("create");

		} catch (NoSuchElementException e) {
			if (bindingResult.hasErrors()) {
				modelAndView.setViewName("create");
			}

			else { // new user so we create user and send confirmation e-mail
				user.setUsername(username);
				// Disable user until they click on confirmation link in email
				user.setEnabled(false);
				// Generate random 36-character string token for confirmation link
				user.setConfirmationToken(UUID.randomUUID().toString());
				user.setRole("ADMIN");
				accountService.addAccount(user);

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
				modelAndView.setViewName("create");
			}
		}

		return modelAndView;
	}
}