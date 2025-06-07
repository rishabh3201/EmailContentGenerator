package com.email.email_writer_sb_new;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
//@AllArgsConstructor
@RequiredArgsConstructor

public class EmailGeneratorController {
	private final EmailGeneratorService emailGeneratorService;
	
	@Autowired // Let Spring inject the dependency
    public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }

	@PostMapping("/generate")
	public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
		String response = emailGeneratorService.generateEmailReply(emailRequest);
		return ResponseEntity.ok(response);
	}
}



//package com.email.email_writer_sb_new;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//
//@RestController
//@RequestMapping("/api/email")
//@RequiredArgsConstructor
//public class EmailGeneratorController {
//
//    private final EmailGeneratorService emailGeneratorService = new EmailGeneratorService();
//
//    @PostMapping("/generate")
//    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
//        String response = emailGeneratorService.generateEmailReply(emailRequest);
//        return ResponseEntity.ok(response);
//    }
//}
