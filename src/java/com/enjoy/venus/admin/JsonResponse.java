package com.enjoy.venus.admin;

import org.restlet.ext.gson.GsonRepresentation;

import com.enjoy.venus.io.BaseResponse;

public class JsonResponse<T> extends GsonRepresentation<BaseResponse> {

	public JsonResponse(T body) {
		super(new BaseResponse(body));
	}

}
