// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.classes.Another;
import com.autoparts.autoparts.classes.aResetTokens;
import com.autoparts.autoparts.services.AccountService;
import com.autoparts.autoparts.services.BusinessDetailsService;
import com.autoparts.autoparts.services.EmailSenderService;
import com.autoparts.autoparts.services.AnotherService;
import com.autoparts.autoparts.services.OrdersService;
import com.autoparts.autoparts.services.ReCaptchaValidationService;
import com.autoparts.autoparts.services.ShippingService;
import com.autoparts.autoparts.services.aResetTokensService;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    aResetTokensService ResetTokensService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        model.addAttribute("hide", true);
        return "home";
    }

    @RequestMapping("/contact")
    public ModelAndView contact(ModelAndView modelAndView, Model model) {
        model.addAttribute("newsletter", new Another());
        modelAndView.setViewName("contact");
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
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
        model.addAttribute("hide", true);
        return "about";
    }

    @RequestMapping("/storepolicies")
    public String storepolicies(Model model) {
        model.addAttribute("newsletter", new Another());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
        return "storepolicies";
    }

    @RequestMapping("/shippings")
    public String getAllShippings(Model model) {
        model.addAttribute("shippings", shippingService.getAllShippings());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
        model.addAttribute("newsletter", new Another());
        return "shippings";
    }

    @RequestMapping("/users")
    public String getAllAccounts(Model model) {
        model.addAttribute("account", accountService.getAllAccounts());
        model.addAttribute("newsletter", new Another());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
        return "users";
    }

    // protect
    @RequestMapping("/myaccount")
    public String myaccount(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Account user = accountService.getOneAccount(context.getAuthentication().getName());
        model.addAttribute("message", "Welcome back, " + user.getFirstName());
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
        model.addAttribute("newsletter", new Another());
        model.addAttribute("user", user);
        // generate email verify token
        // Random random = new Random();
        // int val = random.nextInt(999999);
        // String confirmCode = String.format("%06d", val);
        // model.addAttribute("coEmail", confirmCode);

        return "myaccount";
    }

    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public String updateUser(Model model, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes attributes) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("hide", true);
        model.addAttribute("newsletter", new Another());
        if (bindingResult.hasErrors()) {
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            model.addAttribute("hide", true);
            model.addAttribute("newsletter", new Another());
            return "myaccount";
        }

        SecurityContext context = SecurityContextHolder.getContext();
        Account user = accountService.getOneAccount(context.getAuthentication().getName());

        String fn = (String) requestParams.get("firstName");
        String sn = (String) requestParams.get("secondName");
        String pn = (String) requestParams.get("phoneNumber");

        if (!fn.equals("")) {
            user.setFirstName((String) requestParams.get("firstName"));
        }

        if (!sn.equals("")) {
            user.setSecondName((String) requestParams.get("secondName"));
        }

        if (!pn.equals("")) {
            user.setPhoneNumber((String) requestParams.get("phoneNumber"));
        }

        accountService.addAccount(user);
        attributes.addFlashAttribute("successedit", "Your details were updated successfully!");
        return "redirect:/myaccount";
    }

    @RequestMapping(value = "/addnewsletter", method = RequestMethod.POST)
    public String addNewsletter(@ModelAttribute("newsletter") Another newsletter, BindingResult bindingResult,
            Model model, @RequestParam(value = "email") String email, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newsletter", new Another());
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            model.addAttribute("hide", true);
            return "home";
        }
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        model.addAttribute("newsletter", new Another());
        // check if --email-- already exists
        try {
            Another exists = newsletterService.getOneNewsletter(email);
            model.addAttribute("exists", "Email already in use, try a different one");
            return "home";
        } catch (NoSuchElementException e) {
            // random6 digit code
            Random random = new Random();
            int val = random.nextInt(999999);
            String confirmCode = String.format("%06d", val);
            newsletter.setConf(confirmCode);
            // send confirmation
            // send to email
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(email);
            registrationEmail.setSubject("Verify Newsletter Signup");
            registrationEmail.setText("Use this code to verify newsletter signup " + confirmCode);
            registrationEmail.setFrom("noreply@domain.com");
            emailService.sendEmail(registrationEmail);
            model.addAttribute("sent", "Verification code sent to the email. Enter below to confirm");
            model.addAttribute("show", true);
            // set enabled
            newsletter.setEnabled(true);
            newsletterService.addNewsletter(newsletter);
            // end - success
        }

        return "home";
    }

    @RequestMapping(value = "/confirmnews", method = RequestMethod.POST)
    public String confirmEmail(Model model, @RequestParam(value = "conf") String conf) {
        model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
        try {
            Another news = newsletterService.findByConfirmationToken(conf);
            // get datetime created on
            LocalDateTime sentOn = news.getSendTime();
            // get datetime now
            LocalDateTime now = LocalDateTime.now();
            long minutes = ChronoUnit.MINUTES.between(sentOn, now);
            // check if 30mins elapsed
            if (minutes > 2) {
                model.addAttribute("newsletter", new Another());
                model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
                model.addAttribute("tm", "Code Expired, try again");
                return "home";
            }
        } catch (NullPointerException e) {
            model.addAttribute("newsletter", new Another());
            model.addAttribute("businessDetails", businessDetailsService.getOneDetail(0L));
            model.addAttribute("coderr", "Wrong verification, please try again");
            return "home";
        }

        // true - save email -success
        model.addAttribute("newsletter", new Another());
        model.addAttribute("hide", true);
        model.addAttribute("news", "Successfully subscribed to our newsletter!");
        return "home";
    }

    @RequestMapping(value = "/sendEmailP", method = RequestMethod.POST)
    public String sendEmailP(Model model, @RequestParam Map requestParams, RedirectAttributes attributes){
        SecurityContext context = SecurityContextHolder.getContext();
        Account user = accountService.getOneAccount(context.getAuthentication().getName());
        // generate code
        Random random = new Random();
        int val = random.nextInt(999999);
        String confirmCode = String.format("%06d", val);
        // send to email
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getUsername());
        registrationEmail.setSubject("Confirmation Code");
        registrationEmail.setText("Use this code to verify " + confirmCode);
        registrationEmail.setFrom("noreply@domain.com");
        emailService.sendEmail(registrationEmail);

        aResetTokens newTkn = new aResetTokens();
        Integer neT = Integer.valueOf(confirmCode);
        newTkn.setTkn(neT);
        ResetTokensService.addToken(newTkn);

        attributes.addFlashAttribute("sentEmailS", "Confirmation code sent!");
        attributes.addFlashAttribute("showEnterCode", true);

        return "redirect:/myaccount";
    }

    @RequestMapping(value = "/confirmCode", method = RequestMethod.POST)
    public String verifyCode(Model model, @RequestParam Map requestParams, RedirectAttributes attributes){
        String receivedCode = (String) requestParams.get("vercode");
        Integer receivedCodeInt = Integer.valueOf(receivedCode);
        try {
            aResetTokens theToken = ResetTokensService.getOneToken(receivedCodeInt);
        } catch (NoSuchElementException e) {
            attributes.addFlashAttribute("wrongCode", "Wrong verification code!");
            return "redirect:/myaccount";
        }

        ResetTokensService.delToken(receivedCodeInt);
        
        attributes.addFlashAttribute("codeCorrect", "Code verified!");
        attributes.addFlashAttribute("showResetPass", true);

        return "redirect:/myaccount";
    }

}