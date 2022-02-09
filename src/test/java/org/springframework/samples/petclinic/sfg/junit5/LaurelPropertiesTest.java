/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *
 * @author miron.maksymiuk
 */
@TestPropertySource("classpath:laurel.properties")
@ActiveProfiles("externalized")
@SpringJUnitConfig(classes = {LaurelPropertiesTest.TestConfig.class})
public class LaurelPropertiesTest {
    
    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig {}
    
    @Autowired
    private HearingInterpreter hearingInterpreter;
    
    @Test
    public void test() {
        String word = hearingInterpreter.whatIheard();
        
        assertEquals("LaUrEl", word);
    }
}
