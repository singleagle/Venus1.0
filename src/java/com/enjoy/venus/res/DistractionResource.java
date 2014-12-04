package com.enjoy.venus.res;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.RestApp;
import com.enjoy.venus.db.record.DistractionRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DistractionResource extends DBDataResource {
	DBCollection mActionColl;
	long uin;
	String actionId;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mActionColl = getCollection("distraction");
		actionId = (String)getRequestAttributes().get(RestApp.DISTRACTION_VARIABLE);
		uin = Long.valueOf(getClientInfo().getUser().getIdentifier());
	}
	
	
	@Override
	protected Representation put(Representation entity)
			throws ResourceException {
		DistractionRecord record = null;
		try {
			GsonRepresentation<DistractionRecord> gsonRep = new GsonRepresentation<DistractionRecord>(entity, DistractionRecord.class);
			record = gsonRep.getObject();
		} catch (IOException e) {
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}

		record.setCreatUserId(uin);
		record.setId(actionId);
		IEntity newUserInfo = getJsonConverter().toEntity(record);
		BasicDBObject query = new BasicDBObject();
		query.append("creatUserId", uin);
		query.append("_id", actionId);
		DBObject dbEntity  = mActionColl.findAndModify(query, newUserInfo);
		DistractionRecord newRecord = getJsonConverter().fromEntity(new MongoEntity(dbEntity), DistractionRecord.class);;
		
		return new GsonRepresentation<DistractionRecord>(newRecord);
	}

}
