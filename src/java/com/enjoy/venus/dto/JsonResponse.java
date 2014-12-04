package com.enjoy.venus.dto;

import java.io.IOException;

import org.restlet.ext.gson.GsonRepresentation;

public class JsonResponse<T> extends GsonRepresentation<BaseResponse> {
	
	public JsonResponse(T body) {
		super(new BaseResponse(body));
	}
	
	public JsonResponse(int stateCode, String description, T body) {
		super(new BaseResponse(stateCode, description, body));
	}

	@Override
	public BaseResponse getObject() throws IOException {
		return super.getObject();
	}
	
	

}
