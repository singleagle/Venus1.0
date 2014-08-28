package com.enjoy.venus.persistence;

import java.util.List;


public interface IEntityProvider{
	
	/**
	 * Get an instance of an {@link T}
	 * 
	 * @param id - id of instance to retrieve
	 * @return instance with the id gived
	 */
	IEntity get(long id);

	/**
	 * Get a list of instances of {@link T}
	 * 
	 * @param start - the start to range to retrieve
	 * @param count - maximum instance count to retrieve
	 * @return list of instances in the range specified
	 */
	List<IEntity> get(int start, int count);

	/**
	 * Get a list of instances of {@link T}
	 * 
	 * @param search - string search criteria to filter entities
	 * @param start - the start to range to retrieve
	 * @param count - maximum instance count to retrieve
	 * @param order - column and sort order
	 * @return list of instances in the range specified
	 */
	List<IEntity> get(String search, int start, int count, String order);

	/**
	 * Count the number of instances of {@link T}
	 * 
	 * @return count of instances
	 */
	long count();

	/**
	 * Count the number of instances of {@link T}
	 * 
	 * @param search - string search criteria to filter entities
	 * @return count of instances satisfying given search criteria
	 */
	long count(String search);

	/**
	 * Update an instance of {@link T}
	 * 
	 * @param entity - entity to be updated
	 * @param userId - user performed update
	 */
	IEntity update(IEntity entity, long userId);

	/**
	 * Delete an instance of {@link T}
	 * 
	 * @param entity - entity to be deleted
	 * @param userId - user performed delete
	 */
	void delete(IEntity entity, long userId);
}
