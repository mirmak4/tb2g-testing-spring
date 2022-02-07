/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;

/**
 *
 * @author miron.maksymiuk
 */
public class ClinicServiceImplTest {
    
    @InjectMocks
    private ClinicServiceImpl clinicService;
    @Mock
    private PetRepository petRepository;
    
    public ClinicServiceImplTest() {
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test of findPetTypes method, of class ClinicServiceImpl.
     */
    @Test
    public void testFindPetTypes() {
        // given injected service and repository as private fields
        List<PetType> petTypes = new ArrayList<>();
        PetType tiger = new PetType();
        tiger.setId(1);
        tiger.setName("Tiger");
        PetType lion = new PetType();
        tiger.setId(2);
        tiger.setName("Lion");
        petTypes.add(tiger);
        petTypes.add(lion);
        // when
        when(petRepository.findPetTypes()).thenReturn(petTypes);
        Collection<PetType> returned = clinicService.findPetTypes();
        // then
        verify(petRepository).findPetTypes();
        Assertions.assertThat(returned).size().isEqualTo(2);
        Assertions.assertThat(returned).contains(lion);
        Assertions.assertThat(returned).contains(tiger);
    }
    
}
