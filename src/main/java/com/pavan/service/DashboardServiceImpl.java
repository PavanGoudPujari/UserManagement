package com.pavan.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pavan.dto.QuoteApiResponseDTO;

@Service
public class DashboardServiceImpl implements DashboardService{
	
	private String quoteApiURL="https://dummyjson.com/quotes/random";

	@Override
	public QuoteApiResponseDTO getQuote() {
		
		RestTemplate rt = new RestTemplate();
		
		//send HTTP get req and store response into QuoteApiResponse Object
		ResponseEntity<QuoteApiResponseDTO> forEntity =rt.getForEntity(quoteApiURL, QuoteApiResponseDTO.class);
		
		QuoteApiResponseDTO body=forEntity.getBody();
		
		return body;
	}

}
