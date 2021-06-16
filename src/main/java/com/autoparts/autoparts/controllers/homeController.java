// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import javax.servlet.http.HttpServletRequest;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.EmailSenderService;
import com.autoparts.autoparts.services.OrdersService;
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
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "home";
    }

    @RequestMapping("/contact")
    public ModelAndView contact(ModelAndView modelAndView, Model model) {
		modelAndView.setViewName("contact");
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
		return modelAndView;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
	public ModelAndView processContactForm(ModelAndView modelAndView, BindingResult bindingResult, HttpServletRequest request, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("subject") String subject) {
	
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message1", "Error in contact form");
            modelAndView.setViewName("contact");
        }

        else { 
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo("autoparts250test@gmail.com");
            registrationEmail.setSubject("Customer Query");
            registrationEmail.setText(name + " message below ::> \n" + subject + ". \nCustomer email, " + email);
            registrationEmail.setFrom("noreply@domain.com");

            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("message2", "We have received your message, one of our agent will get back you. Keep shopping!");
            modelAndView.setViewName("contact");
			
		}
		return modelAndView;
	}


    // protect
    @RequestMapping("/productorder")
    public String productorder() {
        return "productorder";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "about";
    }

    @RequestMapping("/storepolicies")
    public String storepolicies(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "storepolicies";
    }

    @RequestMapping("/shippings")
    public String getAllShippings(Model model){
        model.addAttribute("shippings", shippingService.getAllShippings());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "shippings";
    }

    @RequestMapping("/users")
    public String getAllAccounts(Model model){
        model.addAttribute("account", accountService.getAllAccounts());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "users";
    }

    // @RequestMapping("/errors")
    // public String errorse(Model model){
    //     model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
    //     return "errors";
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
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(15L));
        return "myaccount";
    }


}