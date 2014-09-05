package com.enjoy.venus.io;

public class BaseResponse {
	int statecode;
	String stateDescription;
	Object body;
	
	public BaseResponse(){
		statecode = 0;
		stateDescription = "success";
	}
	
	public BaseResponse(Object body){
		this();
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
