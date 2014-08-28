package com.enjoy.venus.persistence;

import java.util.Map;
import java.util.Set;

import com.mongodb.DBObject;

public interface IEntity extends DBObject {

	/**
     * Sets a name/value pair in this object.
     * @param key Name to set
     * @param v Corresponding value
     * @return <tt>v</tt>
     */
    public Object put( String key , Object v );

    /**
     * Sets all key/value pairs from an object into this object
     * @param o the object
     */
    public void putAll(IEntity o );

    /**
     * Sets all key/value pairs from a map into this object
     * @param m the map
     */
    public void putAll( Map m );

    /**
     * Gets a field from this object by a given name.
     * @param key The name of the field fetch
     * @return The field, if found
     */
    public Object get( String key );

    /**
     * Returns a map representing this BSONObject.
     * @return the map
     */
    public Map toMap();

    /**
     * Removes a field with a given name from this object.
     * @param key The name of the field to remove
     * @return The value removed from this object
     */
    public Object removeField( String key );


    /**
     * Checks if this object contains a field with the given name.
     * @param s Field name for which to check
     * @return True if the field is present
     */
    public boolean containsField(String s);

    /**
     * Returns this object's fields' names
     * @return The names of the fields in this object
     */
    public Set<String> keySet();
}
