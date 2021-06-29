package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.Another;
import com.autoparts.autoparts.repository.AnotherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotherService {
    
    @Autowired
    AnotherRepository newsletterRepository;

    public Another getOneNewsletter(Long id){
        return newsletterRepository.findById(id).get();
    }

    public List<Another> getAllNewsletters(){
        return (List<Another>) newsletterRepository.findAll();
    }

    public Another addNewsletter(Another newsletter){
        return newsletterRepository.save(newsletter);
    }

    public void deleteNewsletter(Long id){
        newsletterRepository.deleteById(id);
    }
}
