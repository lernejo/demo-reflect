package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataGeneratorTest {

    private final DataGenerator gen = new DataGenerator();

    @Test
    void generate_string() {
        String value = gen.generateRandom(String.class);

        assertThat(value).isNotBlank();

        String value2 = gen.generateRandom(String.class);

        assertThat(value2)
                .isNotEqualTo(value);
    }

    @Test
    void generate_int() {
        Integer value = gen.generateRandom(Integer.class);

        assertThat(value)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void generate_unknown_object() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> gen.generateRandom(Void.class))
                .withMessageContaining("class java.lang.Void");
    }

    @Test
    void generate_todo() {
        Todo value = gen.generateRandom(Todo.class);

        assertThat(value)
                .isNotNull();

        assertThat(value.author()).isNotBlank();
    }
}
