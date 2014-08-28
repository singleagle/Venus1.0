package com.enjoy.venus.persistence;

import java.net.UnknownHostException;

import com.enjoy.venus.persistence.mongo.JsonConverter;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.enjoy.venus.persistence.mongo.MongoEntityProvider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class PersistenceEntityFactory {
	MongoClient mClient;
	
	public PersistenceEntityFactory(String host, int port) throws UnknownHostException{
		ServerAddress serverAddress= new ServerAddress(host,port);
		MongoClientOptions.Builder optionBuider = new MongoClientOptions.Builder();
		optionBuider.legacyDefaults();
		mClient = new MongoClient(serverAddress, optionBuider.build());
	}
	
	
	public IEntityProvider createEntityProvider(String dbName, String collectionName){
		return new MongoEntityProvider(mClient, dbName, collectionName);
	}
	
	public IEntity createEntity(){
		return new MongoEntity();
	}
	
	public IPOJOConverter createPOJOConvert(){
		return new JsonConverter();
	}
}
