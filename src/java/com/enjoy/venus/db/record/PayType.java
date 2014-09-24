package com.enjoy.venus.db.record;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public enum PayType{
	TYPE_ME,
	TYPE_AA,
	TYPE_YOU;
	
	static public class PayTypeSerializer implements JsonDeserializer<PayType>, JsonSerializer<PayType>{

		@Override
		public PayType deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context)
				throws JsonParseException {
			return PayType.values()[json.getAsInt()];
		}
		
		@Override
		public JsonElement serialize(PayType src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.ordinal());
		}	
		
	}
}
