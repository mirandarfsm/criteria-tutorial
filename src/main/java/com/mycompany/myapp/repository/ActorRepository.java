package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Actor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Actor entity.
 */
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {

    @Query(value = "select distinct actor from Actor actor left join fetch actor.movies",
        countQuery = "select count(distinct actor) from Actor actor")
    Page<Actor> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct actor from Actor actor left join fetch actor.movies")
    List<Actor> findAllWithEagerRelationships();

    @Query("select actor from Actor actor left join fetch actor.movies where actor.id =:id")
    Optional<Actor> findOneWithEagerRelationships(@Param("id") Long id);
}
