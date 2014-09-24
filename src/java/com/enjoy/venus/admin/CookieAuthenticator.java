package com.enjoy.venus.admin;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Cookie;
import org.restlet.data.Method;
import org.restlet.security.ChallengeAuthenticator;


public class CookieAuthenticator extends ChallengeAuthenticator {

	public CookieAuthenticator(Context context, boolean optional,
			ChallengeScheme challengeScheme, String realm) {
		super(context, optional, challengeScheme, realm);
	}
	
	@Override
    protected int beforeHandle(Request request, Response response) {
        Cookie cookie = request.getCookies().getFirst("Credentials");

        if (cookie != null) {
            // Extract the challenge response from the cookie
            String[] credentials = cookie.getValue().split("=");

            if (credentials.length == 2) {
                String identifier = credentials[0];
                String secret = credentials[1];
                request.setChallengeResponse(new ChallengeResponse(
                        ChallengeScheme.HTTP_COOKIE, identifier, secret));
            }
        } else if (Method.POST.equals(request.getMethod())
                && request.getResourceRef().getQueryAsForm().getFirst("login") != null) {
        	
        }
        
        return super.beforeHandle(request, response);
	}
	
   @Override
    public void challenge(Response response, boolean stale) {
	   JsonResponse<String> invalidatePres = new JsonResponse<String>(JsonResponse.ERROR_UNAUTHORIZED, "invaliade user token", "invaliade user token"); 
	   response.setEntity(invalidatePres);
   }

}
