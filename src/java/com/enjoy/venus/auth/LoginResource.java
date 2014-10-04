package com.enjoy.venus.auth;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.internal.Client;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.RestApp;
import com.enjoy.venus.clientdata.BaseResponse;
import com.enjoy.venus.clientdata.JsonResponse;
import com.enjoy.venus.clientdata.LoginResult;
import com.enjoy.venus.clientdata.RegisterAccountReq;
import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class LoginResource extends DBDataResource {
	DBCollection mUserColl;
	TokenManager mTokens;
	Client mVenusClient;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mUserColl = getCollection("user");
		ConcurrentMap<String, Object> attrs = getApplication().getContext().getAttributes();
		mTokens = (TokenManager) attrs.get(TokenManager.class.getName());
		mVenusClient = (Client) attrs.get(Client.class.getName());
	}
	
	
	@Override
	protected Representation delete() throws ResourceException {

		Form form = getQuery();
		int uin = Integer.valueOf(form.getFirstValue("uin"));
		
		mTokens.revokeToken(mVenusClient, String.valueOf(uin));
		JSONObject result = new JSONObject();
		try {
			result.put("uin", uin);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JsonResponse<String>(result.toString());
	}
	
	
	
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		ConcurrentMap<String, Object> attrs = getApplication().getContext().getAttributes();
		TokenManager tokens = (TokenManager) attrs.get(TokenManager.class.getName());
		Client client = (Client) attrs.get(Client.class.getName());
		
		Form form = new Form(entity);
		String uid = form.getFirstValue("uid");
		String password = form.getFirstValue("password");
		if(uid == null || password == null){
			doError(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}
		
		BasicDBObject query = new BasicDBObject();
		
		if(uid.length() == 11){
			query.put("phoneNO", uid);
		}else{
			query.put("uin", Long.valueOf(uid));
		}
		
		DBObject dbObj = mUserColl.findOne(query);
		if(dbObj == null){
			return new JsonResponse<String>(BaseResponse.ERROR_UNAUTHORIZED, "uid not exist!!", null);
		}
		UserRecord record = mConverter.fromEntity(new MongoEntity(dbObj), UserRecord.class);
		if(!password.equals(record.getPassword())){
			return new JsonResponse<String>(BaseResponse.ERROR_UNAUTHORIZED, "password error!!", null);
		}
		Token token;
		try {
			token = tokens.generateToken(client, new String[]{"common"});
			return new JsonResponse<LoginResult>(new LoginResult(record, token.getAccessToken()));
		} catch (OAuthException e) {
			e.printStackTrace();
		}
		
		return new JsonResponse<String>(BaseResponse.ERROR_UNAUTHORIZED, "generate token error!", null);
		
	}
}
