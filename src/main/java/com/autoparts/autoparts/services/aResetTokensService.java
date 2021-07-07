package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.aResetTokens;
import com.autoparts.autoparts.repository.aResetTokensRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class aResetTokensService {
    @Autowired
    aResetTokensRepository resetTokensRepository;

    public List<aResetTokens> getAllTokens(){
        return (List<aResetTokens>) resetTokensRepository.findAll();
    }

    public aResetTokens getOneToken(Integer tkn){
        return resetTokensRepository.findById(tkn).get();
    }

    public void addToken(aResetTokens resetTokens){
        resetTokensRepository.save(resetTokens);
    }

    public void delToken(Integer tkn){
        resetTokensRepository.deleteById(tkn);   
    }
}
