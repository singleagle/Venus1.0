package com.enjoy.venus.persistence.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class EntityIDManager {
	private static final String MONGODB_TABLE_NAME_ID= "ID"; // uin表
	private static final String UIN_ID= "uin"; 
	private static final String DATYPE_ID= "datype"; 
	
	private DB mVenus;
	
	public EntityIDManager(DB db){
		mVenus = db;
		if(!mVenus.collectionExists(MONGODB_TABLE_NAME_ID)){
			initUIN();
		}
	}

	
	private void initUIN(){
		try {
    		DBCollection topic = mVenus.getCollection(MONGODB_TABLE_NAME_ID);
    		BasicDBObject initCounter = new BasicDBObject();
    		initCounter.put("_id", UIN_ID);
    		initCounter.put("seq", 100L);
    		topic.insert(initCounter);
		} catch (MongoException e) {
 		   e.printStackTrace();
		}
	}
	
	
    public  long generateUIN() {
    	long uin = 0;
    	try {
    		DBCollection topic = mVenus.getCollection(MONGODB_TABLE_NAME_ID);
    		BasicDBObject queryObj = new BasicDBObject();
    		queryObj.put("_id", UIN_ID);

    		BasicDBObject update= new BasicDBObject("$inc", new BasicDBObject("seq", 1));
    		DBObject d=topic.findAndModify(queryObj,null, null, false, update, true, false);
    		uin=(long)d.get("seq");

    	}catch (MongoException e) {
    		e.printStackTrace();
    	} 

    	return uin;
    }
    
    public int generateDASubtype(int DAMaintype){
    	int subType = 0;
    	try {
    		DBCollection topic = mVenus.getCollection(MONGODB_TABLE_NAME_ID);
    		BasicDBObject queryObj = new BasicDBObject();
    		queryObj.put("_id", DATYPE_ID);
    		queryObj.put("main", DAMaintype);

    		BasicDBObject update= new BasicDBObject("$inc", new BasicDBObject("sub", 1));
    		DBObject d=topic.findAndModify(queryObj,null, null, false, update, true, true);
    		subType=(int)d.get("sub");

    	}catch (MongoException e) {
    		e.printStackTrace();
    	} 

    	return subType;
    }
}
