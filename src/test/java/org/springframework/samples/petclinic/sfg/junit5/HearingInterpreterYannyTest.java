/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.springframework.samples.petclinic.sfg.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.sfg.BaseConfig;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.YannyConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *
 * @author miron.maksymiuk
 */
@SpringJUnitConfig(classes = {BaseConfig.class, YannyConfig.class})
public class HearingInterpreterYannyTest {
    
    @Autowired
    private HearingInterpreter hearingInterpreter;
    
    public HearingInterpreterYannyTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }

    /**
     * Test of whatIheard method, of class HearingInterpreter.
     */
    @Test
    public void testWhatIheard() {
        String word = hearingInterpreter.whatIheard();

        assertEquals("Yanny", word);
    }
    
}
