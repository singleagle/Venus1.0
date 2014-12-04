package com.enjoy.venus.admin;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.RestApp;
import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.dto.JsonResponse;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

public class UserResource extends DBDataResource {
	DBCollection mUserColl;
	long uin;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mUserColl = getCollection("user");
		uin = Long.valueOf((String)getRequestAttributes().get(RestApp.USER_VARIABLE));
	}
	
	
	
	@Override
	protected Representation get() throws ResourceException {
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		DBObject dbEntity  = mUserColl.findOne(query);
		if(dbEntity == null){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}
		
		UserRecord newRecord = getJsonConverter().fromEntity(new MongoEntity(dbEntity), UserRecord.class);
		return new JsonResponse<UserRecord>(newRecord);
	}



	@Override
	protected Representation put(Representation entity)
			throws ResourceException {
		UserRecord record = null;
		BasicDBObject newUserInfo =  new BasicDBObject();
		Form form = new Form(entity);
		String name = form.getFirstValue("name");
		if(name != null){
			newUserInfo.put("name", name);
		}
		String phoneNO = form.getFirstValue("phoneNO");
		
		if(phoneNO != null){
			newUserInfo.put("phoneNO", phoneNO);
		}
		
		String pw =form.getFirstValue("password");
		if(pw != null){
			newUserInfo.put("password", pw);
		}
		
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		//只修改newUserInfo设置的字段
		DBObject dbEntity  = mUserColl.findAndModify(query, new BasicDBObject("$set", newUserInfo));
		UserRecord newRecord = getJsonConverter().fromEntity(new MongoEntity(dbEntity), UserRecord.class);
		
		return new JsonResponse<UserRecord>(newRecord);
	}
	
	@Override
	protected Representation delete() throws ResourceException {
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		DBObject entity = mUserColl.findAndRemove(query);
		if(entity == null){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}
		
		UserRecord delRecord = getJsonConverter().fromEntity(new MongoEntity(entity), UserRecord.class);
		return new JsonResponse<UserRecord>(delRecord);
	}
}
