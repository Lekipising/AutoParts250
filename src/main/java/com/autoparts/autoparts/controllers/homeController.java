// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import javax.servlet.http.HttpServletRequest;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.EmailSenderService;
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

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "home";
    }

    @RequestMapping("/contact")
    public ModelAndView contact(ModelAndView modelAndView, Model model) {
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
                model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
                modelAndView.setViewName("contact");

            }
        } else {
            modelAndView.addObject("capmessage", "ReCaptcha failed! Please try again");
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
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "about";
    }

    @RequestMapping("/storepolicies")
    public String storepolicies(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "storepolicies";
    }

    @RequestMapping("/shippings")
    public String getAllShippings(Model model) {
        model.addAttribute("shippings", shippingService.getAllShippings());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "shippings";
    }

    @RequestMapping("/users")
    public String getAllAccounts(Model model) {
        model.addAttribute("account", accountService.getAllAccounts());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "users";
    }

    // @RequestMapping("/errors")
    // public String errorse(Model model){
    // model.addAttribute("businessDetails",
    // businessDetailsService.getOneDetail(15L));
    // return "errors";
    // }

    // protect
    @RequestMapping("/myaccount")
    public String myaccount(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Account user = accountService.getOneAccount(context.getAuthentication().getName());
        model.addAttribute("message", "Welcome back, " + user.getFirstName());
        model.addAttribute("message1", user.getFirstName());
        model.addAttribute("message2", user.getSecondName());
        model.addAttribute("message3", user.getPhoneNumber());
        model.addAttribute("message4", user.getUsername());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        return "myaccount";
    }

}
