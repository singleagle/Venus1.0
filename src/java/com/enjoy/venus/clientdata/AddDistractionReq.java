package com.enjoy.venus.clientdata;

import com.enjoy.venus.db.record.DistractionRecord;
import com.enjoy.venus.db.record.UserRecord;

public class AddDistractionReq extends DistractionRecord{
	String address;
	
	public AddDistractionReq() {
		super();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
