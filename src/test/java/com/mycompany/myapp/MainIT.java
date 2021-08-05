package com.mycompany.myapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.mycompany.myapp.domain.Actor;
import com.mycompany.myapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.transaction.annotation.Transactional;


/**
 * Integration tests for {@link UserService}.
 */
@SpringBootTest(classes = CriteriaApp.class)
@Transactional
public class MainIT {

    @Autowired
    private EntityManager em;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @BeforeEach
    public void init() {
    }

    @Test
    @Transactional
    public void assertThatActorsTablesIsNotNull() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> itemRoot = criteriaQuery.from(Actor.class);
        criteriaQuery.select(itemRoot);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = sql.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(2177);
        assertThat(results.get(0).getName()).isEqualTo("Antonio Banderas");
    }


    @Test
    @Transactional
    public void assertThatMovieTablesIsNotNull() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

}
