package com.enjoy.venus.persistence.mongo;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.enjoy.venus.persistence.IDBEntity;
import com.enjoy.venus.persistence.IEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoEntity implements IDBEntity{
	BasicDBObject mImpl;

	public MongoEntity(){
		mImpl = new BasicDBObject();
	}
	
	public MongoEntity(BSONObject dbObject){
		this();
		mImpl.putAll(dbObject);;
	}
	
	public boolean isPartialObject() {
		return mImpl.isPartialObject();
	}

	public BasicDBObject append(String key, Object val) {
		return mImpl.append(key, val);
	}

	public void clear() {
		mImpl.clear();
	}

	public Object clone() {
		return mImpl.clone();
	}

	public boolean containsField(String field) {
		return mImpl.containsField(field);
	}

	public boolean containsKey(Object arg0) {
		return mImpl.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return mImpl.containsValue(arg0);
	}

	public Object copy() {
		return mImpl.copy();
	}

	public Set<Entry<String, Object>> entrySet() {
		return mImpl.entrySet();
	}

	public int getInt(String key) {
		return mImpl.getInt(key);
	}

	public int getInt(String key, int def) {
		return mImpl.getInt(key, def);
	}

	public boolean equals(Object o) {
		return mImpl.equals(o);
	}

	public Object get(Object arg0) {
		return mImpl.get(arg0);
	}

	public void markAsPartialObject() {
		mImpl.markAsPartialObject();
	}

	public Map toMap() {
		return mImpl.toMap();
	}

	public String toString() {
		return mImpl.toString();
	}

	public Object removeField(String key) {
		return mImpl.removeField(key);
	}

	public Object get(String key) {
		return mImpl.get(key);
	}

	public long getLong(String key) {
		return mImpl.getLong(key);
	}

	public long getLong(String key, long def) {
		return mImpl.getLong(key, def);
	}

	public double getDouble(String key) {
		return mImpl.getDouble(key);
	}

	public double getDouble(String key, double def) {
		return mImpl.getDouble(key, def);
	}

	public String getString(String key) {
		return mImpl.getString(key);
	}

	public String getString(String key, String def) {
		return mImpl.getString(key, def);
	}

	public boolean getBoolean(String key) {
		return mImpl.getBoolean(key);
	}

	public boolean getBoolean(String key, boolean def) {
		return mImpl.getBoolean(key, def);
	}

	public ObjectId getObjectId(String field) {
		return mImpl.getObjectId(field);
	}

	public ObjectId getObjectId(String field, ObjectId def) {
		return mImpl.getObjectId(field, def);
	}

	public Date getDate(String field) {
		return mImpl.getDate(field);
	}

	public Date getDate(String field, Date def) {
		return mImpl.getDate(field, def);
	}

	public Object put(String key, Object val) {
		return mImpl.put(key, val);
	}

	public void putAll(Map m) {
		mImpl.putAll(m);
	}

	public void putAll(BSONObject o) {
		mImpl.putAll(o);
	}

	public int hashCode() {
		return mImpl.hashCode();
	}

	public boolean isEmpty() {
		return mImpl.isEmpty();
	}

	public Set<String> keySet() {
		return mImpl.keySet();
	}

	public Object remove(Object arg0) {
		return mImpl.remove(arg0);
	}

	public int size() {
		return mImpl.size();
	}

	public Collection<Object> values() {
		return mImpl.values();
	}

	@Override
	public void putAll(IEntity o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(String s) {
		return mImpl.containsKey(s);
	}
	
	
	
	
}
