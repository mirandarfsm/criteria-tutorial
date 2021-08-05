package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ActorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actor} and its DTO {@link ActorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {


    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "removeMovie", ignore = true)
    Actor toEntity(ActorDTO actorDTO);

    default Actor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Actor actor = new Actor();
        actor.setId(id);
        return actor;
    }
}
