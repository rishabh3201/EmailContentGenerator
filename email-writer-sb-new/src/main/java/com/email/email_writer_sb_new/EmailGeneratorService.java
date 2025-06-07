package com.email.email_writer_sb_new;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;

import java.util.*;
@Service
public class EmailGeneratorService {
	
	private final WebClient webClient;
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	public EmailGeneratorService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
//		this.webClient = webClientBuilder.baseUrl(geminiApiUrl).build();
	}
	public String generateEmailReply(EmailRequest emailRequest) {
		
//		return "Generated Email with tone '" + emailRequest.getTone() + "': " + emailRequest.getEmailContent();
		  
//		Build the prompt
		String prompt = buildPrompt(emailRequest);
//		
//		return prompt;
//		craft the request
		Map<String,Object> requestBody = Map.of(
				"contents", new Object[] {
						Map.of("parts",new Object[] {
								Map.of("text",prompt)
						})
				}
			);
//		DO request and get response
		String response = webClient.post()
				.uri(geminiApiUrl + geminiApiKey)
				.header("Content-Type","application/json")
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
//		
//		try {
//            // Send the request and get response
//			String response = webClient.post()
//					.uri(geminiApiUrl + geminiApiKey)
//					.header("Content-Type","application/json")
//					.bodyValue(requestBody)
//					.retrieve()
//					.bodyToMono(String.class)
//					.block(); // Blocking call (consider using async instead)
//
//            return extractResponseContent(response);
//
//        } catch (WebClientResponseException e) {
//            return "API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
//        } catch (Exception e) {
//            return "Error processing request: " + e.getMessage();
//        }
		
		
		
////		extract response and then return response
		return extractResponseContent(response);
	}

	private String extractResponseContent(String response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(response);
			return rootNode.path("candidates")
					.get(0)
					.path("content")
					.path("parts")
					.get(0)
					.path("text")
					.asText();
		}catch(Exception e) {
			return "Error processing report: " + e.getMessage();
		}
	}
	private String buildPrompt(EmailRequest emailRequest) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Generate  a professional email reply for the following email content. Please generate the subject line also");
		if(emailRequest.getTone()!=null) {
			prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
		}
		prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
		return prompt.toString();
	}
}
