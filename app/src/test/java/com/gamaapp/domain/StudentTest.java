package com.gamaapp.domain;

import static com.gamaapp.domain.StudentTestSamples.*;
import static com.gamaapp.domain.SubjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gamaapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void subjectsTest() {
        Student student = getStudentRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        student.addSubjects(subjectBack);
        assertThat(student.getSubjects()).containsOnly(subjectBack);

        student.removeSubjects(subjectBack);
        assertThat(student.getSubjects()).doesNotContain(subjectBack);

        student.subjects(new HashSet<>(Set.of(subjectBack)));
        assertThat(student.getSubjects()).containsOnly(subjectBack);

        student.setSubjects(new HashSet<>());
        assertThat(student.getSubjects()).doesNotContain(subjectBack);
    }
}
