package com.enjoy.venus.admin;


import org.restlet.security.SecretVerifier;

public class TokenVerifier extends SecretVerifier{
	      
    @Override  
    public int verify(String identifier, char[] secret) {  
        System.out.printf("username:%s password:%s%n" , identifier , new String(secret) );  
        /** 
         * 此处自定义的验证规则为：如果用户名不为空，并且用户名和密码相等则通过。否则不通过 
         */  
        if(identifier == null || identifier.equals("")){  
            return SecretVerifier.RESULT_INVALID ;  
        }else if(identifier.equals( new String(secret) )){  
            return SecretVerifier.RESULT_VALID ;  
        }else{  
            return SecretVerifier.RESULT_INVALID ;  
        }  
    }    
	  
}
