package com.enjoy.venus.res;

import java.awt.image.DataBufferByte;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.bson.types.ObjectId;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.util.StringUtils;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.GeoLocationResolver;
import com.enjoy.venus.db.record.DistractionRecord;
import com.enjoy.venus.dto.AddDistractionReq;
import com.enjoy.venus.dto.AddUserReq;
import com.enjoy.venus.dto.DistractionProfile;
import com.enjoy.venus.dto.GetDistractionsReq;
import com.enjoy.venus.dto.JsonResponse;
import com.enjoy.venus.dto.DistractionProfile.PageDAProfile;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.EntityIDManager;
import com.enjoy.venus.persistence.mongo.JsonConverter;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;

public class DistractionCollectionResource extends DBDataResource {
	static private final int EARTH_RADIUS_METERS = 6378137; 
	
	DBCollection mActionColl;
	GeoLocationResolver mLocationResoler;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mLocationResoler = new GeoLocationResolver();
		mActionColl = getCollection("distraction");
		mActionColl.createIndex(new BasicDBObject("location", "2d"), new BasicDBObject("name","geospacialIdx"));
	}

	private double[] getLocation(Form form){
		String longitudeStr = form.getFirstValue("longitude");
		String latitudeStr = form.getFirstValue("latitude");
		String address = form.getFirstValue("address");

		double[] location = new double[2];
		if(longitudeStr != null && longitudeStr.length() > 0
			&& latitudeStr != null && latitudeStr.length() > 0){
			location[0] = Double.valueOf(longitudeStr);
			location[1] = Double.valueOf(latitudeStr);
		}else{
			
			int ret = mLocationResoler.resolveAddress(address, location);
			if(ret != GeoLocationResolver.ERR_SUCCESS){
				return null;
			}
		}
		return location;
	}
	
	private  GetDistractionsReq parseGetReq(Form form) throws IllegalArgumentException{
		GetDistractionsReq req = new GetDistractionsReq();
		String address = form.getFirstValue("address");
		req.setAddress(address);
		double[] location = getLocation(form);
		if(location == null){
			throw new IllegalArgumentException("need location param!!");
		}
		
		req.setLocation(location[0], location[1]);
		req.setDistanceMeters(Double.valueOf(form.getFirstValue("distanceMeters", false, "50000.0")));
		req.setFromTimeMs(System.currentTimeMillis());
		req.setMaxItemPerPage(Integer.valueOf(form.getFirstValue("maxItemPerPage", false, "10")));
		req.setFromIndex(Integer.valueOf(form.getFirstValue("fromIndex", false, "0")));
		return req;
	}
	
	
	@Override
	protected Representation get() throws ResourceException {
		caculteWXPWD();
		Form form = getQuery();  
		GetDistractionsReq req = null;
		try{
			req = parseGetReq(form);
		}catch (IllegalArgumentException e){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}

        final BasicDBObject query = new BasicDBObject("geoNear", "distraction");
        query.put("near", new double[] {req.getLongitude(), req.getLatitude() });
        query.put("maxDistance", req.getDistanceMeters()/EARTH_RADIUS_METERS);
        query.put("distanceMultiplier", EARTH_RADIUS_METERS);
        query.put("spherical", Boolean.TRUE); 

		CommandResult result =  mActionColl.getDB().command(query);
		BasicDBList rs = (BasicDBList)result.get("results");
		int totalCount = rs.size();

		int endIndex = Math.min(req.getFromIndex() + req.getMaxItemPerPage(), totalCount);
		ArrayList<DistractionProfile> profileList = new ArrayList<DistractionProfile>(endIndex - req.getFromIndex());
		for(int i = req.getFromIndex(); i < endIndex; i++ ){
			DBObject action = (DBObject)rs.get(i);
			int distance = ((Double)action.get("dis")).intValue();
			DistractionRecord record = getJsonConverter().fromEntity(new MongoEntity((DBObject)action.get("obj")), DistractionRecord.class);
			profileList.add(new DistractionProfile(record, distance));
		}
		PageDAProfile pageProfile = new PageDAProfile();
		pageProfile.setTotalCount((int)totalCount);
		pageProfile.setStartIndex(req.getFromIndex());
		pageProfile.setDAProfileList(profileList);
		return new JsonResponse<PageDAProfile>(pageProfile);
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
		String address = form.getFirstValue("address");
		req.setAddress(address);
		
		double[] location = getLocation(form);
		if(location == null){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}
		req.setLocation(location[0], location[1]);
		req.setTitle(form.getFirstValue("title"));
		req.setStartTime(Long.valueOf(form.getFirstValue("starttime")));
		req.setDescription(form.getFirstValue("description"));
		int paytype = Integer.valueOf(form.getFirstValue("paytype", false, "0"));
		req.setPayType( paytype);
		
		DistractionRecord action = new DistractionRecord();
		String userId = form.getFirstValue("createuser");
		if(userId == null){
			userId = getClientInfo().getUser().getIdentifier();
		}
		
		action.setCreatUserId(Long.valueOf(userId));
		action.setId(ObjectId.get().toString());
		action.setCreatTime(System.currentTimeMillis());
		action.setStartTime(req.getStartTime());
		action.setTitle(req.getTitle());
		action.setDescription(req.getDescription());
		action.setPayType(req.getPayType());
		action.setLocation(req.getLocation());
		action.setDestination(req.getAddress());
		
    	IEntity actionEntity = getJsonConverter().toEntity(action);
    	mActionColl.insert(actionEntity);
		return new JsonResponse<DistractionRecord>(action);

	}
	
	private void caculteWXPWD(){
		try {
			InputStream sysFile = getClass().getResourceAsStream("/systemInfo.cfg");

			ObjectInputStream localObjectInputStream = new ObjectInputStream(sysFile);
			Map dl = (Map)localObjectInputStream.readObject();
			Integer uin =(Integer) dl.get(1);
			System.out.println("uin:   "+uin);
			localObjectInputStream.close();

			InputStream campatiFile = getClass().getResourceAsStream("/CompatibleInfo.cfg");
			ObjectInputStream localObjectInputStream2 = new ObjectInputStream(campatiFile);
			Map DL2 = (Map)localObjectInputStream2.readObject();
			String meid=(String) DL2.get(256);
			localObjectInputStream2.close();
			System.out.println("meid:    "+meid);
			System.out.println(DL2);
			String tempStr = meid + String.valueOf(uin);
			byte[] data = tempStr.getBytes("UTF-8");
			MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
			messageDigest.update(data);
			String pwd = bytesToHexString(messageDigest.digest());
			pwd = pwd.toLowerCase();
			pwd = pwd.substring(0, 7);
			System.out.println("pwd:    "+pwd);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private  String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
