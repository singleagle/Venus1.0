package com.enjoy.venus.dto;

import com.enjoy.venus.db.record.UserRecord;

public class AddDATypeReq {
	private int mainTypeId;
	private String subTypeName;
	private long createUIN;
	
	
	public AddDATypeReq() {

	}


	public int getMainTypeId() {
		return mainTypeId;
	}


	public void setMainTypeId(int mainTypeId) {
		this.mainTypeId = mainTypeId;
	}


	public String getSubTypeName() {
		return subTypeName;
	}


	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}


	public long getCreateUIN() {
		return createUIN;
	}


	public void setCreateUIN(long createUIN) {
		this.createUIN = createUIN;
	}
	
	
}
