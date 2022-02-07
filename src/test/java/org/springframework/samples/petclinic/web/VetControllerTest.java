/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 *
 * @author miron.maksymiuk
 */
public class VetControllerTest {
    
    public VetControllerTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of showVetList method, of class VetController.
     */
    @Test
    public void testShowVetList() {
        // given
        ClinicService clinicService = Mockito.mock(ClinicService.class);
        VetController vetController = new VetController(clinicService);
        Vets vets = new Vets();
        List<Vet> vetList = new ArrayList<>();
        Vet vet21 = new Vet();
        vet21.setId(21);
        vet21.setFirstName("Mati");
        vet21.setLastName("Maks");
        Vet vet12 = new Vet();
        vet12.setId(12);
        vet12.setFirstName("Andy");
        vet12.setLastName("SineD");
        vetList.add(vet12);
        vetList.add(vet21);
        Map<String, Object> model = new HashMap<>();
        // when
        when(clinicService.findVets()).thenReturn(vetList);
        String returnedView = vetController.showVetList(model);
        // then
        verify(clinicService).findVets();
        assertThat(returnedView).isEqualToIgnoringCase("vets/vetList");
        assertThat(model).containsKey("vets");
        Vets savedVets = (Vets) model.get("vets");
        assertThat(savedVets.getVetList()).size().isEqualTo(2);
        assertThat(savedVets.getVetList()).contains(vet12);
        assertThat(savedVets.getVetList()).contains(vet21);
    }

    /**
     * Test of showResourcesVetList method, of class VetController.
     */
    @Test
    public void testShowResourcesVetList() {
        // given
        ClinicService clinicService = Mockito.mock(ClinicService.class);
        VetController vetController = new VetController(clinicService);
        List<Vet> vetList = new ArrayList<>();
        Vet vet21 = new Vet();
        vet21.setId(21);
        vet21.setFirstName("Mati");
        vet21.setLastName("Maks");
        Vet vet12 = new Vet();
        vet12.setId(12);
        vet12.setFirstName("Andy");
        vet12.setLastName("SineD");
        vetList.add(vet12);
        vetList.add(vet21);
        // when
        when(clinicService.findVets()).thenReturn(vetList);
        Vets returnedVets = vetController.showResourcesVetList();
        // then
        verify(clinicService).findVets();
        assertThat(returnedVets.getVetList()).size().isEqualTo(2);
        assertThat(returnedVets.getVetList()).contains(vet12);
        assertThat(returnedVets.getVetList()).contains(vet21);
    }
    
}
