package com.enjoy.venus.data;

import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IPOJOable;
import com.mongodb.DBObject;

public class UserRecord implements IPOJOable {
	long uin;
	String name;
	String headerImgPath;
	String phoneNum;
	String password;
	
	public UserRecord(long uin, String name) {
		this.uin = uin;
		this.name = name;
	}

	public long getUin() {
		return uin;
	}

	public String getName() {
		return name;
	}

	public String getHeaderImgPath() {
		return headerImgPath;
	}

	public void setHeaderImgPath(String headerImgPath) {
		this.headerImgPath = headerImgPath;
	}
	
	@Override
	public void writeToEntity(IEntity dest, int flags) {
		dest.put("uin", uin);
		dest.put("name", name);
		dest.put("headerImgPath", headerImgPath);
	}
	
	private UserRecord(IEntity entity){
		uin = (long)entity.get("uin");
		name = (String)entity.get("name");
		headerImgPath = (String)entity.get("headerImgPath");
	}
	
	public static final IPOJOable.Creator CREATOR = new IPOJOable.Creator(){

		@Override
		public Object createFromEntity(IEntity entity) {
			return new UserRecord(entity);
		}
		
	};
	
}
