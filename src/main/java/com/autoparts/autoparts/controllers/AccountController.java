package com.autoparts.autoparts.controllers;


import java.util.List;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.repository.AccountRepository;
import com.autoparts.autoparts.services.AccountService;

// Authors: Liplan Lekipising and catherine Muthoni
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    // GET - one product
    @GetMapping(path = "/account/{email}")
    public Account getOneAccount(@PathVariable("email") String email){
        return accountService.getOneAccount(email);
    }

    // GET - all account
    @GetMapping(path = "/account")
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }


    // add a product - using a form - admin
    @RequestMapping(value="/account", method=RequestMethod.POST)
    public String saveProductSubmission(@ModelAttribute Account account, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            return "home";
        }
        accountService.addAccount(account);

        return "home";
    }


    // DELETE - delete a product
    @DeleteMapping(path = "account/{email}")
    public void delAccount(@PathVariable("email") String email){
        accountService.delAccount(email);
    }

}
