package com.enjoy.venus.persistence.mongo;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IEntityProvider;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoEntityProvider implements IEntityProvider{
	MongoClient mDBImpl;
	DBCollection mCollection;
	
	public MongoEntityProvider(MongoClient mongo, String dbName,  String collectionName)
	{
		mDBImpl = mongo;
		mCollection = mDBImpl.getDB(dbName).getCollection(collectionName);
	}

	@Override
	public IEntity get(long id) {
		return null;
	}

	@Override
	public List<IEntity> get(int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IEntity> get(String search, int start, int count, String order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(String search) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IEntity update(IEntity entity, long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(IEntity entity, long userId) {
		// TODO Auto-generated method stub
		
	}

}
