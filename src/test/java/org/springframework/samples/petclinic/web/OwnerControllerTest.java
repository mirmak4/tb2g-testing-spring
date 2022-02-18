package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.validation.BindingResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
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
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    // match strings for returned views
    private static final String createOrUpdateOwnerView = 
            "owners/createOrUpdateOwnerForm";
    private static final String ownersListView = 
            "owners/ownersList";
    private static final String redirectOwnerView = 
            "redirect:/owners/%d";
    private static final String redirectOwnerUriParamView = 
            "redirect:/owners/%s";
    private static final String findOwnersView = 
            "owners/findOwners";
    

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

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }


    @Test
    void testNewOwnerPostValid() throws Exception {
        ownerMockMvc.perform(post("/owners/new")
                    .param("firstName", "Jimmy")
                    .param("lastName", "Buffett")
                    .param("address", "123 Duval St ")
                    .param("city", "Key West")
                    .param("telephone", "3151231234"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        ownerMockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("city", "Key West"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("owner"))
            .andExpect(model().attributeHasFieldErrors("owner", "address"))
            .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
            .andExpect(view().name(createOrUpdateOwnerView));
    }

    @Test
    void testReturnListOfOwners() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));

        ownerMockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name(ownersListView));

        then(clinicService).should().findOwnerByLastName(stringArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("");
    }

    @Test
    void testFindOwnerOneResult() throws Exception {
        Owner justOne = new Owner();
        justOne.setId(1);
        final String findJustOne = "FindJustOne";

        justOne.setLastName(findJustOne);

        given(clinicService.findOwnerByLastName(findJustOne)).willReturn(Lists.newArrayList(justOne));

        ownerMockMvc.perform(get("/owners")
                    .param("lastName", findJustOne))
                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(redirectOwnerView.formatted(justOne.getId())));
                .andExpect(view().name(String.format(redirectOwnerView, justOne.getId() )));

        then(clinicService).should().findOwnerByLastName(anyString());

    }

    @Test
    void testFindByNameNotFound() throws Exception {
        ownerMockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name(findOwnersView));
    }

    @Test
    void initCreationFormTest() throws Exception {
        ownerMockMvc.perform(get("/owners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name(createOrUpdateOwnerView));
    }
    
    public void testInitCreationForm() throws Exception {
        // given
        // when
        String view = ownerController.initCreationForm(model);
        // then
        ownerMockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(createOrUpdateOwnerView))
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
                .andExpect(view().name(findOwnersView));
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
                .andExpect(view().name(ownersListView));
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
//                .andExpect(view().name(redirectOwnerView.formatted(1410)));
                .andExpect(view().name(String.format(redirectOwnerView, 1410 )));
    }
    
    // processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") 1410) {
    @Test
    public void testProcessUpdateOwnerFormValid() throws Exception {
        ownerMockMvc.perform(post("/owners/{ownerId}/edit", 1410)
                .param("firstName", "Ulryk")
                .param("lastName", "Von Jungingen")
                .param("city", "Koszaolin")
                .param("telephone", "943457200")
                .param("address", "Śniadeckich 2"))
                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(redirectOwnerUriParamView.formatted("{ownerId}")));
                .andExpect(view().name(String.format(redirectOwnerUriParamView, "{ownerId}" )));
        
        verify(clinicService).saveOwner(any(Owner.class));
    }
    
    @Test
    public void testProcessUpdateOwnerFormNotValid() throws Exception {
        ownerMockMvc.perform(post("/owners/{ownerId}/edit", 1410)
                .param("firstName", "Ulryk")
                .param("lastName", "Von Jungingen")
                .param("city", "Koszaolin")
                // no required param telephone 
                .param("address", "Śniadeckich 2"))
                .andExpect(status().isOk())
                .andExpect(view().name(createOrUpdateOwnerView));
        verify(clinicService, never()).saveOwner(any(Owner.class));
    }
}
