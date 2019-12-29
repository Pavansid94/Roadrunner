package com.upb.factchecker.util;

import java.util.HashMap;

public class CharsetUtil {
	
	static final public HashMap<Character, Character> CHARSET = new HashMap<Character, Character>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put('à','a');
		put('á','a');
		put('â','a');
		put('ã','a');
		put('ä','a');
		put('å','a');
		put('À','A');
		put('Á','A');
		put('Â','A');
		put('Ã','A');
		put('Ä','A');
		put('Å','A');
		
		put('è','e');
		put('é','e');
		put('ê','e');
		put('ë','e');
		put('È','E');
		put('É','E');
		put('Ê','E');
		put('Ë','E');
		
		put('ì','i');
		put('í','i');
		put('î','i');
		put('ï','i');
		put('Ì','I');
		put('Í','I');
		put('Î','I');
		put('Ï','I');
		
		put('ò','o');
		put('ó','o');
		put('ô','o');
		put('õ','o');
		put('ö','o');
		put('ø','o');
		put('ō','o');
		put('Ò','O');
		put('Ó','O');
		put('Ô','O');
		put('Õ','O');
		put('Ö','O');
		put('Ø','O');
		put('Ō','O');
		
		put('ù','u');
		put('ú','u');
		put('û','u');
		put('ü','u');
		put('Ù','U');
		put('Ú','U');
		put('Û','U');
		put('Ü','U');
		
		put('ý','y');
		put('ÿ','y');
		put('Ý','Y');
		
		put('š','s');
		put('ç','c');
		put('č','c');
		put('ć','c');
		put('ñ','n');
		
	}};
	
	//Method to replace UniCode characters with its UTF-8 counterparts
	public static String replaceUniCodeChars(String str){
		
		String copy = str;
		for(Character ch:str.toCharArray()){
			if(CHARSET.containsKey(ch)){
				Character replacementValue = CHARSET.get(ch);
				return (replaceUniCodeChars(copy.replace(ch, replacementValue)));
			}
		}
		return copy;
	}
}
