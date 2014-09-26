package com.enjoy.venus.auth;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.RestApp;
import com.enjoy.venus.clientdata.AddUserReq;
import com.enjoy.venus.clientdata.BaseResponse;
import com.enjoy.venus.clientdata.JsonResponse;
import com.enjoy.venus.clientdata.RegisterAccountReq;
import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.EntityIDMananger;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

public class RegisterResource extends DBDataResource {
	DBCollection mUserColl;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mUserColl = getCollection("user");
		mUserColl.createIndex(new BasicDBObject("uin", "1"), new BasicDBObject("unique", true));
	}
	
	
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		
		MongoClient mgClient = (MongoClient) getApplication().getContext().getAttributes().get(RestApp.ATTR_DBCLIEN);
		Form form = new Form(entity);
		RegisterAccountReq req = new RegisterAccountReq();
		req.setNickName(form.getFirstValue("nickName"));
		req.setPhoneNO(form.getFirstValue("phoneNO"));
		req.setPassword(form.getFirstValue("password"));
		
		if(req.getNickName() == null || req.getPhoneNO() == null 
				|| req.getPassword() == null || req.getPassword().length() < 6){
			doError(Status.CLIENT_ERROR_BAD_REQUEST);
			return null;
		}
		
		BasicDBObject query = new BasicDBObject();
		query.put("phoneNO", req.getPhoneNO());
		if(mUserColl.count(query) > 0){
			return new JsonResponse<String>(BaseResponse.ERROR_BAD_REQUEST, "phoneNo had registed!", null);
		}
		
		EntityIDMananger idManager = new EntityIDMananger(mgClient);
		long uin = idManager.generateUIN();
		UserRecord userRecord = new UserRecord(uin, req.getNickName());
		userRecord.setPhoneNO(req.getPhoneNO());
		userRecord.setPassword(req.getPassword());
    	IEntity record = mConverter.toEntity(userRecord);
    	mUserColl.insert(record);
    	userRecord.setPassword(null);
    	return  new JsonResponse<UserRecord>(userRecord);
	}
}

