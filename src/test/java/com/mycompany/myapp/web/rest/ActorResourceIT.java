package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.CriteriaApp;
import com.mycompany.myapp.domain.Actor;
import com.mycompany.myapp.domain.Movie;
import com.mycompany.myapp.repository.ActorRepository;
import com.mycompany.myapp.service.ActorService;
import com.mycompany.myapp.service.dto.ActorCriteria;
import com.mycompany.myapp.service.ActorQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ActorResource} REST controller.
 */
@SpringBootTest(classes = CriteriaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ActorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ActorRepository actorRepository;

    @Mock
    private ActorRepository actorRepositoryMock;

    @Mock
    private ActorService actorServiceMock;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ActorQueryService actorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActorMockMvc;

    private Actor actor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
            .name(DEFAULT_NAME)
            .birthdate(DEFAULT_BIRTHDATE);
        return actor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createUpdatedEntity(EntityManager em) {
        Actor actor = new Actor()
            .name(UPDATED_NAME)
            .birthdate(UPDATED_BIRTHDATE);
        return actor;
    }

    @BeforeEach
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();
        // Create the Actor
        restActorMockMvc.perform(post("/api/actors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActor.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    public void createActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor with an existing ID
        actor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc.perform(post("/api/actors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllActorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(actorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActorMockMvc.perform(get("/api/actors?eagerload=true"))
            .andExpect(status().isOk());

        verify(actorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllActorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(actorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActorMockMvc.perform(get("/api/actors?eagerload=true"))
            .andExpect(status().isOk());

        verify(actorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()));
    }


    @Test
    @Transactional
    public void getActorsByIdFiltering() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        Long id = actor.getId();

        defaultActorShouldBeFound("id.equals=" + id);
        defaultActorShouldNotBeFound("id.notEquals=" + id);

        defaultActorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActorShouldNotBeFound("id.greaterThan=" + id);

        defaultActorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllActorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name equals to DEFAULT_NAME
        defaultActorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the actorList where name equals to UPDATED_NAME
        defaultActorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllActorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name not equals to DEFAULT_NAME
        defaultActorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the actorList where name not equals to UPDATED_NAME
        defaultActorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllActorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultActorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the actorList where name equals to UPDATED_NAME
        defaultActorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllActorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name is not null
        defaultActorShouldBeFound("name.specified=true");

        // Get all the actorList where name is null
        defaultActorShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllActorsByNameContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name contains DEFAULT_NAME
        defaultActorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the actorList where name contains UPDATED_NAME
        defaultActorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllActorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where name does not contain DEFAULT_NAME
        defaultActorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the actorList where name does not contain UPDATED_NAME
        defaultActorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllActorsByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where birthdate equals to DEFAULT_BIRTHDATE
        defaultActorShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the actorList where birthdate equals to UPDATED_BIRTHDATE
        defaultActorShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllActorsByBirthdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where birthdate not equals to DEFAULT_BIRTHDATE
        defaultActorShouldNotBeFound("birthdate.notEquals=" + DEFAULT_BIRTHDATE);

        // Get all the actorList where birthdate not equals to UPDATED_BIRTHDATE
        defaultActorShouldBeFound("birthdate.notEquals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllActorsByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultActorShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the actorList where birthdate equals to UPDATED_BIRTHDATE
        defaultActorShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllActorsByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where birthdate is not null
        defaultActorShouldBeFound("birthdate.specified=true");

        // Get all the actorList where birthdate is null
        defaultActorShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByMovieIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        Movie movie = MovieResourceIT.createEntity(em);
        em.persist(movie);
        em.flush();
        actor.addMovie(movie);
        actorRepository.saveAndFlush(actor);
        Long movieId = movie.getId();

        // Get all the actorList where movie equals to movieId
        defaultActorShouldBeFound("movieId.equals=" + movieId);

        // Get all the actorList where movie equals to movieId + 1
        defaultActorShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActorShouldBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));

        // Check, that the count call also returns 1
        restActorMockMvc.perform(get("/api/actors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActorShouldNotBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActorMockMvc.perform(get("/api/actors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorService.save(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findById(actor.getId()).get();
        // Disconnect from session so that the updates on updatedActor are not directly saved in db
        em.detach(updatedActor);
        updatedActor
            .name(UPDATED_NAME)
            .birthdate(UPDATED_BIRTHDATE);

        restActorMockMvc.perform(put("/api/actors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedActor)))
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActor.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorMockMvc.perform(put("/api/actors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorService.save(actor);

        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Delete the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
