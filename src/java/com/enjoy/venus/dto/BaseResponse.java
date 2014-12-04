package com.enjoy.venus.dto;

public class BaseResponse {
	static public final int ERROR_BAD_REQUEST = 400;
	static public final int ERROR_UNAUTHORIZED = 401;
	
	int statecode;
	String stateDescription;
	Object body;
	
	public BaseResponse(){
		this(0, "success", null);
	}
	
	public BaseResponse(Object body){
		this(0, "success", body);
	}
	
	public BaseResponse(int statecode, String stateDescription, Object body) {
		this.statecode = statecode;
		this.stateDescription = stateDescription;
		this.body = body;
	}

	public int getStatecode() {
		return statecode;
	}
	public void setStatecode(int statecode) {
		this.statecode = statecode;
	}
	public String getStateDescription() {
		return stateDescription;
	}
	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
