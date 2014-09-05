package com.enjoy.venus.admin;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.data.Distraction;
import com.enjoy.venus.data.Distraction;
import com.enjoy.venus.io.AddDistractionReq;
import com.enjoy.venus.io.AddUserReq;
import com.enjoy.venus.io.GetDistractionsReq;
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
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class DistractionCollectionResource extends DBDataResource {

	DBCollection mActionColl;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mActionColl = getCollection("distraction");
		mActionColl.createIndex(new BasicDBObject("location", "2d"), new BasicDBObject("name","geospacialIdx"));
	}

	@Override
	protected Representation get() throws ResourceException {
		
		Form form = getQuery();  
		GetDistractionsReq req = new GetDistractionsReq();
		
		req.setLocation(Double.valueOf(form.getFirstValue("longitude")), Double.valueOf(form.getFirstValue("latitude")));
		req.setDistanceMile(Double.valueOf(form.getFirstValue("distancemile")));
		req.setFromTimeMs(System.currentTimeMillis());
		req.setMaxItemPerPage(20);
		
		
        final BasicDBObject filter = new BasicDBObject("$near", new double[] {req.getLongitude(), req.getLatitude() });
        filter.put("$maxDistance", req.getDistanceMile());

        final BasicDBObject query = new BasicDBObject("location", filter);
		
		DBCursor cursor = mActionColl.find(query).skip(req.getFromIndex()).limit(req.getMaxItemPerPage());
		DBObject action = null;
		Distraction record = null;
		
		ArrayList<Distraction> actionList = new ArrayList<Distraction>(cursor.count());
		while(cursor.hasNext()){
			action = cursor.next();
			record = mConverter.fromEntity(new MongoEntity(action), Distraction.class);
			actionList.add(record);
		}
		return new GsonRepresentation<ArrayList<Distraction>>(actionList);
		
	}


	@Override
	protected Representation delete() throws ResourceException {

		DBCursor cursor = mActionColl.find();
		DBObject action = null;
		while(cursor.hasNext()){
			action = cursor.next();
			mActionColl.remove(action);
		}
		return new StringRepresentation("delete all distraction!!");
	}
	
    
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {

		Form form = new Form(entity);
		AddDistractionReq req = new AddDistractionReq();
		
		req.setTitle(form.getFirstValue("title"));
		req.setDescription(form.getFirstValue("description"));
		int paytype = Integer.valueOf(form.getFirstValue("paytype"));
		req.setPayType( paytype);
		req.setLocation(Double.valueOf(form.getFirstValue("longitude")), Double.valueOf(form.getFirstValue("latitude")));
		
		
		Distraction action = new Distraction();
		action.setId(UUID.randomUUID().toString());
		action.setCreatTime(System.currentTimeMillis());
		action.setTitle(req.getTitle());
		action.setDescription(req.getDescription());
		action.setPayType(req.getPayType());
		action.setLocation(req.getLocation());
		
    	IEntity actionEntity = mConverter.toEntity(action);
    	mActionColl.insert(actionEntity);
    	
		return new JsonResponse<Distraction>(action);

	}
	
	
	
}
