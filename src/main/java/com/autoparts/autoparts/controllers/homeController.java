// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import javax.servlet.http.HttpServletRequest;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.classes.Another;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.EmailSenderService;
import com.autoparts.autoparts.services.AnotherService;
import com.autoparts.autoparts.services.OrdersService;
import com.autoparts.autoparts.services.ReCaptchaValidationService;
import com.autoparts.autoparts.services.ShippingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homeController {

    @Autowired
    ReCaptchaValidationService validator;

    @Autowired
    AccountService accountService;

    @Autowired
    ShippingService shippingService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    EmailSenderService emailService;

    @Autowired
    BusinessDetailsService businessDetailsService;

    @Autowired
    AnotherService newsletterService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        return "home";
    }

    @RequestMapping("/contact")
    public ModelAndView contact(ModelAndView modelAndView, Model model) {
        model.addAttribute("newsletter", new Another());
        modelAndView.setViewName("contact");
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return modelAndView;

    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public ModelAndView processContactForm(ModelAndView modelAndView, BindingResult bindingResult,
            HttpServletRequest request, @RequestParam("name") String name, @RequestParam("email") String email,
            @RequestParam("subject") String subject, Model model,
            @RequestParam(name = "g-recaptcha-response") String resp) {

        // captcha
        if (validator.validateCaptcha(resp)) {

            if (bindingResult.hasErrors()) {
                model.addAttribute("newsletter", new Another());
                model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
                modelAndView.setViewName("contact");
            }

            else {
                SimpleMailMessage registrationEmail = new SimpleMailMessage();
                registrationEmail.setTo("autoparts250test@gmail.com");
                registrationEmail.setSubject("Customer Query");
                registrationEmail.setText(name + " message below ::> \n" + subject + ". \nCustomer email, " + email);
                registrationEmail.setFrom("noreply@domain.com");

                emailService.sendEmail(registrationEmail);
                modelAndView.addObject("message2",
                        "We have received your message, one of our agents will get back you. Keep shopping!");
                model.addAttribute("newsletter", new Another());
                model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
                modelAndView.setViewName("contact");

            }
        } else {
            modelAndView.addObject("capmessage", "ReCaptcha failed! Please try again");
            model.addAttribute("newsletter", new Another());
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            modelAndView.setViewName("contact");
        }
        // captcha
        return modelAndView;
    }

    // protect
    @RequestMapping("/productorder")
    public String productorder() {
        return "productorder";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("newsletter", new Another());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "about";
    }

    @RequestMapping("/storepolicies")
    public String storepolicies(Model model) {
        model.addAttribute("newsletter", new Another());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "storepolicies";
    }

    @RequestMapping("/shippings")
    public String getAllShippings(Model model) {
        model.addAttribute("shippings", shippingService.getAllShippings());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        return "shippings";
    }

    @RequestMapping("/users")
    public String getAllAccounts(Model model) {
        model.addAttribute("account", accountService.getAllAccounts());
        model.addAttribute("newsletter", new Another());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "users";
    }

    // protect
    @RequestMapping("/myaccount")
    public String myaccount(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Account user = accountService.getOneAccount(context.getAuthentication().getName());
        model.addAttribute("message", "Welcome back, " + user.getFirstName());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        model.addAttribute("user", user);
        return "myaccount";
    }

    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") Account user, BindingResult bindingResult, Model model,
            @RequestParam(value = "username", required = false) String username) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        if (bindingResult.hasErrors()) {
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            model.addAttribute("newsletter", new Another());
            return "myaccount";
        }

        accountService.addAccount(user);

        model.addAttribute("success", "Your details were updated successfully!");
        return "myaccount";
    }

    @RequestMapping(value = "/addnewsletter", method = RequestMethod.POST)
    public String addNewsletter(@ModelAttribute("newsletter") Another newsletter, BindingResult bindingResult,
            Model model, @RequestParam(value = "email") String email) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newsletter", new Another());
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            return "home";
        }
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        newsletterService.addNewsletter(newsletter);
        model.addAttribute("news", "Successfully subscribed to our newsletter!");
        return "home";
    }

}