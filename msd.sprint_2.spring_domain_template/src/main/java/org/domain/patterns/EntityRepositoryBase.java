package org.domain.patterns;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository("EntityRepositoryBase")
@Transactional
public class EntityRepositoryBase<T> implements EntityRepository<T> {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	// Spring Configuration must provide LocalEntityManagerFactoryBean
	@PersistenceContext
	protected EntityManager em;
	
	protected Class<T> repositoryType;
	protected String genericSQL;

	public EntityManager getEm() {
		return em;
	}
	
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public EntityRepositoryBase() {
		logger.info("START DEFAULT INIT: ENTITY REPOSITORY ... ");
		try {
			this.setRepositoryType(getEntityParametrizedType());
			
			logger.info("... END DEFAULT INIT: ENTITY REPOSITORY!");
		}catch(Exception ex) {
			logger.severe("Repository type not initialized! Set Repository Entity Type explicit! ");
		}
	}
	
	public EntityRepositoryBase(EntityManager em, Class<T> t) {
		this.em = em;
		this.repositoryType = t;
		genericSQL = "SELECT o FROM " + repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1)
				+ " o";
		logger.info("generic JPAQL: " + genericSQL);
	}

	public EntityRepositoryBase(Class<T> t) {
		this.repositoryType = t;
		genericSQL = "SELECT o FROM " + repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1)
				+ " o";
		logger.info("generic JPAQL: " + genericSQL);
	}	
	
	// Repository query implementation
	@Override
	public boolean contains(T entitySample) {
		Collection<T> samples = getAll(entitySample);
		if (samples != null && samples.size() > 0)
			return true;
		return false;
	}
	
	/* 
	 * @see org.app.patterns.EntityRepositoryService#getById(java.lang.Object)
	 */
	@Override
	public T getById(Object id) {
		return (T) em.find(repositoryType, id);
	}

	// QBExample
	/* 
	 * @see org.app.patterns.EntityRepositoryService#get(T)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<T> getAll(T entitySample) {

		Map<String, Object> sqlCriterias = new HashMap<String, Object>();
		try {
			// get all properties and transform them into sqlCriterias
			PropertyDescriptor[] properties = Introspector.getBeanInfo(repositoryType).getPropertyDescriptors();
			Object propertyValue;
			Method readMethod;
			for (PropertyDescriptor property : properties) {
				readMethod = property.getReadMethod();
				if (readMethod != null) {
					logger.info("readMethod = " + readMethod.getName());
					propertyValue = readMethod.invoke(entitySample);
					logger.info("propertyValue = " + propertyValue);
					if (propertyValue == null || property.getName().equals("class")) {
						continue;
					}
					if (propertyValue instanceof Collection && ((Collection) propertyValue).size() == 0) {
						continue;
					}
					if (propertyValue instanceof String ||
							propertyValue instanceof Number ||
							propertyValue instanceof BigDecimal ||
							propertyValue instanceof java.util.Date)
					sqlCriterias.put(property.getName(), propertyValue);
				}
			}
		} catch (IllegalAccessException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IntrospectionException ex) {
			Logger.getLogger(EntityRepositoryBase.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (sqlCriterias.isEmpty()) {
			return null;
		}

		String queryString = genericSQL + " WHERE ";
		for (String criteria : sqlCriterias.keySet()) {
			if (sqlCriterias.get(criteria) instanceof Collection) {
				queryString += "o." + criteria + " IN (:" + criteria + ") AND ";
			} else {
				queryString += "o." + criteria + " = :" + criteria + " AND ";
			}
		}
		queryString += " 1 = 1";

		logger.info("JPAQL: " + queryString);

		Query query = em.createQuery(queryString);
		for (String criteria : sqlCriterias.keySet()) {
			query = query.setParameter(criteria, sqlCriterias.get(criteria));
		}
		return query.getResultList();

	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#toCollection()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Collection<T> toCollection() {
		logger.info("JPAQL: " + genericSQL + "... em: " + em);

		return em.createQuery(genericSQL).getResultList();
	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#toArray()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		logger.info("JPAQL: " + genericSQL);

		List<T> entities = em.createQuery(genericSQL).getResultList();
		if (entities == null) {
			return null;
		}

		return (T[]) entities.toArray();
	}

	// Repository transaction implementation
	/* 
	 * @see org.app.patterns.EntityRepositoryService#add(T)
	 */
	@Override
	public T add(T entity) {
		try {
			entity = em.merge(entity);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#addAll(java.util.Collection)
	 */
	@Override
	public Collection<T> addAll(Collection<T> entities) {
		try {
			for (T entity : entities) {
				entity = em.merge(entity);
			}
			return entities;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#remove(T)
	 */
	@Override
	public boolean remove(T entity) {
		try {
			entity = em.merge(entity);
			em.remove(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		}
	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<T> entities) {
		try {
			for (Object entity : entities) {
				entity = em.merge(entity);
				em.remove(entity);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Others
	/* 
	 * @see org.app.patterns.EntityRepositoryService#size()
	 */
	@Override
	public int size() {
		String sqlCount = "SELECT count(o) FROM "
				+ repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1) + " o";

		logger.info("JPAQL: " + sqlCount);

		Long size = (Long) em.createQuery(sqlCount).getSingleResult();
		return size.intValue();
	}

	/* 
	 * @see org.app.patterns.EntityRepositoryService#refresh(T)
	 */
	@Override
	public T refresh(T entity) {
		entity = em.merge(entity);
		em.refresh(entity);
		return entity;
	}
	
	@Override
	public T get(T entitySample) {
		if (checkRepositoryType(entitySample))
			return null;
				
		Collection<T> results = getAll(entitySample);			
		
		if(results!=null && !results.isEmpty() && results.iterator().hasNext())
			return results.iterator().next();
		
		return null;
	}
	
	// Private reflection for RepositoryType
	private Class<?> extractClassFromType(Type t) throws ClassCastException {
	    if (t instanceof Class<?>) {
	        return (Class<?>)t;
	    }
	    return (Class<?>)((ParameterizedType)t).getRawType();
	}

	public Class<T> getEntityParametrizedType() throws ClassCastException {
	    Class<?> superClass = getClass(); // initial value
	    Type superType;
	    do {
	        superType = superClass.getGenericSuperclass();
	        superClass = extractClassFromType(superType);
	    } while (! (superClass.equals(EntityRepositoryBase.class)));

	    Type actualArg = ((ParameterizedType)superType).getActualTypeArguments()[0];
	    return (Class<T>)extractClassFromType(actualArg);
	}

	public Class<T> getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(Class<T> repositoryType) {
		if (repositoryType != null) {
			logger.info("init repositoryType: " + repositoryType.getSimpleName());
			this.repositoryType = repositoryType;
			this.genericSQL = "SELECT o FROM " + repositoryType.getName().substring(repositoryType.getName().lastIndexOf('.') + 1)
					+ " o";
			logger.info("init generic JPAQL: " + genericSQL);	
		}else
			logger.severe("Init repositoryType failed! RepositoryType is... " + repositoryType);
	}
	
	// Check and set repository type
	private boolean checkRepositoryType(T entitySample) {
		if (this.repositoryType == null && entitySample != null) {
			this.setRepositoryType((Class<T>) entitySample.getClass());
			return true;
		}
		return false;
	}
	
	//	
	@PostConstruct
	private void init() {
		System.out.println(">>>> getTypeName::: " + this.getClass().getTypeName());
	}
}