package com.foodorder.order.client;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.foodorder.order.dto.external.UserResponse;

//User Service Validation using synchronous client.
@Component
public class UserServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
	
	private final RestTemplate restTemplate;
	
	@Value("${services.user-service.url}")
	private String userServiceUrl;
	
	
	public UserServiceClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	public Optional<UserResponse> getUserById(String userId){
		
		try {
			
			String url = userServiceUrl + "/api/users/" + userId;
			logger.info("UserServiceClient.getUserById() service url: " + url);
			UserResponse userResponse = restTemplate.getForObject(url, UserResponse.class);
			return Optional.ofNullable(userResponse);
			
		}catch(HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}catch(Exception e) {
			throw new RuntimeException("Failed to fetch user" +userId,e);
		}
		
	}
}
