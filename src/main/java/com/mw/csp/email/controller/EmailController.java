package com.mw.csp.email.controller;

// RestAPI Receiver 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mw.csp.email.service.EmailService_NEW;
import com.mw.csp.email.service.EmailService_OLD;
 
@RestController
public class EmailController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	// private ResponseDTO dto;
	private String result; 

	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	String send_memo(HttpServletRequest request) throws ServletException, IOException {
		
		String title			= (String) request.getParameter("title");
		String sender_email		= (String) request.getParameter("sdr_email");
		String receiver_email	= (String) request.getParameter("rcv_email");
		String contents			= (String) request.getParameter("contents");
		String encoding			= "UTF-8";
		
		// String all 			= title + "<BR>" + email + "<BR>" + msg;
		// String result 		= "[Excute] Ver "+ version;
		
		EmailService_OLD email = new EmailService_OLD();
		// EmailService_NEW email = new EmailService_NEW();
		result = email.send(title, sender_email, receiver_email, contents, encoding);

		logger.info("result : " + result);
		// return result + "<BR>" + all + "<BR>" + dto.toString() ;
		// return result + "<BR><BR>" + all + "<BR><BR>" + StringUtils.replace( StringUtils.replace(dto.toString(), "<", "&lt" ), ">", "&gt" );
		return result;
	}
}