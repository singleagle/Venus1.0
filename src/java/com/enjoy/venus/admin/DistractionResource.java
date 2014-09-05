package com.enjoy.venus.admin;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.data.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class DistractionResource extends DBDataResource {
	DBCollection mUserColl;
	long uin;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mUserColl = getCollection("user");
		uin = Long.valueOf((String)getRequestAttributes().get(RestApp.USER_VARIABLE));
	}
	
	
	@Override
	protected Representation put(Representation entity)
			throws ResourceException {
		UserRecord record = null;
		try {
			GsonRepresentation<UserRecord> gsonRep = new GsonRepresentation<UserRecord>(entity, UserRecord.class);
			record = gsonRep.getObject();
		} catch (IOException e) {
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}

		record.setUin(uin);
		IEntity newUserInfo = getJsonConverter().toEntity(record);
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		DBObject dbEntity  = mUserColl.findAndModify(query, newUserInfo);
		UserRecord newRecord = getJsonConverter().fromEntity(new MongoEntity(dbEntity), UserRecord.class);;
		
		return new GsonRepresentation<UserRecord>(newRecord);
	}
	
	@Override
	protected Representation delete() throws ResourceException {
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		DBObject entity = mUserColl.findAndRemove(query);
		if(entity == null){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
		}
		UserRecord delRecord = getJsonConverter().fromEntity(new MongoEntity(entity), UserRecord.class);
		return new GsonRepresentation<UserRecord>(delRecord);
	}
}
