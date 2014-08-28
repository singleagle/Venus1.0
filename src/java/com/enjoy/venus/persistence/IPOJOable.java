package com.enjoy.venus.persistence;

import java.util.List;


public interface IPOJOable {
    /**
     * Flatten this object in to a Entity.
     * 
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
	public void writeToEntity(IEntity dest, int flags);
	
	public interface Creator<T> {
		public T createFromEntity(IEntity entity);
	}
	
	public interface ClassLoaderCreator<T> extends Creator<T> {
		public T createFromEntity(IEntity entity, ClassLoader loader);
	}
	
}
