package com.enjoy.venus.persistence;

import java.util.List;


public interface IPOJOConverter {
	public <T> T fromEntity(IEntity dest, Class<T> clazz);
	
	public IEntity toEntity(Object src);
	
}
