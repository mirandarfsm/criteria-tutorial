package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ActorMapperTest {

    private ActorMapper actorMapper;

    @BeforeEach
    public void setUp() {
        actorMapper = new ActorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(actorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(actorMapper.fromId(null)).isNull();
    }
}
