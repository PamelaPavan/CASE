package com.school.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjects")
    @JsonIgnoreProperties(value = { "subjects" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjects")
    @JsonIgnoreProperties(value = { "subjects" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Subject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Subject description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.removeSubjects(this));
        }
        if (teachers != null) {
            teachers.forEach(i -> i.addSubjects(this));
        }
        this.teachers = teachers;
    }

    public Subject teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public Subject addTeachers(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getSubjects().add(this);
        return this;
    }

    public Subject removeTeachers(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getSubjects().remove(this);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeSubjects(this));
        }
        if (students != null) {
            students.forEach(i -> i.addSubjects(this));
        }
        this.students = students;
    }

    public Subject students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Subject addStudents(Student student) {
        this.students.add(student);
        student.getSubjects().add(this);
        return this;
    }

    public Subject removeStudents(Student student) {
        this.students.remove(student);
        student.getSubjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return getId() != null && getId().equals(((Subject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
