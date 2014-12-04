package com.enjoy.venus.admin;


import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.LogManager;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpSession;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Reference;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.AuthPageServerResource;
import org.restlet.ext.oauth.AuthorizationServerResource;
import org.restlet.ext.oauth.ClientVerifier;
import org.restlet.ext.oauth.HttpOAuthHelper;
import org.restlet.ext.oauth.TokenAuthServerResource;
import org.restlet.ext.oauth.TokenVerifier;
import org.restlet.ext.oauth.internal.Client;
import org.restlet.ext.oauth.internal.Client.ClientType;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.ext.oauth.internal.memory.MemoryClientManager;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.security.SecretVerifier;

import com.enjoy.venus.auth.LoginResource;
import com.enjoy.venus.auth.RegisterResource;
import com.enjoy.venus.persistence.PersistenceEntityFactory;
import com.enjoy.venus.persistence.mongo.MongoClientManager;
import com.enjoy.venus.persistence.mongo.MongoTokenManager;
import com.enjoy.venus.res.DATypeRes;
import com.enjoy.venus.res.DistractionCollectionResource;
import com.enjoy.venus.res.DistractionResource;
import com.mongodb.DB;
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
	public static final String DISTRACTION_TYPE = "/datypes";
	
	public static final String DB_USER_NAME = "4c5OS5cGGjmPr4ERO1gcByk8";
	public static final String DB_USER_PWD = "LzvELOKlwM66OGZpdApUYtrTA7FopDgj";
	public static final String DB_VENUS_NAME = "wlkqnuQHpoaIYnLikclm";
	
	Router mRouter;
	private TokenManager mTokenManager;
	private MongoClient mMongoClient;
	private MemoryClientManager mAuthClientManager;
	private Client  mVenusClient;
	
	static Class<?> mClass = RestApp.class;
	
	public RestApp(){
		super();
		//System.setProperty("java.util.logging.config.file", "logging.properties");
		try {
			InputStream conf = getClass().getResourceAsStream("logging.properties");
			LogManager manager = LogManager.getLogManager();
			LogManager.getLogManager().readConfiguration(conf);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		mRouter = new Router(getContext());
		ServerAddress serverAddress = null;
		try {
			serverAddress = new ServerAddress("127.0.0.1");
		} catch (UnknownHostException e) {
			throw new RuntimeException("UnknownHostException");
		}
		
		MongoClientOptions.Builder optionBuider = new MongoClientOptions.Builder();
		optionBuider.cursorFinalizerEnabled(false);
		MongoCredential credential = MongoCredential.createMongoCRCredential(DB_USER_NAME, DB_VENUS_NAME, DB_USER_PWD.toCharArray());
		ArrayList<MongoCredential> credentialList = new ArrayList<MongoCredential>(1);
		credentialList.add(credential);
		mMongoClient = new MongoClient(serverAddress, credentialList, optionBuider.build());
	    DB venus = mMongoClient.getDB(DB_VENUS_NAME);
	    venus.authenticate(DB_USER_NAME, DB_USER_PWD.toCharArray());
		mTokenManager = new MongoTokenManager(venus);
		mAuthClientManager = new MemoryClientManager();
		
		mVenusClient = mAuthClientManager.createClient(ClientType.CONFIDENTIAL, null, null);
	}
	
	/**
	 * Bind URL paths to the appropriate ServerResource subclass. 
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		ConcurrentMap<String, Object> attribs = getContext().getAttributes();
		attribs.put(ATTR_DBCLIEN, mMongoClient);
		attribs.put(TokenManager.class.getName(), mTokenManager);
	    attribs.put(Client.class.getName(), mVenusClient);
	    
	    
        mRouter.attach("/register", RegisterResource.class);
        mRouter.attach("/login", LoginResource.class);
        
        mRouter.attach("/oauth/authorize", AuthorizationServerResource.class);
        mRouter.attach("/oauth/auth_page", AuthPageServerResource.class);
        //HttpOAuthHelper.setLoginPage("login.html", getContext);
        
        // Setup Token Endpoint
        ChallengeAuthenticator clientAuthenticator = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "VenusOAuth");
        ClientVerifier clientVerifier = new ClientVerifier(getContext());
        clientVerifier.setAcceptBodyMethod(true);
        clientAuthenticator.setVerifier(clientVerifier);
        clientAuthenticator.setNext(AccessTokenServerResource.class);
        mRouter.attach("/oauth/token", clientAuthenticator);
        // Setup Token Auth for Resources Server
        mRouter.attach("/oauth/token_auth", TokenAuthServerResource.class);
        
	    Router secRouter = new Router(getContext());
	    secRouter.attachDefault(UserCollectionResource.class);
	    secRouter.attach(USERS, UserCollectionResource.class);
	    secRouter.attach(USERS + USERID_SEGMENT, UserResource.class);
	    secRouter.attach(DISTRACTIONS, DistractionCollectionResource.class);
	    secRouter.attach(DISTRACTIONS + DISTRACTION_SEGMENT , DistractionResource.class);
	    secRouter.attach(DISTRACTION_TYPE, DATypeRes.class);
        //创建认证器  
        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC,   
                "Venus Realm");  
          
        //配置用户认证
        TokenVerifier verifier = new TokenVerifier(new Reference("riap://application/oauth/token_auth"));
        authenticator.setVerifier(verifier);  
        //将路由器放在认证器之后  
        authenticator.setNext(secRouter); 
        mRouter.attach("/sec", secRouter);
        
		return mRouter;
	}

	@Override
	public void stop() throws Exception {
		if (mRouter != null) {
			mRouter.stop();
		}
	}
	
	
}
