package com.enjoy.venus.clientdata;

import com.enjoy.venus.db.record.UserRecord;

public class LoginResult {
	long uin;
	String name;
	String headerImgUrl;
	String phoneNO;
	String password;
	String accessToken;
	
	public LoginResult(){
		
	}
	
	public LoginResult(UserRecord record, String accessToken){
		uin = record.getUin();
		name = record.getName();
		headerImgUrl = record.getHeaderImgUrl();
		phoneNO = record.getPhoneNO();
		password = record.getPassword();
		this.accessToken = accessToken;
	}
	
	public long getUin() {
		return uin;
	}
	public void setUin(long uin) {
		this.uin = uin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeaderImgUrl() {
		return headerImgUrl;
	}
	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
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
	
	
}
