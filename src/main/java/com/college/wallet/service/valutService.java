package com.college.wallet.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.college.wallet.model.valuts;
import com.college.wallet.repository.ValutRepository;

@Service
public  class valutService {
    @Autowired
    private ValutRepository valutRepository;
    public RestTemplate restTemplate=new RestTemplate();
    public void adddrip(valuts valut){
        valutRepository.save(valut);
        String url="http://localhost:3000/adddrip/valut";
        restTemplate.postForObject(url,valut,String.class);
    }
}
