package com.gamaapp.domain;

import static com.gamaapp.domain.SubjectTestSamples.*;
import static com.gamaapp.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gamaapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = getTeacherSample1();
        Teacher teacher2 = new Teacher();
        assertThat(teacher1).isNotEqualTo(teacher2);

        teacher2.setId(teacher1.getId());
        assertThat(teacher1).isEqualTo(teacher2);

        teacher2 = getTeacherSample2();
        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    void subjectsTest() {
        Teacher teacher = getTeacherRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        teacher.addSubjects(subjectBack);
        assertThat(teacher.getSubjects()).containsOnly(subjectBack);

        teacher.removeSubjects(subjectBack);
        assertThat(teacher.getSubjects()).doesNotContain(subjectBack);

        teacher.subjects(new HashSet<>(Set.of(subjectBack)));
        assertThat(teacher.getSubjects()).containsOnly(subjectBack);

        teacher.setSubjects(new HashSet<>());
        assertThat(teacher.getSubjects()).doesNotContain(subjectBack);
    }
}
