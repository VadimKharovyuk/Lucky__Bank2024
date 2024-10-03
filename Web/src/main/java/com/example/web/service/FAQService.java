package com.example.web.service;

import com.example.web.dto.FAQDto;
import com.example.web.repository.FAQServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FAQService {
    private final FAQServiceClient client ;

    public List<FAQDto> list(){
        return client.list();
    }

}
