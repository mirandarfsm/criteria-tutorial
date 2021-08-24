package com.mycompany.myapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import com.mycompany.myapp.domain.Actor;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Movie;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.ActorDTO;
import com.mycompany.myapp.service.dto.ActorWithAgeDTO;

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
        Query sql = em.createNativeQuery("SELECT * FROM MOVIE", Movie.class);

        // JPQL
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie", Movie.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(root);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        sql.getResultList();
        jpql.getResultList();
        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1659);

    }

    @Test
    @Transactional
    public void assertThatActorIsZero() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor WHERE id = 0", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE actor.id = 0", Actor.class);
        
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
        Query sql = em.createNativeQuery("SELECT * FROM movie WHERE year = 1990", Movie.class);

        // JPQL
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie WHERE movie.year = 1990", Movie.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Predicate equalNinety = criteriaBuilder.equal(root.get("year"), 1990);
        criteriaQuery.where(equalNinety);
        criteriaQuery.select(root);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        sql.getResultList();
        jpql.getResultList();
        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(105);
    }

    @Test
    @Transactional
    public void assertThatMoviesReleasedOverFiftyYearsAgo() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM movie WHERE year+50 < 2021", Movie.class);
        
        // JPQL
        // TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie WHERE movie.year+50 < 2021", Movie.class);
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie WHERE movie.year < 2021-50", Movie.class);

        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Predicate lessThanSeventyOne = criteriaBuilder.lessThan(root.get("year"), 1971);
        criteriaQuery.where(lessThanSeventyOne);
        criteriaQuery.select(root);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(491);

    }

    @Test
    @Transactional
    public void assertThatMoviesHaveAwardInSixtyOne() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM movie WHERE year = 1961 and awards = true", Movie.class);
        
        // JPQL
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie WHERE movie.year = 1961 and movie.awards = true", Movie.class);

        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Predicate yearEqualSixtyOne = criteriaBuilder.equal(root.get("year"), 1961);
        Predicate awardsIsTrue = criteriaBuilder.equal(root.get("awards"), true);
        Predicate and = criteriaBuilder.and(yearEqualSixtyOne, awardsIsTrue);
        criteriaQuery.where(and);
        criteriaQuery.select(root);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4);

    }

    @Test
    @Transactional
    public void assertThatMoviesHaveAwardOrPopularityIsGreaterThanEighty() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM movie WHERE popularity > 80 or awards = true", Movie.class);
        
        // JPQL
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Movie movie WHERE movie.popularity > 80 or movie.awards = true", Movie.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Predicate popularityGreaterThanEighty = criteriaBuilder.greaterThan(root.get("popularity"), 80);
        Predicate awardsIsTrue = criteriaBuilder.equal(root.get("awards"), true);
        Predicate and = criteriaBuilder.or(popularityGreaterThanEighty, awardsIsTrue);
        criteriaQuery.where(and);
        criteriaQuery.select(root);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(289);
    }

    @Test
    @Transactional
    public void assertThatActorsHaveFortyYearOld() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor WHERE birthdate between '1981-01-01' and '1981-12-31'", Actor.class);
        Query sql1 = em.createNativeQuery("SELECT * FROM actor WHERE EXTRACT(YEAR FROM birthdate) = 1981", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE actor.birthdate between '1981-01-01' and '1981-12-31'", Actor.class);
        TypedQuery<Actor> jpql1 = em.createQuery("SELECT actor FROM Actor actor WHERE EXTRACT(YEAR FROM actor.birthdate) = 1981", Actor.class);

        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Predicate betweenEightyOne = criteriaBuilder.between(root.get("birthdate"), Instant.parse("1981-01-01T00:00:00.00Z"), Instant.parse("1981-12-31T00:00:00.00Z"));
        // Predicate extractBirthdateYear = criteriaBuilder.equal(criteriaBuilder.function("EXTRACT", Integer.class, criteriaBuilder.literal("YEAR"), criteriaBuilder.literal("FROM"),root.get("birthdate")),1981);
        criteriaQuery.where(betweenEightyOne);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(42);

    }


    @Test
    @Transactional
    public void assertThatActorsBornDayThirtyth() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor WHERE EXTRACT(DAY FROM birthdate) = 30", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE EXTRACT(DAY FROM actor.birthdate) = 30", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Predicate extractBirthdateDay = criteriaBuilder.equal(criteriaBuilder.function("DAY", Integer.class, root.get("birthdate")), 30);
        criteriaQuery.where(extractBirthdateDay);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isEmpty();
    }

    @Test
    @Transactional
    public void assertThatMoviesHaveHightPopularity() {
        // SQL
        Query sql = em.createNativeQuery("SELECT max(popularity) FROM movie", Integer.class);
        
        // JPQL
        TypedQuery<Integer> jpql = em.createQuery("SELECT max(movie.popularity) FROM Movie movie", Integer.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(criteriaBuilder.max(root.get("popularity")));
        TypedQuery<Integer> query = em.createQuery(criteriaQuery);

        Integer results = query.getSingleResult();
        assertThat(results).isEqualTo(88);

    }

    @Test
    @Transactional
    public void assertThatMoviesSumAllPopularity() {
        // SQL
        Query sql = em.createNativeQuery("SELECT sum(popularity) FROM movie", Long.class);
        
        // JPQL
        TypedQuery<Long> jpql = em.createQuery("SELECT sum(movie.popularity) FROM Movie movie", Long.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(criteriaBuilder.sum(root.get("popularity")));
        TypedQuery<Integer> query = em.createQuery(criteriaQuery);

        Integer results = query.getSingleResult();
        assertThat(results).isEqualTo(71261);

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
        Query sql = em.createNativeQuery("SELECT * FROM actor, movie", Actor.class);
        Query sql2 = em.createNativeQuery("SELECT * FROM actor CROSS JOIN movie", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor, Movie movie", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Root<Movie> movie = criteriaQuery.from(Movie.class);
        criteriaQuery.multiselect(root, movie);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(3611643);

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
        Query sql = em.createNativeQuery("SELECT * FROM actor a INNER JOIN actor_movie am ON am.actor_id = a.id INNER JOIN movie m ON m.id = am.movie_id WHERE m.awards = true", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor INNER JOIN actor.movies movie WHERE movie.awards = true", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movies = root.join("movies");
        Predicate movieHasAward = criteriaBuilder.isTrue(movies.get("awards"));
        criteriaQuery.where(movieHasAward);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(462);
    }

    @Test
    @Transactional
    public void assertThatActorsInnerJoinMovieWithMovieHasAward() {
        // SQL
        Query sql1 = em.createNativeQuery("SELECT * FROM actor a INNER JOIN actor_movie am ON am.actor_id = a.id INNER JOIN movie m ON m.id = am.movie_id and m.awards = true", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql1 = em.createQuery("SELECT actor FROM Actor actor INNER JOIN actor.movies movie with movie.awards = true", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movies = root.join("movies");
        Predicate movieHasAward = criteriaBuilder.isTrue(movies.get("awards"));
        movies.on(movieHasAward);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(462);
        
    }

    @Test
    @Transactional
    public void assertThatActorsLeftJoinMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a LEFT OUTER JOIN actor_movie am ON am.actor_id = a.id LEFT OUTER JOIN movie m ON m.id = am.movie_id", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor LEFT OUTER JOIN actor.movies", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join movies = root.join("movies", JoinType.LEFT);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4286);
        
    }

    @Test
    @Transactional
    public void assertThatActorsRightJoinMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a RIGHT OUTER JOIN actor_movie am ON am.actor_id = a.id RIGHT OUTER JOIN movie m ON m.id = am.movie_id", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor RIGHT OUTER JOIN actor.movies", Actor.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movies = root.join("movies", JoinType.INNER);
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = jpql.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4286);
        
    }

    @Test
    @Transactional
    public void assertThatActorsFullJoinMovie() {
        // SQL
        // FULL OUTER JOIN is not supported by H2
        // JPQL
        // FULL OUTER JOIN is not supported by H2
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
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE exists (SELECT 1 FROM actor_movie am where a.id=am.actor_id)", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE exists (SELECT 1 FROM Actor actor2 JOIN actor.movies where actor.id = actor2.id)", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        actor.join("movies");
        Predicate actorEqualRoot = criteriaBuilder.equal(root.get("id"), actor.get("id"));
        subQuery.where(actorEqualRoot);
        subQuery.select(actor.get("id"));

        criteriaQuery.where(criteriaBuilder.exists(subQuery));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(2177);
        
    }

    @Test
    @Transactional
    public void assertThatActorsExistsMovieSemiJoinAndHasAward() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE exists (SELECT 1 FROM actor_movie am JOIN movie m on m.id=am.movie_id where a.id=am.actor_id and m.awards = true)", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE exists (SELECT 1 FROM Actor actor2 JOIN actor2.movies movie where actor.id = actor2.id and movie.awards = true)", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        Join<Actor, Movie> movie = actor.join("movies");
        Predicate actorEqualRoot = criteriaBuilder.equal(root.get("id"), actor.get("id"));
        Predicate movieHasAward = criteriaBuilder.isTrue(movie.get("awards"));
        subQuery.where(criteriaBuilder.and(actorEqualRoot, movieHasAward));
        subQuery.select(actor.get("id"));

        criteriaQuery.where(criteriaBuilder.exists(subQuery));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(344);
        
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtyCrossJoin() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a, actor_movie am, movie m WHERE a.id=am.actor_id and am.movie_id=m.id and m.awards=true and a.birthdate > '1991-01-01'", Actor.class);
        Query sql1 = em.createNativeQuery("SELECT * FROM actor a CROSS JOIN actor_movie am CROSS JOIN movie m WHERE a.id=am.actor_id and am.movie_id=m.id and m.awards=true and a.birthdate > '1991-01-01'", Actor.class);
        
        // JPQL
        // Não é possível, tabela actor_movie não mapeada como class        
        // Criteria
        // Não é possível, tabela actor_movie não mapeada como class
        
        List<Actor> results = sql1.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(141);
        
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtyEquiJoin() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a JOIN actor_movie am on a.id=am.actor_id JOIN movie m on am.movie_id=m.id WHERE m.awards=true and a.birthdate > '1991-01-01'", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor JOIN actor.movies movie WHERE movie.awards = true and actor.birthdate > '1991-01-01'", Actor.class);
        
        // Criteria
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movies = root.join("movies", JoinType.INNER);
        Predicate movieHasAwards = criteriaBuilder.isTrue(movies.get("awards"));
        Predicate actorLessThanThirtyYear = criteriaBuilder.greaterThan(root.get("birthdate"), Instant.parse("1991-01-01T00:00:00.00Z"));
        criteriaQuery.where(criteriaBuilder.and(movieHasAwards, actorLessThanThirtyYear));
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(141);
    }

    @Test
    @Transactional
    public void assertThatActorsHasAwardAndLessThanThirtySemiJoin() {
        // SQL
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE exists (SELECT 1 FROM actor_movie am JOIN movie m on m.id=am.movie_id where a.id=am.actor_id and m.awards = true and a.birthdate > '1991-01-01')", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE exists (SELECT 1 FROM Actor actor2 JOIN actor2.movies movie where actor.id = actor2.id and movie.awards = true and actor2.birthdate > '1991-01-01')", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        Join<Actor, Movie> movie = actor.join("movies");
        Predicate actorEqualRoot = criteriaBuilder.equal(root.get("id"), actor.get("id"));
        Predicate movieHasAward = criteriaBuilder.isTrue(movie.get("awards"));
        Predicate actorLessThanThirtyYear = criteriaBuilder.greaterThan(root.get("birthdate"), Instant.parse("1991-01-01T00:00:00.00Z"));
        subQuery.where(criteriaBuilder.and(actorEqualRoot, movieHasAward, actorLessThanThirtyYear));
        subQuery.select(actor.get("id"));

        criteriaQuery.where(criteriaBuilder.exists(subQuery));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = jpql.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(101);
        
    }

    @Ignore
    @Test
    @Transactional
    public void assertThatActorsDoesntHaveAwardEquiJoin() {
        // SQL
        // Query sql = em.createNativeQuery("SELECT * FROM actor a JOIN actor_movie am on a.id=am.actor_id JOIN movie m on am.movie_id=m.id WHERE m.awards=true", Actor.class);

        // JPQL
        // TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor JOIN actor.movies movie WHERE movie.awards = false", Actor.class);
        
        // Criteria
        // CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        // Root<Actor> root = criteriaQuery.from(Actor.class);
        // Join<Actor, Movie> movies = root.join("movies", JoinType.INNER);
        // Predicate movieDoesntHaveAwards = criteriaBuilder.isFalse(movies.get("awards"));
        // criteriaQuery.where(movieDoesntHaveAwards);
        // criteriaQuery.select(root);
        // TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        // List<Actor> results = query.getResultList();
        // assertThat(results).isNotEmpty();
        // assertThat(results.size()).isEqualTo(1833);
        
    }

    @Ignore
    @Test
    @Transactional
    public void assertThatActorsDoesntHaveAwardSemiJoin() {
        // // SQL
        // Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE not exists (SELECT 1 FROM actor_movie am JOIN movie m on m.id=am.movie_id where a.id=am.actor_id and m.awards = true)", Actor.class);

        // // JPQL
        // TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE exists (SELECT 1 FROM Actor actor2 JOIN actor2.movies movie where actor.id = actor2.id and movie.awards = true)", Actor.class);

        // // Criteria
        // CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        // Root<Actor> root = criteriaQuery.from(Actor.class);
        
        // Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        // Root<Actor> actor = subQuery.from(Actor.class);
        // Join<Actor, Movie> movie = actor.join("movies");
        // Predicate actorEqualRoot = criteriaBuilder.equal(root.get("id"), actor.get("id"));
        // Predicate movieDoesntHaveAward = criteriaBuilder.isFalse(movie.get("awards"));
        // subQuery.where(criteriaBuilder.and(actorEqualRoot, movieDoesntHaveAward));
        // subQuery.select(actor.get("id"));

        // criteriaQuery.where(criteriaBuilder.exists(subQuery));
        // TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        // List<Actor> results = query.getResultList();
        // assertThat(results).isNotEmpty();
        // assertThat(results.size()).isEqualTo(1833);
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
        Query sql = em.createNativeQuery("SELECT * FROM actor a WHERE not exists (SELECT 1 FROM actor_movie am JOIN movie m on m.id=am.movie_id where a.id=am.actor_id and m.awards = true)", Actor.class);

        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor WHERE not exists (SELECT 1 FROM Actor actor2 JOIN actor2.movies movie where actor.id = actor2.id and movie.awards = true)", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        Join<Actor, Movie> movie = actor.join("movies");
        Predicate actorEqualRoot = criteriaBuilder.equal(root.get("id"), actor.get("id"));
        Predicate movieDoesntHaveAward = criteriaBuilder.isTrue(movie.get("awards"));
        subQuery.where(criteriaBuilder.and(actorEqualRoot, movieDoesntHaveAward));
        subQuery.select(actor.get("id"));

        criteriaQuery.where(criteriaBuilder.not(criteriaBuilder.exists(subQuery)));
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1833);
        
    }

    @Test
    @Transactional
    public void assertThatMovieAntonioBanderasAndVictoriaAbrilWorkToghether() {
        // SQL
        Query sql = em.createNativeQuery("select * from actor a JOIN actor_movie am on a.id=am.actor_id JOIN movie m on am.movie_id=m.id where a.name = 'Antonio Banderas' and m.id in (select m.id from actor a JOIN actor_movie am on a.id=am.actor_id JOIN movie m on am.movie_id=m.id WHERE a.name = 'Victoria Abril');", Movie.class);
        
        // JPQL
        TypedQuery<Movie> jpql = em.createQuery("SELECT movie FROM Actor actor JOIN actor.movies movie WHERE actor.name = 'Antonio Banderas' and movie.id in (SELECT movie1.id FROM Actor actor1 JOIN actor1.movies movie1 where actor1.name = 'Victoria Abril')", Movie.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor,Movie> movie = root.join("movies");
        
        Subquery<Actor> subQuery = criteriaQuery.subquery(Actor.class);
        Root<Actor> actor = subQuery.from(Actor.class);
        Join<Actor, Movie> movie1 = actor.join("movies");
        Predicate actorNameIsVictoriaAbril = criteriaBuilder.equal(actor.get("name"), "Victoria Abril");
        subQuery.where(actorNameIsVictoriaAbril);
        subQuery.select(movie1.get("id"));

        Predicate actorNameIsAntonioBanderas = criteriaBuilder.equal(root.get("name"), "Antonio Banderas");
        criteriaQuery.where(criteriaBuilder.and(movie.in(subQuery), actorNameIsAntonioBanderas));
        criteriaQuery.select(movie);
        TypedQuery<Movie> query = em.createQuery(criteriaQuery);

        List<Movie> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        
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
        Query sql = em.createNativeQuery("select * from actor a JOIN actor_movie am on a.id=am.actor_id JOIN movie m on am.movie_id=m.id ", Actor.class);
        
        // JPQL
        TypedQuery<Actor> jpql = em.createQuery("SELECT actor FROM Actor actor JOIN actor.movies movie ", Actor.class);

        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Actor> criteriaQuery = criteriaBuilder.createQuery(Actor.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Fetch<Actor,Movie> movie = root.fetch("movies");
        criteriaQuery.select(root);
        TypedQuery<Actor> query = em.createQuery(criteriaQuery);

        List<Actor> results = query.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(4286);

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
        Query sql = em.createNativeQuery("SELECT COUNT(1) FROM movie m");

        // JPQL
        TypedQuery<Long> jpql = em.createQuery("SELECT COUNT(1) FROM Movie movie", Long.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(criteriaBuilder.count(root.get("id")));
        TypedQuery<Long> query = em.createQuery(criteriaQuery);

        Long results = query.getSingleResult();
        assertThat(results).isEqualTo(1659);

    }

    @Test
    @Transactional
    public void assertThatSumMoviesYears() {
        // SQL
        Query sql = em.createNativeQuery("SELECT SUM(m.year) FROM movie m");

        // JPQL
        TypedQuery<Long> jpql = em.createQuery("SELECT SUM(movie.year) FROM Movie movie", Long.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.select(criteriaBuilder.sum(root.get("year")));
        TypedQuery<Integer> query = em.createQuery(criteriaQuery);

        Integer results = query.getSingleResult();
        assertThat(results).isEqualTo(3277857);

    }


    @Test
    @Transactional
    public void assertThatAvgActorsAge() {
        // SQL
        Query sql = em.createNativeQuery("SELECT AVG(2021-YEAR(a.birthdate)) FROM actor a");
        
        // JPQL
        TypedQuery<Double> jpql = em.createQuery("SELECT AVG(2021-YEAR(actor.birthdate)) FROM Actor actor", Double.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.select(criteriaBuilder.avg(criteriaBuilder.diff(2021, criteriaBuilder.function("YEAR", Integer.class, root.get("birthdate")))));
        TypedQuery<Double> query = em.createQuery(criteriaQuery);

        Integer results = query.getSingleResult().intValue();
        assertThat(results).isEqualTo(46);

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
        Query sql = em.createNativeQuery("SELECT a.birthdate,COUNT(1) FROM actor a GROUP BY a.birthdate", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT actor.birthdate as birthdate, COUNT(1) as total FROM Actor actor GROUP BY actor.birthdate", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        criteriaQuery.multiselect(root.get("birthdate").alias("birthdate"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.groupBy(root.get("birthdate"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(2073);
        assertThat(results.get(0).get("total")).isEqualTo(1L);
        assertThat(results.get(1).get("total")).isEqualTo(1L);

    }

    @Test
    @Transactional
    public void assertThatCountActorByMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT m.title,COUNT(a.id) FROM actor a join actor_movie am on am.actor_id=a.id join movie m on am.movie_id=m.id GROUP BY m.title", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.title as title, COUNT(actor.id) as total FROM Actor actor JOIN actor.movies movie GROUP BY movie.title", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movie = root.join("movies");
        criteriaQuery.multiselect(movie.get("title").alias("title"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.groupBy(movie.get("title"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(1657);
        assertThat(results.get(0).get("total")).isEqualTo(3L);
        assertThat(results.get(1).get("total")).isEqualTo(3L);
    }

    @Test
    @Transactional
    public void assertThatCountActorByYearMovie() {
        // SQL
        Query sql = em.createNativeQuery("SELECT m.year, COUNT(a.id) FROM actor a join actor_movie am on am.actor_id=a.id join movie m on am.movie_id=m.id GROUP BY m.year", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.year as year, COUNT(actor.id) as total FROM Actor actor JOIN actor.movies movie GROUP BY movie.year", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Join<Actor, Movie> movie = root.join("movies");
        criteriaQuery.multiselect(movie.get("year").alias("year"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.groupBy(movie.get("year"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(74);
        assertThat(results.get(0).get("total")).isEqualTo(2L);
        assertThat(results.get(1).get("total")).isEqualTo(3L);

    }

    @Test
    @Transactional
    public void assertThatCountMoviesPopulatiryGreatherThanNinetyGroupByPopularity() {
        // SQL
        Query sql = em.createNativeQuery("SELECT popularity,COUNT(1) FROM movie m WHERE popularity > 90 GROUP BY popularity", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.popularity as popularity, COUNT(1) as total FROM Movie movie WHERE movie.popularity > 90 GROUP BY popularity", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        criteriaQuery.multiselect(root.get("popularity").alias("popularity"), criteriaBuilder.count(root).alias("total"));
        criteriaQuery.where(criteriaBuilder.greaterThan(root.get("popularity"), 90));
        criteriaQuery.groupBy(root.get("popularity"));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isZero();

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
        Query sql = em.createNativeQuery("SELECT year,AVG(popularity) as total FROM movie m GROUP BY year HAVING total > 60", Tuple.class);
        
        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.year as year, AVG(movie.popularity) as total FROM Movie movie GROUP BY movie.popularity HAVING total > 60", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Expression<Double> total = criteriaBuilder.avg(root.get("popularity"));
        criteriaQuery.multiselect(root.get("year").alias("year"), total.alias("total"));
        criteriaQuery.groupBy(root.get("year"));
        criteriaQuery.having(criteriaBuilder.greaterThan(total, 60.0));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    public void assertThatMoviesYearWithPopulatiryAvgLessThanFourtyAndHaveAward() {
        // SQL
        Query sql = em.createNativeQuery("SELECT year,AVG(popularity) as total FROM movie m WHERE m.awards = true GROUP BY year HAVING total < 40", Tuple.class);

        // JPQL
        TypedQuery<Tuple> jpql = em.createQuery("SELECT movie.year as year, AVG(movie.popularity) as total FROM Movie movie WHERE movie.awards = true GROUP BY movie.popularity HAVING total < 40", Tuple.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        Expression<Double> total = criteriaBuilder.avg(root.get("popularity"));
        criteriaQuery.multiselect(root.get("year").alias("year"), total.alias("total"));
        criteriaQuery.where(criteriaBuilder.isTrue(root.get("awards")));
        criteriaQuery.groupBy(root.get("year"));
        criteriaQuery.having(criteriaBuilder.lessThan(total, 40.0));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        List<Tuple> results = query.getResultList();
        assertThat(results.size()).isEqualTo(21);

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
        // Query sql = em.createNativeQuery("SELECT id, name FROM actor m");
        
        // JPQL
        TypedQuery<ActorWithAgeDTO> jpql = em.createQuery("SELECT new com.mycompany.myapp.service.dto.ActorWithAgeDTO(actor.id, actor.name, 2021-YEAR(birthdate)) FROM Actor actor", ActorWithAgeDTO.class);
        
        // Criteria
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ActorWithAgeDTO> criteriaQuery = criteriaBuilder.createQuery(ActorWithAgeDTO.class);
        Root<Actor> root = criteriaQuery.from(Actor.class);
        Expression<Integer> age = criteriaBuilder.diff(2021, criteriaBuilder.function("YEAR", Integer.class, root.get("birthdate")));
        criteriaQuery.select(criteriaBuilder.construct(ActorWithAgeDTO.class, root.get("id"), root.get("name"), age));
        TypedQuery<ActorWithAgeDTO> query = em.createQuery(criteriaQuery);

        List<ActorWithAgeDTO> results = query.getResultList();
        assertThat(results.size()).isEqualTo(2177);
        assertThat(results.get(0).getName()).isEqualTo("Antonio Banderas");
        assertThat(results.get(0).getAge()).isEqualTo(56);

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
        criteriaQuery.select(root.get("year"));
        criteriaQuery.distinct(true);
        TypedQuery<Integer> query = em.createQuery(criteriaQuery);

        List<Integer> results = jpql.getResultList();
        assertThat(results.size()).isEqualTo(74);
    }   

}
