package com.school.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TeacherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Teacher getTeacherSample1() {
        return new Teacher()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .specialization("specialization1")
            .street("street1")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1");
    }

    public static Teacher getTeacherSample2() {
        return new Teacher()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .specialization("specialization2")
            .street("street2")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2");
    }

    public static Teacher getTeacherRandomSampleGenerator() {
        return new Teacher()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .specialization(UUID.randomUUID().toString())
            .street(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString());
    }
}
