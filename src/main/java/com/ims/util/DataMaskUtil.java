package com.ims.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class DataMaskUtil {
	
	private DataMaskUtil(){
		
	}
	
	public static String maskData(String ticketData){
		return maskedNlpString(ticketData);
		
	}
	
	private static String maskedNlpString(String ticketData){
		Map< String, String> tokenMap = new HashMap<String, String>();
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	 // create an empty Annotation just with the given text
	    Annotation document = new Annotation(ticketData);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    int i = 0;
	    for(CoreMap sentence: sentences) {
	        // traversing the words in the current sentence
	        // a CoreLabel is a CoreMap with additional token-specific methods
	        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	            // this is the text of the token
	            String word = token.get(TextAnnotation.class);
	            // this is the POS tag of the token
	            String pos = token.get(PartOfSpeechAnnotation.class);
	            // this is the NER label of the token
	            String ne = token.get(NamedEntityTagAnnotation.class);
	            int lastIndex =  ticketData.indexOf(word)+word.length();
	            System.out.println("word: " + word + " pos: " + pos + " ne:" + ne+" :index in sentence is"
	            							+ticketData.indexOf(word)+" last index to replace:"+lastIndex+" lenght of the word:"+word.length());
	            if(ne.equalsIgnoreCase("DATE") || ne.equalsIgnoreCase("TIME")|| ne.equalsIgnoreCase("URL") 
	            		||ne.equalsIgnoreCase("DURATION") || ne.equalsIgnoreCase("EMAIL")|| ne.equalsIgnoreCase("PERSON")
	            		||ne.equalsIgnoreCase("CITY") ) {
	            	tokenMap.put("ne:"+(i++), ticketData.indexOf(word)+" "+lastIndex+" " +word.length());
	            }

	        }
	        	
	    }
	    StringBuilder outputString = new StringBuilder(ticketData);
	    
	    for (String key : tokenMap.keySet()) {
		    String value = tokenMap.get(key);
		    String[] values = value.split("\\s");
		    for(int j=0;j<Integer.parseInt(values[2]);j++) {
		    	outputString.setCharAt(Integer.parseInt(values[0])+j, '*');	
		    }
	    }
	    System.out.println("Replaced Text is :");
	    System.out.println("***************************");
	    System.out.println(outputString);    
	    
		return maskEmailId(outputString.toString());
	}
	
	private static String maskEmailId(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedEmail = matcher.replaceAll("****@*****");
		return maskPhoneNumberFormatOne(maskedEmail);
	}
	
	private static String maskPhoneNumberFormatOne(String ticketData){
		Pattern pattern = Pattern.compile("\\+\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatOne = matcher.replaceAll("************");
		return maskPhoneNumberFormatTwo(maskedPhoneNumberFormatOne);
	}
	
	private static String maskPhoneNumberFormatTwo(String ticketData){
		Pattern pattern = Pattern.compile("\\+\\d{2}[-\\.\\s]\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatTwo = matcher.replaceAll("************");
		return maskPhoneNumberFormatThree(maskedPhoneNumberFormatTwo);
	}
	
	private static String maskPhoneNumberFormatThree(String ticketData){
		Pattern pattern = Pattern.compile("\\d{2}[-\\.\\s]\\d{10}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatThree = matcher.replaceAll("**********");
		return maskPhoneNumberFormatFour(maskedPhoneNumberFormatThree);
	}
	
	private static String maskPhoneNumberFormatFour(String ticketData){
		Pattern pattern = Pattern.compile("\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedPhoneNumberFormatFour = matcher.replaceAll("**********");
		return ssn(maskedPhoneNumberFormatFour);
	}
	
	private static String ssn(String ticketData){
		Pattern pattern = Pattern. compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$");
		Matcher matcher = pattern.matcher(ticketData);
		String maskedSsn = matcher.replaceAll("*********");
		return maskCompanyName(maskedSsn);
	}
	
	private static String maskCompanyName(String ticketData){
		Pattern pattern = Pattern. compile("[a-zA-Z0-9_.+-]+[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
		Matcher matcher = pattern.matcher(ticketData);
		return matcher.replaceAll("*********");
	}

}
