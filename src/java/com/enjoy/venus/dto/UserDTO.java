package com.enjoy.venus.dto;

import com.enjoy.venus.db.record.UserRecord;
import com.enjoy.venus.persistence.IEntity;
import com.enjoy.venus.persistence.IPOJOable;
import com.mongodb.DBObject;

public class UserDTO {
	static public final int SEX_UNKNOWN = 0;
	static public final int SEX_MALE = 1;
	static public final int SEX_WOMAN = 2;
	
	long uin;
	String name;
	String headerImgUrl;
	String phoneNO;
	int sexType; 
	
	public UserDTO(UserRecord record) {
		uin = record.getUin();
		name = record.getName();
		headerImgUrl = record.getHeaderImgUrl();
		phoneNO =  record.getPhoneNO();
		sexType = record.getSexType();
	}
	
	public String getPhoneNO() {
		return phoneNO;
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

	
}
