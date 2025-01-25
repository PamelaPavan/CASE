package com.school.repository;

import com.school.domain.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TeacherRepositoryWithBagRelationships {
    Optional<Teacher> fetchBagRelationships(Optional<Teacher> teacher);

    List<Teacher> fetchBagRelationships(List<Teacher> teachers);

    Page<Teacher> fetchBagRelationships(Page<Teacher> teachers);
}
