package com.enjoy.venus.admin;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.data.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.JsonConverter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DBDataResource extends ServerResource {
	
	DB mMongoDB;
	JsonConverter mConverter;
	
	@Override
	protected void doInit() throws ResourceException {
    	super.doInit();
		MongoClient mgClient = (MongoClient) getApplication().getContext().getAttributes().get(RestApp.ATTR_DBCLIEN);
		mMongoDB = mgClient.getDB("venus");
    	mConverter = new JsonConverter();
	}

	public DBCollection getCollection(String name) {
		return mMongoDB.getCollection(name);
	}

	public JsonConverter getJsonConverter() {
		return mConverter;
	}

	
}
