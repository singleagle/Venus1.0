package com.enjoy.venus.admin;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.util.Base64;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.dto.AddUserReq;
import com.enjoy.venus.dto.BaseResponse;
import com.enjoy.venus.dto.JsonResponse;
import com.enjoy.venus.dto.UserDTO;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.EntityIDManager;
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
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class UserCollectionResource extends DBDataResource {
	DBCollection mUserColl;
    
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mUserColl = getCollection("user");
		mUserColl.createIndex(new BasicDBObject("uin", 1), new BasicDBObject("unique", true));
	}

	@Override
	protected Representation get() throws ResourceException {

		DBCursor cursor = mUserColl.find();
		DBObject user = null;
		UserRecord record = null;
		
		ArrayList<UserDTO> userList = new ArrayList<UserDTO>(cursor.count());
		while(cursor.hasNext()){
			user = cursor.next();
			record = getJsonConverter().fromEntity(new MongoEntity(user), UserRecord.class);
			userList.add(new UserDTO(record));
		}
		return new JsonResponse<ArrayList<UserDTO>>(userList);
		
	}


	@Override
	protected Representation delete() throws ResourceException {
		if(!isInRole("admin")){
			return null;
		}
		DBCursor cursor = mUserColl.find();
		DBObject user = null;
		while(cursor.hasNext()){
			user = cursor.next();
			mUserColl.remove(user);
		}
		return new StringRepresentation("delete all users!!");
	}

	
    private IEntity creatUserRecord(UserRecord userRecord, String password){
    	userRecord.setPassword(password);
    	IEntity entity = getJsonConverter().toEntity(userRecord);
    	mUserColl.insert(entity);
		return entity;
    }
    
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		
		EntityIDManager idManager = getEntityIDManager();
		long uin = idManager.generateUIN();
		AddUserReq req = null;
		if(MediaType.APPLICATION_JSON.equals(entity.getMediaType())){
			GsonRepresentation<AddUserReq> gsonRep = new GsonRepresentation<AddUserReq>(entity, AddUserReq.class);
			
			try {
				req = gsonRep.getObject();
			} catch (IOException e) {
				doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
				return null;
			}
			
		}else{
			Form form = new Form(entity);
			req = new AddUserReq();
			req.setName(form.getFirstValue("name"));
			req.setPhoneNO(form.getFirstValue("phoneNO"));
			req.setPassword(form.getFirstValue("password"));
		}
		
		UserRecord userRecord = new UserRecord(uin, req.getName());
		IEntity record = creatUserRecord(userRecord, req.getPassword());
		
		return  new JsonResponse<UserDTO>(new UserDTO(userRecord));
	}
	
    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            Base64 encoder = new Base64();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encode(hashedBytes, false) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }
	
}
