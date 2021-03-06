package com.engagepoint.msap.web.rest;

import com.engagepoint.msap.Application;
import com.engagepoint.msap.domain.LookupCounty;
import com.engagepoint.msap.repository.LookupCountyRepository;
import com.engagepoint.msap.repository.search.LookupCountySearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LookupCountyResource REST controller.
 *
 * @see LookupCountyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupCountyResourceIntTest {

    private static final String DEFAULT_COUNTY_NAME = "AAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBB";

    private static final Integer DEFAULT_COUNTY_CODE = 1;
    private static final Integer UPDATED_COUNTY_CODE = 2;

    @Inject
    private LookupCountyRepository lookupCountyRepository;

    @Inject
    private LookupCountySearchRepository lookupCountySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLookupCountyMockMvc;

    private LookupCounty lookupCounty;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupCountyResource lookupCountyResource = new LookupCountyResource();
        ReflectionTestUtils.setField(lookupCountyResource, "lookupCountySearchRepository", lookupCountySearchRepository);
        ReflectionTestUtils.setField(lookupCountyResource, "lookupCountyRepository", lookupCountyRepository);
        this.restLookupCountyMockMvc = MockMvcBuilders.standaloneSetup(lookupCountyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lookupCounty = new LookupCounty();
        lookupCounty.setCountyName(DEFAULT_COUNTY_NAME);
        lookupCounty.setCountyCode(DEFAULT_COUNTY_CODE);
    }

    @Test
    @Transactional
    public void createLookupCounty() throws Exception {
        int databaseSizeBeforeCreate = lookupCountyRepository.findAll().size();

        // Create the LookupCounty

        restLookupCountyMockMvc.perform(post("/api/lookupCountys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupCounty)))
                .andExpect(status().isCreated());

        // Validate the LookupCounty in the database
        List<LookupCounty> lookupCountys = lookupCountyRepository.findAll();
        assertThat(lookupCountys).hasSize(databaseSizeBeforeCreate + 1);
        LookupCounty testLookupCounty = lookupCountys.get(lookupCountys.size() - 1);
        assertThat(testLookupCounty.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testLookupCounty.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
    }

    @Test
    @Transactional
    public void checkCountyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupCountyRepository.findAll().size();
        // set the field null
        lookupCounty.setCountyName(null);

        // Create the LookupCounty, which fails.

        restLookupCountyMockMvc.perform(post("/api/lookupCountys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupCounty)))
                .andExpect(status().isBadRequest());

        List<LookupCounty> lookupCountys = lookupCountyRepository.findAll();
        assertThat(lookupCountys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupCountyRepository.findAll().size();
        // set the field null
        lookupCounty.setCountyCode(null);

        // Create the LookupCounty, which fails.

        restLookupCountyMockMvc.perform(post("/api/lookupCountys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupCounty)))
                .andExpect(status().isBadRequest());

        List<LookupCounty> lookupCountys = lookupCountyRepository.findAll();
        assertThat(lookupCountys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLookupCountys() throws Exception {
        // Initialize the database
        lookupCountyRepository.saveAndFlush(lookupCounty);

        // Get all the lookupCountys
        restLookupCountyMockMvc.perform(get("/api/lookupCountys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupCounty.getId().intValue())))
                .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME.toString())))
                .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)));
    }

    @Test
    @Transactional
    public void getLookupCounty() throws Exception {
        // Initialize the database
        lookupCountyRepository.saveAndFlush(lookupCounty);

        // Get the lookupCounty
        restLookupCountyMockMvc.perform(get("/api/lookupCountys/{id}", lookupCounty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupCounty.getId().intValue()))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME.toString()))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingLookupCounty() throws Exception {
        // Get the lookupCounty
        restLookupCountyMockMvc.perform(get("/api/lookupCountys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupCounty() throws Exception {
        // Initialize the database
        lookupCountyRepository.saveAndFlush(lookupCounty);

		int databaseSizeBeforeUpdate = lookupCountyRepository.findAll().size();

        // Update the lookupCounty
        lookupCounty.setCountyName(UPDATED_COUNTY_NAME);
        lookupCounty.setCountyCode(UPDATED_COUNTY_CODE);

        restLookupCountyMockMvc.perform(put("/api/lookupCountys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupCounty)))
                .andExpect(status().isOk());

        // Validate the LookupCounty in the database
        List<LookupCounty> lookupCountys = lookupCountyRepository.findAll();
        assertThat(lookupCountys).hasSize(databaseSizeBeforeUpdate);
        LookupCounty testLookupCounty = lookupCountys.get(lookupCountys.size() - 1);
        assertThat(testLookupCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testLookupCounty.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    public void deleteLookupCounty() throws Exception {
        // Initialize the database
        lookupCountyRepository.saveAndFlush(lookupCounty);

		int databaseSizeBeforeDelete = lookupCountyRepository.findAll().size();

        // Get the lookupCounty
        restLookupCountyMockMvc.perform(delete("/api/lookupCountys/{id}", lookupCounty.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupCounty> lookupCountys = lookupCountyRepository.findAll();
        assertThat(lookupCountys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
