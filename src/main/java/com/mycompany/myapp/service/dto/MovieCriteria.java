package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.Subject;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Movie} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MovieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /movies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MovieCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Subject
     */
    public static class SubjectFilter extends Filter<Subject> {

        public SubjectFilter() {
        }

        public SubjectFilter(SubjectFilter filter) {
            super(filter);
        }

        @Override
        public SubjectFilter copy() {
            return new SubjectFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter year;

    private SubjectFilter subject;

    private IntegerFilter popularity;

    private BooleanFilter awards;

    public MovieCriteria() {
    }

    public MovieCriteria(MovieCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.popularity = other.popularity == null ? null : other.popularity.copy();
        this.awards = other.awards == null ? null : other.awards.copy();
    }

    @Override
    public MovieCriteria copy() {
        return new MovieCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public SubjectFilter getSubject() {
        return subject;
    }

    public void setSubject(SubjectFilter subject) {
        this.subject = subject;
    }

    public IntegerFilter getPopularity() {
        return popularity;
    }

    public void setPopularity(IntegerFilter popularity) {
        this.popularity = popularity;
    }

    public BooleanFilter getAwards() {
        return awards;
    }

    public void setAwards(BooleanFilter awards) {
        this.awards = awards;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MovieCriteria that = (MovieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(year, that.year) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(popularity, that.popularity) &&
            Objects.equals(awards, that.awards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        year,
        subject,
        popularity,
        awards
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (popularity != null ? "popularity=" + popularity + ", " : "") +
                (awards != null ? "awards=" + awards + ", " : "") +
            "}";
    }

}
