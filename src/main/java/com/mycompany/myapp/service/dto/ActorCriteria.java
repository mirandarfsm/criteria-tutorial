package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Actor} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ActorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter birthdate;

    private LongFilter movieId;

    public ActorCriteria() {
    }

    public ActorCriteria(ActorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.birthdate = other.birthdate == null ? null : other.birthdate.copy();
        this.movieId = other.movieId == null ? null : other.movieId.copy();
    }

    @Override
    public ActorCriteria copy() {
        return new ActorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public InstantFilter getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(InstantFilter birthdate) {
        this.birthdate = birthdate;
    }

    public LongFilter getMovieId() {
        return movieId;
    }

    public void setMovieId(LongFilter movieId) {
        this.movieId = movieId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActorCriteria that = (ActorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        birthdate,
        movieId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
                (movieId != null ? "movieId=" + movieId + ", " : "") +
            "}";
    }

}
