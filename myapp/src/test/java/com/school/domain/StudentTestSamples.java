package com.school.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Student getStudentSample1() {
        return new Student()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .street("street1")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1");
    }

    public static Student getStudentSample2() {
        return new Student()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .street("street2")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .street(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString());
    }
}
