package com.complexschoolapp.web.rest;

import static com.complexschoolapp.domain.TeacherAsserts.*;
import static com.complexschoolapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.complexschoolapp.IntegrationTest;
import com.complexschoolapp.domain.Teacher;
import com.complexschoolapp.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeacherResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALIZATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIZATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teachers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherRepository teacherRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    private Teacher insertedTeacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity() {
        return new Teacher().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME).specialization(DEFAULT_SPECIALIZATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity() {
        return new Teacher().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).specialization(UPDATED_SPECIALIZATION);
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTeacher != null) {
            teacherRepository.delete(insertedTeacher);
            insertedTeacher = null;
        }
    }

    @Test
    @Transactional
    void createTeacher() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Teacher
        var returnedTeacher = om.readValue(
            restTeacherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Teacher.class
        );

        // Validate the Teacher in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTeacherUpdatableFieldsEquals(returnedTeacher, getPersistedTeacher(returnedTeacher));

        insertedTeacher = returnedTeacher;
    }

    @Test
    @Transactional
    void createTeacherWithExistingId() throws Exception {
        // Create the Teacher with an existing ID
        teacher.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teacher.setFirstName(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teacher.setLastName(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecializationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teacher.setSpecialization(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeachers() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teacherRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teacherRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teacherRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(teacherRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL_ID, teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION));
    }

    @Test
    @Transactional
    void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).specialization(UPDATED_SPECIALIZATION);

        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeacherToMatchAllProperties(updatedTeacher);
    }

    @Test
    @Transactional
    void putNonExistingTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(put(ENTITY_API_URL_ID, teacher.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.firstName(UPDATED_FIRST_NAME);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTeacher, teacher), getPersistedTeacher(teacher));
    }

    @Test
    @Transactional
    void fullUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).specialization(UPDATED_SPECIALIZATION);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherUpdatableFieldsEquals(partialUpdatedTeacher, getPersistedTeacher(partialUpdatedTeacher));
    }

    @Test
    @Transactional
    void patchNonExistingTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacher.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teacher
        restTeacherMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teacherRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Teacher getPersistedTeacher(Teacher teacher) {
        return teacherRepository.findById(teacher.getId()).orElseThrow();
    }

    protected void assertPersistedTeacherToMatchAllProperties(Teacher expectedTeacher) {
        assertTeacherAllPropertiesEquals(expectedTeacher, getPersistedTeacher(expectedTeacher));
    }

    protected void assertPersistedTeacherToMatchUpdatableProperties(Teacher expectedTeacher) {
        assertTeacherAllUpdatablePropertiesEquals(expectedTeacher, getPersistedTeacher(expectedTeacher));
    }
}
