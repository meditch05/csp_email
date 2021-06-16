package com.mw.csp.email.service;
// https://javainterviewpoint.com/xml-to-json-java/

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlToJson
{
	private static final Logger logger = LoggerFactory.getLogger(XmlToJson.class);
	
    public static String convert(String xmlString)    {
    	 
        String jsonString = xmlString;
        
        try
        {
            // Create a new XmlMapper to read XML tags
            XmlMapper xmlMapper = new XmlMapper();
            
            //Reading the XML
            JsonNode jsonNode = xmlMapper.readTree(xmlString.getBytes());
            
            //Create a new ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            
            //Get JSON as a string
            jsonString = objectMapper.writeValueAsString(jsonNode);
            
            // logger.info("*** Converting XML to JSON ***");
            // logger.info(jsonString);

        } catch (JsonParseException e)
        {
            e.printStackTrace();
        } catch (JsonMappingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return jsonString;
        
    }
}