package org.app.repository;

import org.app.domain.Assignment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public abstract class AssignmentRepository {
//to connect DB
    @PersistenceContext(unitName = "X")
    private EntityManager em;

    public Assignment find(@NotNull Long id) {
        return em.find(Assignment.class, id);
    }

    @Transactional(REQUIRED)
    public Assignment create(@NotNull @Min(10) Assignment assignment) {
        em.persist(assignment);
        return assignment;
    }

    @Transactional(REQUIRED)
    public void delete(@NotNull @Min(1) @Max(1000) Long id){
        em.remove(em.getReference(Assignment.class, id));
    }
    public List<Assignment> findAll() {
        TypedQuery<Assignment> query= em.createQuery("select a from Assignment a order by Date desc", Assignment.class);
        return query.getResultList();
    }
}
