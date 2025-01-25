package com.complexschoolapp.domain;

import static com.complexschoolapp.domain.StudentTestSamples.*;
import static com.complexschoolapp.domain.SubjectTestSamples.*;
import static com.complexschoolapp.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.complexschoolapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subject.class);
        Subject subject1 = getSubjectSample1();
        Subject subject2 = new Subject();
        assertThat(subject1).isNotEqualTo(subject2);

        subject2.setId(subject1.getId());
        assertThat(subject1).isEqualTo(subject2);

        subject2 = getSubjectSample2();
        assertThat(subject1).isNotEqualTo(subject2);
    }

    @Test
    void teachersTest() {
        Subject subject = getSubjectRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        subject.addTeachers(teacherBack);
        assertThat(subject.getTeachers()).containsOnly(teacherBack);
        assertThat(teacherBack.getSubjects()).containsOnly(subject);

        subject.removeTeachers(teacherBack);
        assertThat(subject.getTeachers()).doesNotContain(teacherBack);
        assertThat(teacherBack.getSubjects()).doesNotContain(subject);

        subject.teachers(new HashSet<>(Set.of(teacherBack)));
        assertThat(subject.getTeachers()).containsOnly(teacherBack);
        assertThat(teacherBack.getSubjects()).containsOnly(subject);

        subject.setTeachers(new HashSet<>());
        assertThat(subject.getTeachers()).doesNotContain(teacherBack);
        assertThat(teacherBack.getSubjects()).doesNotContain(subject);
    }

    @Test
    void studentsTest() {
        Subject subject = getSubjectRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        subject.addStudents(studentBack);
        assertThat(subject.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getSubjects()).containsOnly(subject);

        subject.removeStudents(studentBack);
        assertThat(subject.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getSubjects()).doesNotContain(subject);

        subject.students(new HashSet<>(Set.of(studentBack)));
        assertThat(subject.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getSubjects()).containsOnly(subject);

        subject.setStudents(new HashSet<>());
        assertThat(subject.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getSubjects()).doesNotContain(subject);
    }
}
