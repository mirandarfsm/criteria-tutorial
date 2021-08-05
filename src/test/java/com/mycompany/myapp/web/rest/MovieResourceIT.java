package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.CriteriaApp;
import com.mycompany.myapp.domain.Movie;
import com.mycompany.myapp.repository.MovieRepository;
import com.mycompany.myapp.service.MovieService;
import com.mycompany.myapp.service.dto.MovieCriteria;
import com.mycompany.myapp.service.MovieQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Subject;
/**
 * Integration tests for the {@link MovieResource} REST controller.
 */
@SpringBootTest(classes = CriteriaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MovieResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final Subject DEFAULT_SUBJECT = Subject.COMEDY;
    private static final Subject UPDATED_SUBJECT = Subject.HORROR;

    private static final Integer DEFAULT_POPULARITY = 1;
    private static final Integer UPDATED_POPULARITY = 2;
    private static final Integer SMALLER_POPULARITY = 1 - 1;

    private static final Boolean DEFAULT_AWARDS = false;
    private static final Boolean UPDATED_AWARDS = true;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQueryService movieQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieMockMvc;

    private Movie movie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(DEFAULT_TITLE)
            .year(DEFAULT_YEAR)
            .subject(DEFAULT_SUBJECT)
            .popularity(DEFAULT_POPULARITY)
            .awards(DEFAULT_AWARDS);
        return movie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createUpdatedEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(UPDATED_TITLE)
            .year(UPDATED_YEAR)
            .subject(UPDATED_SUBJECT)
            .popularity(UPDATED_POPULARITY)
            .awards(UPDATED_AWARDS);
        return movie;
    }

    @BeforeEach
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();
        // Create the Movie
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMovie.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testMovie.getPopularity()).isEqualTo(DEFAULT_POPULARITY);
        assertThat(testMovie.isAwards()).isEqualTo(DEFAULT_AWARDS);
    }

    @Test
    @Transactional
    public void createMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie with an existing ID
        movie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].awards").value(hasItem(DEFAULT_AWARDS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.popularity").value(DEFAULT_POPULARITY))
            .andExpect(jsonPath("$.awards").value(DEFAULT_AWARDS.booleanValue()));
    }


    @Test
    @Transactional
    public void getMoviesByIdFiltering() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        Long id = movie.getId();

        defaultMovieShouldBeFound("id.equals=" + id);
        defaultMovieShouldNotBeFound("id.notEquals=" + id);

        defaultMovieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("id.greaterThan=" + id);

        defaultMovieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMoviesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title equals to DEFAULT_TITLE
        defaultMovieShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title not equals to DEFAULT_TITLE
        defaultMovieShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the movieList where title not equals to UPDATED_TITLE
        defaultMovieShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMovieShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title is not null
        defaultMovieShouldBeFound("title.specified=true");

        // Get all the movieList where title is null
        defaultMovieShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllMoviesByTitleContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title contains DEFAULT_TITLE
        defaultMovieShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the movieList where title contains UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title does not contain DEFAULT_TITLE
        defaultMovieShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the movieList where title does not contain UPDATED_TITLE
        defaultMovieShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllMoviesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year equals to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year not equals to DEFAULT_YEAR
        defaultMovieShouldNotBeFound("year.notEquals=" + DEFAULT_YEAR);

        // Get all the movieList where year not equals to UPDATED_YEAR
        defaultMovieShouldBeFound("year.notEquals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultMovieShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is not null
        defaultMovieShouldBeFound("year.specified=true");

        // Get all the movieList where year is null
        defaultMovieShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is greater than or equal to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the movieList where year is greater than or equal to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is less than or equal to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the movieList where year is less than or equal to SMALLER_YEAR
        defaultMovieShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is less than DEFAULT_YEAR
        defaultMovieShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the movieList where year is less than UPDATED_YEAR
        defaultMovieShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is greater than DEFAULT_YEAR
        defaultMovieShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the movieList where year is greater than SMALLER_YEAR
        defaultMovieShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }


    @Test
    @Transactional
    public void getAllMoviesBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where subject equals to DEFAULT_SUBJECT
        defaultMovieShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the movieList where subject equals to UPDATED_SUBJECT
        defaultMovieShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllMoviesBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where subject not equals to DEFAULT_SUBJECT
        defaultMovieShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the movieList where subject not equals to UPDATED_SUBJECT
        defaultMovieShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllMoviesBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultMovieShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the movieList where subject equals to UPDATED_SUBJECT
        defaultMovieShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllMoviesBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where subject is not null
        defaultMovieShouldBeFound("subject.specified=true");

        // Get all the movieList where subject is null
        defaultMovieShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity equals to DEFAULT_POPULARITY
        defaultMovieShouldBeFound("popularity.equals=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity equals to UPDATED_POPULARITY
        defaultMovieShouldNotBeFound("popularity.equals=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity not equals to DEFAULT_POPULARITY
        defaultMovieShouldNotBeFound("popularity.notEquals=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity not equals to UPDATED_POPULARITY
        defaultMovieShouldBeFound("popularity.notEquals=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity in DEFAULT_POPULARITY or UPDATED_POPULARITY
        defaultMovieShouldBeFound("popularity.in=" + DEFAULT_POPULARITY + "," + UPDATED_POPULARITY);

        // Get all the movieList where popularity equals to UPDATED_POPULARITY
        defaultMovieShouldNotBeFound("popularity.in=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is not null
        defaultMovieShouldBeFound("popularity.specified=true");

        // Get all the movieList where popularity is null
        defaultMovieShouldNotBeFound("popularity.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is greater than or equal to DEFAULT_POPULARITY
        defaultMovieShouldBeFound("popularity.greaterThanOrEqual=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity is greater than or equal to UPDATED_POPULARITY
        defaultMovieShouldNotBeFound("popularity.greaterThanOrEqual=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is less than or equal to DEFAULT_POPULARITY
        defaultMovieShouldBeFound("popularity.lessThanOrEqual=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity is less than or equal to SMALLER_POPULARITY
        defaultMovieShouldNotBeFound("popularity.lessThanOrEqual=" + SMALLER_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is less than DEFAULT_POPULARITY
        defaultMovieShouldNotBeFound("popularity.lessThan=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity is less than UPDATED_POPULARITY
        defaultMovieShouldBeFound("popularity.lessThan=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is greater than DEFAULT_POPULARITY
        defaultMovieShouldNotBeFound("popularity.greaterThan=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity is greater than SMALLER_POPULARITY
        defaultMovieShouldBeFound("popularity.greaterThan=" + SMALLER_POPULARITY);
    }


    @Test
    @Transactional
    public void getAllMoviesByAwardsIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where awards equals to DEFAULT_AWARDS
        defaultMovieShouldBeFound("awards.equals=" + DEFAULT_AWARDS);

        // Get all the movieList where awards equals to UPDATED_AWARDS
        defaultMovieShouldNotBeFound("awards.equals=" + UPDATED_AWARDS);
    }

    @Test
    @Transactional
    public void getAllMoviesByAwardsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where awards not equals to DEFAULT_AWARDS
        defaultMovieShouldNotBeFound("awards.notEquals=" + DEFAULT_AWARDS);

        // Get all the movieList where awards not equals to UPDATED_AWARDS
        defaultMovieShouldBeFound("awards.notEquals=" + UPDATED_AWARDS);
    }

    @Test
    @Transactional
    public void getAllMoviesByAwardsIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where awards in DEFAULT_AWARDS or UPDATED_AWARDS
        defaultMovieShouldBeFound("awards.in=" + DEFAULT_AWARDS + "," + UPDATED_AWARDS);

        // Get all the movieList where awards equals to UPDATED_AWARDS
        defaultMovieShouldNotBeFound("awards.in=" + UPDATED_AWARDS);
    }

    @Test
    @Transactional
    public void getAllMoviesByAwardsIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where awards is not null
        defaultMovieShouldBeFound("awards.specified=true");

        // Get all the movieList where awards is null
        defaultMovieShouldNotBeFound("awards.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].awards").value(hasItem(DEFAULT_AWARDS.booleanValue())));

        // Check, that the count call also returns 1
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .title(UPDATED_TITLE)
            .year(UPDATED_YEAR)
            .subject(UPDATED_SUBJECT)
            .popularity(UPDATED_POPULARITY)
            .awards(UPDATED_AWARDS);

        restMovieMockMvc.perform(put("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMovie)))
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMovie.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testMovie.getPopularity()).isEqualTo(UPDATED_POPULARITY);
        assertThat(testMovie.isAwards()).isEqualTo(UPDATED_AWARDS);
    }

    @Test
    @Transactional
    public void updateNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc.perform(put("/api/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(movie)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
