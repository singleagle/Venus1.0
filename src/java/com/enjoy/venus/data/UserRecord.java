package com.enjoy.venus.data;

import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IPOJOable;
import com.mongodb.DBObject;

public class UserRecord /*implements IPOJOable*/ {
	long uin;
	String name;
	String headerImgPath;
	String phoneNO;
	String password;
	
	public UserRecord() {
		uin = -1;
		name = "unRegesterUser";
	}
	
	public String getPhoneNO() {
		return phoneNO;
	}


	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setUin(long uin) {
		this.uin = uin;
	}


	public void setName(String name) {
		this.name = name;
	}


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
