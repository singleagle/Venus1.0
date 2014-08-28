package com.enjoy.venus.admin;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.data.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.EntityIDMananger;
import com.enjoy.venus.persistence.mongo.JsonConverter;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class UserCollectionResource extends ServerResource {
	DBCollection mUserColl;
	JsonConverter mConverter = new JsonConverter();
	
	@Override
	protected void doInit() throws ResourceException {
    	MongoClient mgClient = (MongoClient) getApplication().getContext().getAttributes().get(RestApp.ATTR_DBCLIEN);
    	mUserColl = mgClient.getDB("venus").getCollection("user");
    	super.doInit();
	}

	
    private IEntity creatUserRecord(UserRecord userRecord, String password){
    	IEntity entity = mConverter.toEntity(userRecord);
    	mUserColl.insert(entity);
		return entity;
    }
    
	@Override
	protected Representation get() throws ResourceException {

		DBCursor cursor = mUserColl.find();
		DBObject user = null;
		UserRecord record = null;
		
		ArrayList<UserRecord> userList = new ArrayList<UserRecord>(cursor.count());
		while(cursor.hasNext()){
			user = cursor.next();
			record = mConverter.fromEntity(new MongoEntity(user), UserRecord.class);
			userList.add(record);
		}
		return new GsonRepresentation<ArrayList<UserRecord>>(userList);
		
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

		IEntity newUserInfo = mConverter.toEntity(record);
		long uin = record.getUin();
		BasicDBObject query = new BasicDBObject();
		query.append("uin", uin);
		mUserColl.update(query, newUserInfo);
		
		return new GsonRepresentation<UserRecord>(record);
	}

	@Override
	protected Representation delete() throws ResourceException {

		DBCursor cursor = mUserColl.find();
		DBObject user = null;
		while(cursor.hasNext()){
			user = cursor.next();
			mUserColl.remove(user);
		}
		return new StringRepresentation("delete all users!!");
	}

	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		MongoClient mgClient = (MongoClient) getApplication().getContext().getAttributes().get(RestApp.ATTR_DBCLIEN);
		
		EntityIDMananger idManager = new EntityIDMananger(mgClient);
		long uin = idManager.generateUIN();

		UserRecord userRecord = new UserRecord(uin, form.getFirstValue("name"));
		IEntity record = creatUserRecord(userRecord, form.getFirstValue("password"));
		return  new GsonRepresentation<UserRecord>(userRecord);
	}
	
	
	
}
