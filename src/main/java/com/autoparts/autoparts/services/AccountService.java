// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.Account;
import com.autoparts.autoparts.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    // get all account
    public List<Account> getAllAccounts(){
        return (List<Account>) accountRepository.findAll();
    }

    // get one product
    public Account getOneAccount(String email){
        return accountRepository.findById(email).get();
    }

    public Account findByConfirmationToken(String confirmationToken) {
		return accountRepository.findByConfirmationToken(confirmationToken);
	}

    // add a product
    public void addAccount(Account account){
        accountRepository.save(account);
    }
    
    // update product details
    public void updateAccount(Account account, String email){
        accountRepository.save(account);
    }
    

    // delete a product
    public void delAccount(String email){
        boolean exists = accountRepository.existsById(email);
        if (exists){
            accountRepository.deleteById(email);
        }
        else {
            throw new IllegalStateException("ID doesn't exist"); 
        }
    }

}