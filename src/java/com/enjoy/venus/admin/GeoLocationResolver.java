package com.enjoy.venus.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoLocationResolver {
	static private final String BAIDU_VENUS_SK = "LzvELOKlwM66OGZpdApUYtrTA7FopDgj";
	static private final String BAIDU_VENUS_AK = "4c5OS5cGGjmPr4ERO1gcByk8";
	static private final String BAIDU_MAP_BASE_URL = "http://api.map.baidu.com/geocoder/v2/?";
	
	static private final int READ_TIME_OUT = 3000; //ms
	
	static public final int ERR_SUCCESS = 0;
	static public final int ERR_UNKNOWN = -1;
	static public final int ERR_UNKNOWN_ADDR = -2;
	static public final int ERR_NETWORK = -3;
	
	
	private String caculateBaiduSN(String queryString){
		String wholeStr = "/geocoder/v2/?" + queryString + BAIDU_VENUS_SK;
        try {
        	String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(tempStr.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                 sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
            
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
	    	
	    }
        return null;
	}
	
	private String buildQueryUrl(String address){
		StringBuffer queryString = new StringBuffer();
		try {
			queryString.append("address=").append(URLEncoder.encode(address, "UTF-8"));
			queryString.append("&output=json");
			queryString.append("&ak=").append(URLEncoder.encode(BAIDU_VENUS_AK, "UTF-8"));
			queryString.append("&sn=" + caculateBaiduSN(queryString.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		return BAIDU_MAP_BASE_URL + queryString.toString();
	}
	
	private int parseLocation(String jsonLocationString, double[] location){
		int retCode = ERR_UNKNOWN_ADDR;
		
		try {
			JSONObject jsonObj = new JSONObject(jsonLocationString);
			int status = jsonObj.getInt("status");
			if(status == 0){
				JSONObject result = jsonObj.optJSONObject("result");
				if(result != null ){
					JSONObject locationObj = result.optJSONObject("location");
					if(locationObj != null){
						int confidence = result.optInt("confidence");
						String level = result.optString("level");
						location[0] = locationObj.getDouble("lng");
						location[1] = locationObj.getDouble("lat");
						retCode = ERR_SUCCESS;
					}
					
				}
			}
		} catch (JSONException e) {
			retCode = ERR_UNKNOWN;
		}
		
		return retCode;
	}
	
	public int resolveAddress(String address, double[] location){
		int retCode = ERR_UNKNOWN;
		
		String queryUrl = buildQueryUrl(address);
		HttpURLConnection connection = null;
		int retry = 3;
		while(retry -- > 0){
			
	        try {
	        	URL url = new URL(queryUrl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(READ_TIME_OUT);
				connection.setRequestMethod("GET");
				connection.connect();
				InputStream is = connection.getInputStream();
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
		            InputStreamReader isr = new InputStreamReader(is);  
		            BufferedReader bufferReader = new BufferedReader(isr);  
		            String inputLine  = null;
		            String resultData = "";
		            while((inputLine = bufferReader.readLine()) != null){  
		                resultData += inputLine + "\n";  
		            } 
		            if(resultData != null){
		            	retCode = parseLocation(resultData, location);
		            	break;
		            }
				}
			} catch (IOException e) {
				retCode = ERR_NETWORK;
			}finally{
				if(connection != null){
					connection.disconnect();
				}
			}
		}
        return retCode;
	}
	
	
}

