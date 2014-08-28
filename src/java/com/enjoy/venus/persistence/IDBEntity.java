package com.enjoy.venus.persistence;

public interface IDBEntity extends IEntity{
    /**
     * if this object was retrieved with only some fields (using a field filter)
     * this method will be called to mark it as such.
     */
    public void markAsPartialObject();

    /**
     * whether markAsPartialObject was ever called
     * only matters if you are going to upsert and do not want to risk losing fields
     */
    public boolean isPartialObject();
}
