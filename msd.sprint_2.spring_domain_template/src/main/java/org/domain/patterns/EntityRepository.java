package org.domain.patterns;

import java.util.Collection;

public interface EntityRepository<T> {

	// Repository query implementation
	public abstract T getById(Object id);

	// QBExample
	public boolean contains(T entitySample);
	
	public abstract Collection<T> getAll(T entitySample);
	
	public abstract T get(T entitySample);

	public abstract Collection<T> toCollection();

	public abstract T[] toArray();

	// Repository transaction implementation
	
	public abstract T add(T entity);

	public abstract Collection<T> addAll(Collection<T> entities);

	public abstract boolean remove(T entity);

	public abstract boolean removeAll(Collection<T> entities);

	// Others
	public abstract int size();

	public abstract T refresh(T entity);

}