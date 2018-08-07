package com.oz4cs.eformchecker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * 
 * @author PHAM
 *
 */
public class CommonUtil {
	
	private static final Logger logger = Logger.getLogger(CommonUtil.class);
	
	/**
	 * get Language
	 * @param language
	 * @return
	 */
	public static String getLanguage(String language) {

		/*
		request header | Accept-Language
		FF: ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3
		Chrome: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4
		IE: ko
		*/

		String firstLang = language.split(",")[0];
		if (firstLang.indexOf("ko") > -1) {
			return "ko";
		}else if (firstLang.indexOf("ja") > -1) {
			return "ja";
		}
		return "en";
	}
	
	/**
	 * Blank 
	 * @param str
	 * @return
	 */
	public static String getBlank(String str) {
		if(str == null || str.equals("") || str.equals("null")) {
			return "";
		} else {
			return str;
		}
	}
	
	/**
	 * Check for empty objects
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}
		if ((s instanceof String) && (((String)s).trim().length() == 0)) {
			return true;
		}
		if (s instanceof Map) {
			return ((Map<?, ?>)s).isEmpty();
		}
		if (s instanceof List) {
			return ((List<?>)s).isEmpty();
		}
		if (s instanceof Object[]) {
			return (((Object[])s).length == 0);
		}
		return false;
	}
	
	/**
	 * Check Email 
	 * @param email
	 * @return
	 */
	public static boolean isCheckEmail(String email){
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * Gen Random Password 
	 * @param len
	 * @return
	 */
	public static String getRandomPassword(int len) {
		char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
				'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
				'U', 'V', 'W', 'X', 'Y', 'Z' };
		
		StringBuilder sb = new StringBuilder("");
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(chars[random.nextInt(chars.length)]);
		}
		return sb.toString();
	}
	
	/**
	 * Gen AuthKey
	 * @param authKey
	 * @return
	 */
	public static String getSHAKey(String authKey) {
		/*
		 Crreate Auth Key SHA-256
		 email + passwd = 64Len
		 */
		StringBuilder sb = new StringBuilder("");
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(authKey.getBytes());
			byte byteData[] = md.digest();
			// convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<byteData.length;i++) {
				String hex=Integer.toHexString(0xff & byteData[i]);
				if(hex.length()==1) hexString.append('0');
				hexString.append(hex);
			}
			sb.append(hexString.toString());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * Base64Decoder
	 * @param str
	 * @return
	 */
	public static String getBase64Decoder(String str) {
		Base64 decoder = new Base64();
		byte[] authKeyBytes = decoder.decode(str);
		return new String(authKeyBytes);
	}
	
	/**
	 * - TimeZone: Asia/Singapore, Asia/Seoul
	 * @param timeZone
	 * @param dateStr
	 * @return
	 */
	public static Date getTZDF(String timeZone, String dateStr){
		SimpleDateFormat simpleDateFormat = null;
		if (timeZone.equals("Asia/Singapore")) {
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		} else if (timeZone.equals("Asia/Seoul")) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} 
		
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * - TimeZone: Asia/Singapore, Asia/Seoul 
	 * @param timeZone
	 * @param date
	 * @return
	 */
	public static String getTZDF(String timeZone, Date date){
		
		// TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		
		SimpleDateFormat simpleDateFormat = null;
		if (timeZone.equals("Asia/Singapore")) {
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		} else if (timeZone.equals("Asia/Seoul")) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		} 
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		Timestamp timestamp = new Timestamp(date.getTime());
		return simpleDateFormat.format(timestamp); 
	}
	
	/**
	 * Check Number
	 * @param number
	 * @return
	 */
	public static boolean isCheckNumber(String number){
		String regex = "^[0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}
	
	/**
	 * Current Date  - yyyyMMddHHmmss
	 * @return
	 */
	public static String getCurrentDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new java.util.Date())+ System.currentTimeMillis();
	}
	
	/**
	 * Check Length 
	 * @param s
	 * @param maxLength
	 * @return
	 */
	public static boolean isMaxLen(String s, int maxLength) {
		boolean is = false;
		
		//logger.debug(s.length() +"=="+ maxLength);
		if (s.length() > maxLength) {
			is = true;
		}
		return is;
	}	
}
