package com.autoparts.autoparts.services;

import java.util.List;

import com.autoparts.autoparts.classes.BusinessDetails;
import com.autoparts.autoparts.repository.BusinessDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessDetailsService {
    
    @Autowired
    BusinessDetailsRepository businessDetailsRepository;

    public BusinessDetails getOneDetail(Long detailid){
        return businessDetailsRepository.findById(detailid).get();
    }

    public BusinessDetails addDetail(BusinessDetails businessDetails){
        return businessDetailsRepository.save(businessDetails);
    }

    public BusinessDetails updateDetail(Long detailid){
        return businessDetailsRepository.findById(detailid).get();
    }

    public void delDetail(Long detailid){
        boolean exists = businessDetailsRepository.existsById(detailid);
        if (exists){
            businessDetailsRepository.deleteById(detailid);
        }
        else {
            throw new IllegalStateException("ID doesn't exist"); 
        }
    }

    public List<BusinessDetails> getAllDetails(){
        return (List<BusinessDetails>) businessDetailsRepository.findAll();
    }
}
