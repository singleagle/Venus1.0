package com.enjoy.venus.persistence.mongo;

import java.lang.reflect.Field;

import com.enjoy.venus.data.PayType;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IPOJOConverter;
import com.enjoy.venus.persistence.IPOJOable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class JsonConverter implements IPOJOConverter{
	Gson mGson;
	
	public JsonConverter(){
		GsonBuilder gsonBuilder = new GsonBuilder();  
	    gsonBuilder.registerTypeAdapter(PayType.class,  
	                new PayType.PayTypeSerializer());  
	    mGson = gsonBuilder.create();
	}
	
	@Override
	public <T> T fromEntity(IEntity dest, Class<T> clazz) {
		if(IPOJOable.class.isAssignableFrom(clazz)){
			IPOJOable.Creator<T> creator = null;
            
            try {
            	Field f = clazz.getField("CREATOR");
				creator = (IPOJOable.Creator)f.get(null);
			} catch (IllegalArgumentException e) {
				throw new ClassCastException("IllegalArgumentException");
			} catch (IllegalAccessException e) {
				throw new ClassCastException("IllegalAccessException");
			} catch (NoSuchFieldException e) {
				throw new ClassCastException("NoSuchFieldException");
			} catch (SecurityException e) {
				throw new ClassCastException("SecurityException");
			}
            return creator.createFromEntity(dest);
		}
		String content = dest.toString();
		return mGson.fromJson(content, clazz);
	}

	@Override
	public IEntity toEntity(Object src) {
		if(src instanceof IPOJOable){
			IPOJOable pojo = (IPOJOable) src;
			MongoEntity entity = new MongoEntity();
			pojo.writeToEntity(entity, 0);
			return entity;
		}
		String content = mGson.toJson(src);
		
		DBObject entity = (DBObject)JSON.parse(content);
		return new MongoEntity(entity);
	}

}
