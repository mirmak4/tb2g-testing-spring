package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;


@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    private OwnerController ownerController;
    @Autowired
    // mocks:
    private ClinicService clinicService;
    private BindingResult bindResult;
    // helpers:
    private MockMvc ownerMockMvc;
    private Map<String, Object> model;
    private Collection<Owner> multiResults;
    private Collection<Owner> singleResult;
    private Owner jungingen;

    @BeforeEach
    public void setup() {
        model = new HashMap<>();
        ownerMockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
        bindResult = Mockito.mock(BindingResult.class);
        // multiple results
        Owner zbyszko = new Owner();
        zbyszko.setFirstName("Zbyszko");
        zbyszko.setFirstName("Ze Bogdańca");
        Owner jurand = new Owner();
        jurand.setFirstName("Jurand");
        jurand.setFirstName("Ze Spychowa");
        multiResults = new ArrayList<>();
        multiResults.add(zbyszko);
        multiResults.add(jurand);
        // juginhen
        jungingen = new Owner();
        jungingen.setFirstName("Ulryk");
        jungingen.setLastName("Von Jungingen");
        jungingen.setId(1410);
        // single results
        singleResult = new ArrayList<>();
        singleResult.add(jungingen);
    }
    
    @Test
    public void testInitCreationForm() throws Exception {
        // given
        String expectedView = "owners/createOrUpdateOwnerForm";
        // when
        String view = ownerController.initCreationForm(model);
        // then
        ownerMockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedView))
                .andExpect(model().attributeExists("owner"));
    }
    
    @Test
    public void testProcessFindFormNullLastName() throws Exception {
        // given
        Owner owner = new Owner();
        Collection<Owner> emptyResults = new ArrayList<>();
        given(clinicService.findOwnerByLastName(ArgumentMatchers.anyString()))
                .willReturn(emptyResults);
        // when
        ownerController.processFindForm(owner, bindResult, model);
        // then
        ownerMockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
        then(bindResult).should().rejectValue(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testProcessFindFormWithMultipleResults() throws Exception {
        // given
        given(clinicService.findOwnerByLastName(ArgumentMatchers.anyString()))
                .willReturn(multiResults);
        // when
        ownerController.processFindForm(jungingen, bindResult, model);
        // then
        ownerMockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));
    }
    
    // return "redirect:/owners/" + owner.getId();
    @Test
    public void testProcessFindFormWithSingleResult() throws Exception {
        // given
        given(clinicService.findOwnerByLastName(ArgumentMatchers.anyString()))
                .willReturn(singleResult);
        // when
        ownerController.processFindForm(jungingen, bindResult, model);
        // then
        ownerMockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1410"));
    }
}