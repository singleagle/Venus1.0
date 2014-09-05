package com.enjoy.venus.admin;


import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpSession;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.security.SecretVerifier;

import com.enjoy.venus.persistence.PersistenceEntityFactory;
import com.enjoy.venus.persistence.mongo.MongoTokenManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class RestApp extends org.restlet.Application{
	
	public static final String ATTR_DBCLIEN = "dbclien";
	
	public static final String USERS = "/users";
	
	public static final String USER_VARIABLE = "userId";
	public static final String USERID_SEGMENT = "/{" + USER_VARIABLE +"}";
	public static final String DISTRACTIONS = "/distractions";
	public static final String DISTRACTION_VARIABLE = "actionId";
	public static final String DISTRACTION_SEGMENT = "/{" + DISTRACTION_VARIABLE + "}";
	
	Router mRouter;
	private TokenManager mTokenManager;
	private MongoClient mMongoClient;
	
	
	static Class<?> mClass = RestApp.class;
	
	public RestApp(){
		super();
		mRouter = new Router(getContext());
		ServerAddress serverAddress = null;
		try {
			serverAddress = new ServerAddress("127.0.0.1", 27017);
		} catch (UnknownHostException e) {
			throw new RuntimeException("UnknownHostException");
		}
		
		MongoClientOptions.Builder optionBuider = new MongoClientOptions.Builder();
		optionBuider.legacyDefaults();
		MongoCredential credential = MongoCredential.createMongoCRCredential("tangwh", "venus", "yl04688".toCharArray());
		ArrayList<MongoCredential> credentialList = new ArrayList<MongoCredential>(1);
		credentialList.add(credential);
		mMongoClient = new MongoClient(serverAddress, credentialList, optionBuider.build());
		mTokenManager = new MongoTokenManager(mMongoClient.getDB("venus"));
	}
	
	/**
	 * Bind URL paths to the appropriate ServerResource subclass. 
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		getContext().getAttributes().put(ATTR_DBCLIEN, mMongoClient);
	    getContext().getAttributes().put(TokenManager.class.getName(), mTokenManager);
		mRouter.attachDefault(UserCollectionResource.class);
		mRouter.attach(USERS, UserCollectionResource.class);
		mRouter.attach(USERS + USERID_SEGMENT, UserResource.class);
		mRouter.attach(DISTRACTIONS, DistractionCollectionResource.class);
		mRouter.attach(DISTRACTIONS + DISTRACTION_SEGMENT , DistractionResource.class);
		
        //创建认证器  
        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC,   
                "Venus Ream");  
          
        //配置用户认证
        TokenVerifier verifier = new TokenVerifier();
        authenticator.setVerifier(verifier);  
          
        //将路由器放在认证器之后  
        authenticator.setNext(mRouter); 
		return authenticator;
	}

	@Override
	public void stop() throws Exception {
		if (mRouter != null) {
			mRouter.stop();
		}
	}
	
	
}
