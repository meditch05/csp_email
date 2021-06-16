package com.mw.csp.email.service;
//RestAPI Caller

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.mw.csp.email.service.XmlToJson;

/*
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.ObjectUtils;
import com.mw.csp.nateonbiz.dto.ResponseDTO;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
*/

public class EmailService_NEW {

	private static final Logger logger = LoggerFactory.getLogger(EmailService_NEW.class);

	private static final String STATUS_OK = "CSP_SVP_I0001";

	private String product_domain 	= "cspapi.sktelecom.com";
	private String uri 					= "/rest/SCBH000006/";
	// private String uri = "/rest/SCBH000001/";   // SMS
	// private String uri = "/rest/SCBH000005/";   // E-Mail
	// private String uri = "/rest/SCBH000006/";   // NateOnBiz 

	private String apiKey_product	= "i14xz390pddspcbw";  // "MW 운영 포탈" apiKey for CSP 운영 
	private String url 					= "https://" + product_domain + uri + apiKey_product;

	private String xmlMSG; 
	private String jsonMSG; 
	// private ResponseDTO dto;
	
	public String send(String title, String sender_email, String receiver_email, String contents, String encoding) {
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		HttpClient httpClient; 
		RestTemplate restTemplate;
		
		logger.info("at EmailService");

		if 		( isHTML(contents) ) contents = StringUtils.replace(contents, "\n", "<p>");
		else 	contents = StringUtils.replace(contents, "\n", "<br>"); 

		try {			
			logger.info("URL = " + url );
			
			factory.setReadTimeout(5000);  // 읽기시간초과(ms) 
			factory.setConnectTimeout(3000);  // 연결시간초과(ms) 
			httpClient = HttpClientBuilder.create()
					.setMaxConnTotal(100) // connection pool 적용. 최대 오픈되는 커넥션수
					.setMaxConnPerRoute(5) // connection pool 적용. IP:PORT 1 쌍에 대해 수행할 연결수 
					.build(); 
			factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
			restTemplate = new RestTemplate(factory);
			
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
			
			MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
			// MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

			postParameters.add("SUBJECT"			, title 				);
			postParameters.add("SENDEREMAIL"		, sender_email	);
			postParameters.add("RECEIVEREMAIL"	, receiver_email	);
			postParameters.add("CONTENT"			, contents			);
			
			HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
			
			/*
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("SUBJECT", 		title		));
			postParameters.add(new BasicNameValuePair("SENDEREMAIL", 	sender_email	));
			postParameters.add(new BasicNameValuePair("RECEIVEREMAIL", 			receiver_email	));
			postParameters.add(new BasicNameValuePair("CONTENT", 			contents	));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity((List<? extends NameValuePair>) postParameters, encoding);
			*/

			////////////////////////////////////////
			// restTemplate Set "UTF-8"
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Config.encode);
			////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			// "RESULT_CODE":"CSP_SYS_E001","RESULT_MESSAGE":"채널서비스플랫폼 시스템 에러"  => 해결않됨
			// POST로 값을 잘 못 넘겼을때 나는 현상임, 변수명이 잘못됬거나, 값이 잘 못됬거나
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			// restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
			// restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter());
			// restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			// restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(encoding)));
			// restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName(encoding)));
			// UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, encoding);
			
			////////////////////////////////////////
			// ResponsetDTO 로 받기
			////////////////////////////////////////
			/*
			dto = restTemplate.postForObject(url, postParameters, ResponseDTO.class );

			System.out.println("여기 - 1");
			System.out.println("DTO.getSuccess()= " + dto.getRESULT_CODE() );
			System.out.println("여기 - 2");
			System.out.println("DTO.toString()= " + dto.toString() );
			System.out.println("여기 - 3");
			*/
			// System.out.println("DTO.getHeaderString()= " + dto.getHeaderString() );
			// System.out.println("DTO.getBodyString()= " + dto.getBodyString() );
			// System.out.println("DTO.toString()= " + dto.toString() );
			// System.out.println("DTO.getRESULT_CODE()= " + dto.getRESULT_CODE() );
			
			//////////////////////////////////////
			// String으로 받기
            //////////////////////////////////////
			ResponseEntity<String> response = restTemplate.exchange("https://example.com/api/request", HttpMethod.POST, formEntity, String.class);
			// xmlMSG = restTemplate.postForObject(url, formEntity, String.class);
			// xmlMSG = restTemplate.postForObject(url, postParameters, String.class);
			// System.out.println("xmlMSG : " + xmlMSG);

			//////////////////////////////////////
			// XML => Json String
            //////////////////////////////////////
			jsonMSG = XmlToJson.convert(xmlMSG);
			// System.out.println("jsonMSG : " + jsonMSG);
			
			//////////////////////////////////////
			// XML 문자열 => DTO 객체 전환
			/////////////////////////////////////
			// JAXBContext jaxbContext = JAXBContext.newInstance(ResponseDTO.class);
			// Unmarshaller jaxbunmarshaller = jaxbContext.createUnmarshaller();
			// dto = (ResponseDTO) jaxbunmarshaller.unmarshal(new StringReader(xmlMSG));
			
		} catch (Exception e) {
			e.printStackTrace();
		}		

		if (StringUtils.contains(jsonMSG, STATUS_OK) ) {
			return jsonMSG;
		} else {
			return jsonMSG;
		}
	}
	
	public boolean isHTML(String contents) {
		if ( contents.contains("<html") || contents.contains("<table") || contents.contains("<br") || contents.contains("<td>") ) {
			System.out.println("isHTML() : 파일이 HTML 양식입니다.");
			return true;
		}
		else {
			System.out.println("isHTML() : 파일이 Text 양식입니다.");
			return false;
		}
	}

}