/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.springframework.samples.petclinic.sfg;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author miron.maksymiuk
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BaseConfig.class, YannyConfig.class})
public class HearingInterpreterYannyTest {
    
    @Autowired
    private HearingInterpreter hearingInterpreter;
    
    public HearingInterpreterYannyTest() {
    }
    
    @Before
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
