package com.complexschoolapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TeacherAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeacherAllPropertiesEquals(Teacher expected, Teacher actual) {
        assertTeacherAutoGeneratedPropertiesEquals(expected, actual);
        assertTeacherAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeacherAllUpdatablePropertiesEquals(Teacher expected, Teacher actual) {
        assertTeacherUpdatableFieldsEquals(expected, actual);
        assertTeacherUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeacherAutoGeneratedPropertiesEquals(Teacher expected, Teacher actual) {
        assertThat(expected)
            .as("Verify Teacher auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeacherUpdatableFieldsEquals(Teacher expected, Teacher actual) {
        assertThat(expected)
            .as("Verify Teacher relevant properties")
            .satisfies(e -> assertThat(e.getFirstName()).as("check firstName").isEqualTo(actual.getFirstName()))
            .satisfies(e -> assertThat(e.getLastName()).as("check lastName").isEqualTo(actual.getLastName()))
            .satisfies(e -> assertThat(e.getSpecialization()).as("check specialization").isEqualTo(actual.getSpecialization()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeacherUpdatableRelationshipsEquals(Teacher expected, Teacher actual) {
        assertThat(expected)
            .as("Verify Teacher relationships")
            .satisfies(e -> assertThat(e.getSubjects()).as("check subjects").isEqualTo(actual.getSubjects()));
    }
}
