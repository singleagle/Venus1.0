package com.enjoy.venus.res;

import java.util.ArrayList;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.enjoy.venus.admin.DBDataResource;
import com.enjoy.venus.admin.GeoLocationResolver;
import com.enjoy.venus.db.record.DATypeRecord;
import com.enjoy.venus.db.record.DistractionRecord;
import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.dto.AddDATypeReq;
import com.enjoy.venus.dto.DATypeDTO;
import com.enjoy.venus.dto.DATypeDTO.DATypeDTOList;
import com.enjoy.venus.dto.JsonResponse;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.mongo.EntityIDManager;
import com.enjoy.venus.persistence.mongo.MongoEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DATypeRes extends DBDataResource {
	DBCollection mTypeColl;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		mTypeColl = getCollection("datype");
		mTypeColl.createIndex(new BasicDBObject("typeId", 1), new BasicDBObject("unique", true));
		getLogger().info("datype collection had create!");
	}

	@Override
	protected Representation get() throws ResourceException {
		DBCursor cursor = mTypeColl.find();
		DBObject daObj = null;
		DATypeRecord record = null;
		
		ArrayList<DATypeDTO> dtoList = new ArrayList<DATypeDTO>();
		String mainTypeName = "undefine";
		while(cursor.hasNext()){
			daObj = cursor.next();
			record = getJsonConverter().fromEntity(new MongoEntity(daObj), DATypeRecord.class);
			if(record.getSubTypeId() == 0){
				mainTypeName = record.getTypeName();
			}else{
				dtoList.add(new DATypeDTO(record, mainTypeName));
			}
			
		}
		DATypeDTOList list = new DATypeDTOList(dtoList, 1);
		return new JsonResponse<DATypeDTOList>(list);
	}
	
	@Override
	protected Representation delete() throws ResourceException {
		return null;
	}
	
	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		Form  form = new Form(entity);
		
		AddDATypeReq req = new AddDATypeReq();
		req.setMainTypeId(Integer.valueOf(form.getFirstValue("maintypeid", "0")));
		req.setCreateUIN(Long.valueOf(form.getFirstValue("createuin")));
		req.setSubTypeName(form.getFirstValue("subtypename"));
		if(req.getCreateUIN() == 0 || req.getSubTypeName() == null){
			doError(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			return null;
		}
		DATypeRecord record = new DATypeRecord();
		int subTypeId = getEntityIDManager().generateDASubtype(req.getMainTypeId());
		record.setTypeId(req.getMainTypeId(),subTypeId);
		record.setTypeName(req.getSubTypeName());
		record.setCreateUIN(req.getCreateUIN());
		if(isInRole("admin")){
			record.setScope(DATypeRecord.SCOPE_PUBLIC);
		}else{
			record.setScope(DATypeRecord.SCOPE_PRIVATE);
		}
		
		
    	IEntity actionEntity = getJsonConverter().toEntity(record);
    	mTypeColl.insert(actionEntity);
		return new JsonResponse<DATypeRecord>(record);
	}
	
}
