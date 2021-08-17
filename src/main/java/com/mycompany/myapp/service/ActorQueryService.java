package com.mycompany.myapp.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Actor;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.ActorRepository;
import com.mycompany.myapp.service.dto.ActorCriteria;

/**
 * Service for executing complex queries for {@link Actor} entities in the database.
 * The main input is a {@link ActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Actor} or a {@link Page} of {@link Actor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActorQueryService extends QueryService<Actor> {

    private final Logger log = LoggerFactory.getLogger(ActorQueryService.class);

    private final ActorRepository actorRepository;

    public ActorQueryService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    /**
     * Return a {@link List} of {@link Actor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Actor> findByCriteria(ActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Actor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Actor> findByCriteria(ActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.count(specification);
    }

    /**
     * Function to convert {@link ActorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Actor> createSpecification(ActorCriteria criteria) {
        Specification<Actor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Actor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Actor_.name));
            }
            if (criteria.getBirthdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthdate(), Actor_.birthdate));
            }
            if (criteria.getMovieId() != null) {
                specification = specification.and(buildSpecification(criteria.getMovieId(),
                    root -> root.join(Actor_.movies, JoinType.LEFT).get(Movie_.id)));
            }
        }
        return specification;
    }

    private Specification<Actor> createProjection() {
        return (root, query, build) -> {
            query.distinct(true);
            root.fetch(Actor_.movies, JoinType.LEFT);
            return null;
        };
      }
      
      private Specification<Actor> filterByIdIn(List<Long> ids) {
        return (root, query, builder) -> root.get(Actor_.id).in(ids);
      }
      
      private Specification<Movie> filterByYearGreaterThanToday() {
        return (root, query, build) -> {
            return build.greaterThan(root.get(Movie_.year), 80);
        };
      }
      
      private Specification<Actor> filterByRoles() {
        return (root, query, builder) -> {
            List<String> movies = Arrays.asList("Beat Street", "Power");
            Subquery<Movie> movieSubquery = query.subquery(Movie.class);
            Root<Movie> movie = movieSubquery.from(Movie.class);
            Join<Movie, Actor> join = movie.join(Movie_.actors, JoinType.LEFT);
            Predicate equal = builder.equal(join.get(Actor_.id), root.get(Actor_.id));
            Predicate in = movie.get(Movie_.title).in(movies);
            Predicate where = builder.and(equal, in);
            movieSubquery.select(movie).distinct(true).where(where);
            return builder.exists(movieSubquery);
        };
      }
}
