package com.enjoy.venus.admin;

import java.io.IOException;

import org.restlet.ext.gson.GsonRepresentation;

import com.enjoy.venus.clientdata.BaseResponse;

public class JsonResponse<T> extends GsonRepresentation<BaseResponse> {
	static public final int ERROR_UNAUTHORIZED = 401;
	
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
