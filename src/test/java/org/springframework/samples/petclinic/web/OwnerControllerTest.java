package org.springframework.samples.petclinic.web;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    private OwnerController ownerController;
    @Autowired
    private ClinicService clinicService;
    private Map<String, Object> model;
    private MockMvc ownerMockMvc;

    @BeforeEach
    public void setup() {
        model = new HashMap<>();
        ownerMockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }
    
    @Test
    public void testInitCreationForm() throws Exception {
        // given
        String expectedView = "owners/createOrUpdateOwnerForm";
        // when
        String view = ownerController.initCreationForm(model);
        // then
        ownerMockMvc.perform(MockMvcRequestBuilders.get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedView))
                .andExpect(model().attributeExists("owner"));
    }
}