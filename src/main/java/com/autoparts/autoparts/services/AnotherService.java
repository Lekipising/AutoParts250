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

    public Another getOneNewsletter(String id){
        return newsletterRepository.findById(id).get();
    }

    public List<Another> getAllNewsletters(){
        return (List<Another>) newsletterRepository.findAll();
    }

    public Another addNewsletter(Another newsletter){
        return newsletterRepository.save(newsletter);
    }

    public void deleteNewsletter(String id){
        newsletterRepository.deleteById(id);
    }

    public Another findByConfirmationToken(String conf) {
		return newsletterRepository.findByConf(conf);
	}


}
