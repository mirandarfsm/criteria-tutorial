package com.mycompany.myapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.mycompany.myapp.domain.Actor;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Movie;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.ActorDTO;

import org.junit.Ignore;
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
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
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

    @Test
    @Transactional
    public void assertThatActorIsZero() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor WHERE id = 0", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE id = 0", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Predicate idEqualZeroPredicate = criteriaBuilder.equal(root.get("id"), 0);
        criteriaQuery.where(idEqualZeroPredicate);
        criteriaQuery.select(root);

        TypedQuery<Actor> query = em.createQuery(criteriaQuery);
        
        Actor result = query.getSingleResult();
        assertThat(result.getId()).isZero();
        assertThat(result.getName()).isEqualTo("Antonio Banderas");
    }


    @Test
    @Transactional
    public void assertThatMovieIsRealeaseInNinety() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesReleasedOverFiftyYearsAgo() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesHaveAwardInSixtyOne() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesHaveAwardOrPopularityIsGreaterThanEighty() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatActorsHaveFortyYearOld() {
        // SQL
        
        // JPQL
        
        // Criteria

    }


    @Test
    @Transactional
    public void assertThatActorsBornDayThirtyth() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesHaveHightPopularity() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesSumAllPopularity() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatActorsCrossJoinUser() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor, jhi_user", Actor.class);
        Query sql2 = em.createNativeQuery("SELECT * FROM actor CROSS JOIN jhi_user", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor, User user", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Root<User> user = criteriaQuery.from(User.class);
        criteriaQuery.multiselect(root, user);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(8708);
    }

    @Test
    @Transactional
    public void assertThatActorsCrossJoinMovie() {
        // SQL

        // JPQL

        // Criteria

    }
    
    @Test
    @Transactional
    public void assertThatActorsInnerJoinMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a INNER JOIN actor_movie am ON am.actor_id = a.id INNER JOIN movie m ON m.id = am.movie_id", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor INNER JOIN actor.movies", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join movies = root.join("movies");
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4286);
    }
  
    @Test
    @Transactional
    public void assertThatActorsInnerJoinMovieAndMovieHasAward() {
        // SQL
        
        // JPQL
        
        // Criteria
    }

    @Test
    @Transactional
    public void assertThatActorsInnerJoinMovieWithMovieHasAward() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsLeftJoinMovie() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsRightJoinMovie() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsFullJoinMovie() {
        // SQL
        
        // JPQL
        
        // Criteria
        // JPA JoinType does not provide the full outer join type!
        
    }

    @Test
    @Transactional
    public void assertThatActorsInMovieSemiJoin() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE a.id in (SELECT am.actor_id FROM actor_movie am)", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE actor.id in (SELECT actor.id FROM Actor actor JOIN actor.movies)", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        actor.join("movies");
        subQuery.select(actor.get("id"));

        criteriaQuery.where(root.get("id").in(subQuery));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(2177);
    }

    @Test
    @Transactional
    public void assertThatActorsExistsMovieSemiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsExistsMovieSemiJoinAndHasAward() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtyCrossJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtyEquiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtySemiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsDoesntHaveAwardEquiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatActorsDoesntHaveAwardSemiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }


    @Test
    @Transactional
    public void assertThatActorsDoesntHaveActorsIn() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE a.id not in (SELECT am.actor_id FROM actor_movie am)", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE actor.id not in (SELECT actor.id FROM Actor actor JOIN actor.movies)", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        actor.join("movies");
        subQuery.select(actor.get("id"));

        criteriaQuery.where(criteriaBuilder.not(root.get("id").in(subQuery)));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isEmpty();
    }

    @Test
    @Transactional
    public void assertThatActorsDoesntHaveAwardAntiJoin() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatMovieAntonioBanderasAndVictoriaAbrilWorkToghether() {
        // SQL
        
        // JPQL
        
        // Criteria
        
    }

    @Ignore
    @Test
    @Transactional
    public void assertThatDivisionExample() {
        // SQL
        Query sql = em.createNativeQuery("SELECT DISTINCT * FROM actor_movie am WHERE NOT EXISTS ( SELECT 1 FROM actor WHERE NOT EXISTS ( SELECT 1 FROM actor_movie am2 WHERE am.actor_id = am2.actor_id AND am2.movie_id = 1 ) )", Actor.class);
        
        // JPQL
        
        // Criteria
        
    }

    @Test
    @Transactional
    public void assertThatUserJoinFetchAuthorities() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM jhi_user u INNER JOIN jhi_user_authority ua ON ua.user_id = u.id INNER JOIN jhi_authority a ON a.name = ua.authority_name", User.class);
        
        // JPQL
        TypedQuery<User> jpql = em.createQuery("SELECT user FROM User user INNER JOIN FETCH user.authorities", User.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Fetch<User, Authority> authorities = root.fetch("authorities");
        criteriaQuery.select(root);
        TypedQuery<User> query = em.createQuery(criteriaQuery);

        List<User> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(5);
        assertThat(results.get(0).getAuthorities()).isNotEmpty();
    }

    @Test
    @Transactional
    public void assertThatActorsJoinFetchMovie() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountActors() {
        // SQL
        Query sql = em.createNativeQuery("SELECT COUNT(1) FROM actor a");
        
        // JPQL
        TypedQuery<Long> jpql = em.createQuery("SELECT COUNT(1) FROM User user", Long.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.select(criteriaBuilder.count(root.get("id")));
        TypedQuery<Long> query = em.createQuery(criteriaQuery);

        Long results = query.getSingleResult();
        assertThat(results).isEqualTo(2177);
    }


    @Test
    @Transactional
    public void assertThatCountMovies() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatSumMoviesYears() {
        // SQL
        
        // JPQL
        
        // Criteria

    }


    @Test
    @Transactional
    public void assertThatAvgActorsAge() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountMoviesGroupByAwards() {
        // SQL
        Query sql = em.createNativeQuery("SELECT awards,COUNT(1) FROM movie m GROUP BY awards", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.awards as awards, COUNT(1) as total FROM Movie movie GROUP BY awards", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.multiselect(root.get("awards").alias("awards"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.groupBy(root.get("awards"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).get("total")).isEqualTo(1496L);
        assertThat(results.get(1).get("total")).isEqualTo(163L);
    }

    @Test
    @Transactional
    public void assertThatCountActorGroupByYearBirthdate() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountActorByMovie() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountActorByYearMovie() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountMoviesPopulatiryGreatherThanNinetyGroupByPopularity() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatCountMovieByYearHavingYearGreatherThanNinety() {
        // SQL
        Query sql = em.createNativeQuery("SELECT year,COUNT(1) FROM movie m GROUP BY year HAVING year > '1990'", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.year, COUNT(1) as total FROM Movie movie GROUP BY year HAVING year > 1990", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.multiselect(root.get("year"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.groupBy(root.get("year"));
        criteriaQuery.having(criteriaBuilder.greaterThan(root.get("year"), 1990));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(5);
    }

    @Test
    @Transactional
    public void assertThatMoviesYearWithPopulatiryAvgGreatherThanSixty() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatMoviesYearWithPopulatiryAvgLessThanFourtyAndHaveAward() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Ignore
    @Test
    @Transactional
    public void assertThatUnionMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT id, name FROM actor a union all select id, title FROM movie m", Tuple.class);
        List<Tuple> sqlResults = sql.getResultList();
        assertThat(sqlResults.size()).isEqualTo(3836);

        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT id, name FROM Actor actor", Tuple.class);
        TypedQuery<Tuple> jpqlUnion = em.createQuery("SELECT id, title FROM Movie movie", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.multiselect(root.get("id"), root.get("name"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        CriteriaQuery<Tuple> criteriaQueryMovie = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> movieRoot = criteriaQueryMovie.from(Movie.class);
        criteriaQueryMovie.multiselect(movieRoot.get("id"), movieRoot.get("title"));
        TypedQuery<Tuple> queryUnion = em.createQuery(criteriaQueryMovie);

        List<Tuple> results = query.getResultList();
        results.addAll(queryUnion.getResultList());

        assertThat(results.size()).isEqualTo(3836);
    }  

    @Test
    @Transactional
    public void assertThatConstructors() {
        // SQL
        // Query sql = em.createNativeQuery("SELECT id, name FROM actor m");
        
        // JPQL
        TypedQuery<ActorDTO> jpql = em.createQuery("SELECT new com.mycompany.myapp.service.dto.ActorDTO(actor.id, actor.name) FROM Actor actor", ActorDTO.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ActorDTO> criteriaQuery = criteriaBuilder.createQuery(ActorDTO.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.select(criteriaBuilder.construct(ActorDTO.class, root.get("id"), root.get("name")));
        TypedQuery<ActorDTO> query = em.createQuery(criteriaQuery);

        List<ActorDTO> results = query.getResultList();
        assertThat(results.size()).isEqualTo(2177);
        assertThat(results.get(0).getName()).isEqualTo("Antonio Banderas");
    }


    @Test
    @Transactional
    public void assertThatActorWithAge() {
        // SQL
        
        // JPQL
        
        // Criteria

    }

    @Test
    @Transactional
    public void assertThatDistinctYear() {
        // SQL
        Query sql = em.createNativeQuery("SELECT distinct(year) FROM movie m");
        
        // JPQL
        TypedQuery<Integer> jpql = em.createQuery("SELECT distinct(movie.year) FROM Movie movie", Integer.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(root.get("year")).distinct(true);
        TypedQuery<Integer> query = em.createQuery(criteriaQuery);

        List<Integer> results = jpql.getResultList();
        assertThat(results.size()).isEqualTo(74);
    }   

}
