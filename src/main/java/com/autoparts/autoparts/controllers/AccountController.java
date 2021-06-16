package com.autoparts.autoparts.controllers;


import java.util.List;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.repository.AccountRepository;
import com.autoparts.autoparts.services.AccountService;

// Authors: Liplan Lekipising and catherine Muthoni
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    // DELETE - delete a product
    @DeleteMapping(path = "account/{email}")
    public void delAccount(@PathVariable("email") String email){
        accountService.delAccount(email);
    }

}
