package com.mw.csp.email.service;
//RestAPI Caller

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mw.csp.email.service.XmlToJson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.mozilla.universalchardet.UniversalDetector;

public class EmailService_OLD {

	private static final Logger logger = LoggerFactory.getLogger(EmailService_OLD.class);

	private static final String STATUS_OK = "CSP_SVP_I0001";

	private String product_domain 	= "cspapi.sktelecom.com";
	private String uri 					= "/rest/SCBH000005/";
	// private String uri = "/rest/SCBH000001/";   // SMS
	// private String uri = "/rest/SCBH000005/";   // E-Mail
	// private String uri = "/rest/SCBH000006/";   // NateOnBiz 
	// 150.28.67.38 => 203.236.3.225 => 61.250.23.73 => 71/72
	
	private String apiKey_product     = "i14xz390pddspcbw";  // "MW 운영 포탈" apiKey for CSP 운영 
	private String url = "https://" + product_domain + uri + apiKey_product;

	// private String apiKey_product			= "26v341hresqop8dq";	// "자동화포탈" apiKey 			for CSP 운영
	// private String decryptKey_product		= "73wiv6p54v25067v";		// "자동화포탈" decryptKey	for CSP, Response 확인 필요없으면 안써도됨.
	
	// private String apiKey_dev				= "ait6yl18st8gwq67";		// SA Client apiKey     for CSP
	// private String decryptKey_dev			= "31y52n2lzh1cuuiz";		// SA Client decryptKey for CSP, Response 확인 필요없으면 안써도됨.
	
	// private String apiKey_product			= "mql8uumfr37kx86v";		// SA Client apiKey     for MW Portal ( skt-infmon01 )
	// private String decryptKey_product 	= "slspb87e5d6ozltd";		// SA Client decryptKey for MW Portal ( skt-infmon01 ), Response 확인 필요없으면 안써도됨.
	
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private String msg = "" ;
	private String xmlMsg;
	private String jsonMsg;
	
	public void init() throws UnknownHostException {		
		try {
			System.out.println("Call URL = " + url );
			InetAddress inet= InetAddress.getByName(product_domain);
			System.out.println("init() : Resolved = " + product_domain + " -> " + inet.getHostAddress() );						
		} catch (Exception e) {
			System.out.println("init() : Resolved = " + product_domain + "-> " + "hosts 파일내 ip 설정이 존재하지 않습니다." );
		} finally {
			System.out.println("init() : apiKey = " + apiKey_product );
		}
	}

	public String send(String title, String sender_email, String receiver_email, String contents, String encoding) {
		
		logger.info("at EmailService _ OLD");

		if 		( isHTML(contents) ) contents = StringUtils.replace(contents, "\n", "<p>");
		else 	contents = StringUtils.replace(contents, "\n", "<br>");

		try {			
			if(httpclient == null) init();			
			
			System.out.println("init() : url = " + url );
			HttpPost request = new HttpPost(url);						
						
			List<NameValuePair> postlParameters = new ArrayList<NameValuePair>();

			/*
			postlParameters.add(new BasicNameValuePair("P_SENDUSERID", 		URLEncoder.encode(sendUserId, encoding)));
			postlParameters.add(new BasicNameValuePair("P_SENDUSERNAME", 	URLEncoder.encode(sendUserName, encoding)));
			postlParameters.add(new BasicNameValuePair("P_TARGETUSER", 		URLEncoder.encode(targetUserId, encoding)));
			postlParameters.add(new BasicNameValuePair("P_CONTENT", 			URLEncoder.encode(contents, encoding) ));
			postlParameters.add(new BasicNameValuePair("P_KIND", 				URLEncoder.encode(contents, encoding) ));
			*/

			postlParameters.add(new BasicNameValuePair("SUBJECT", 				title				));
			postlParameters.add(new BasicNameValuePair("SENDEREMAIL", 		sender_email		));
			postlParameters.add(new BasicNameValuePair("RECEIVEREMAIL", 		receiver_email	));
			postlParameters.add(new BasicNameValuePair("CONTENT", 				contents			));

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postlParameters, encoding);
		    request.setEntity(formEntity);
		    
			CloseableHttpResponse response = httpclient.execute(request);			
			xmlMsg = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			
			// System.out.println("HTTP MSG = " + xmlMsg );			
			// check_encoding(resultMsg);
			
			jsonMsg = XmlToJson.convert(xmlMsg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		

		if(StringUtils.contains(xmlMsg, STATUS_OK) )	return jsonMsg;
		else													return jsonMsg;
	}
	
	public String findFileEncoding(String full_path) throws IOException {
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(new File(full_path));
		UniversalDetector detector = new UniversalDetector(null);

		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();

		String encoding = detector.getDetectedCharset();
		if (encoding != null) {
			System.out.println("Detected encoding = " + encoding);
		} else {
			System.out.println("No encoding detected.");
		}

		detector.reset();
		
		// FileInputStream InputStreamReader BufferedReader 를 닫아준다.
		if(fis != null) try{fis.close();}catch(IOException e){}
		
		return encoding;
	}
	
	public int openFile(String full_path, String endcoding) throws IOException {
		try {
			FileInputStream		fis			= new FileInputStream(new File(full_path));
			InputStreamReader	isr			= new InputStreamReader(fis, endcoding);
			BufferedReader		br			= new BufferedReader(isr);			

			System.out.println("File Opened : " + full_path );

			while(true) {
				String str = br.readLine();
				if ( str == null )		break;					
				else 					msg += str + "\n";
			}

			// FileInputStream InputStreamReader BufferedReader 를 닫아준다.
			if(fis != null) try{fis.close();}catch(IOException e){}
			if(isr != null) try{isr.close();}catch(IOException e){}
			if(br != null) try{br.close();}catch(IOException e){}
						
			return msg.length();			
		} catch (IOException e) {
			e.printStackTrace();
			return msg.length();			
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
	
	public String getString() {		return msg ; 	}

}
