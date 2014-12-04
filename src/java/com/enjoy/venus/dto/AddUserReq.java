package com.enjoy.venus.dto;

import com.enjoy.venus.db.record.UserRecord;

public class AddUserReq extends UserRecord{
	
	public AddUserReq() {
		super();
		setUin(-1);
	}
}
