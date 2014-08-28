package com.enjoy.venus.persistence.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class EntityIDMananger {
	private static final String MONGODB_TABLE_NAME_ID= "ID"; // uin表
	private static final String ID_COUNTER= "idCounter"; 
	
	DB mVenus;
	
	public EntityIDMananger(MongoClient client){
		mVenus = client.getDB("venus");
		if(!mVenus.collectionExists(MONGODB_TABLE_NAME_ID)){
			initUIN();
		}
	}
	
	private void initUIN(){
		try {
    		DBCollection topic = mVenus.getCollection(MONGODB_TABLE_NAME_ID);
    		BasicDBObject initCounter = new BasicDBObject();
    		initCounter.put("_id", ID_COUNTER);
    		initCounter.put("uin", 100L);
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
    		queryObj.put("_id", ID_COUNTER);

    		BasicDBObject update= new BasicDBObject("$inc", new BasicDBObject("uin", 1));
    		DBObject d=topic.findAndModify(queryObj,update);
    		uin=(long)d.get("uin");

    	}catch (MongoException e) {
    		e.printStackTrace();
    	} 

    	return uin;
    }
}
