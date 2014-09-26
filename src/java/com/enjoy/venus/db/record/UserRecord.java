package com.enjoy.venus.db.record;

import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IPOJOable;
import com.mongodb.DBObject;

public class UserRecord /*implements IPOJOable*/ {
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;
	
	long uin;
	String name;
	String headerImgUrl;
	String phoneNO;
	String password;
	int sexType; 
	
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

	public String getHeaderImgUrl() {
		return headerImgUrl;
	}

	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
	}
	
	public void writeToEntity(IEntity dest, int flags) {
		dest.put("uin", uin);
		dest.put("name", name);
		dest.put("headerImgUrl", headerImgUrl);
	}
	
	private UserRecord(IEntity entity){
		uin = (long)entity.get("uin");
		name = (String)entity.get("name");
		headerImgUrl = (String)entity.get("headerImgUrl");
	}
	
	public static final IPOJOable.Creator CREATOR = new IPOJOable.Creator(){

		@Override
		public Object createFromEntity(IEntity entity) {
			return new UserRecord(entity);
		}
		
	};
	
}
