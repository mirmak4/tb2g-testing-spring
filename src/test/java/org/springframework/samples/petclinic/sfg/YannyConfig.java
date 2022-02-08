/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author miron.maksymiuk
 */
@Configuration
public class YannyConfig {
    
    @Bean
    YannyWordProducer yannyWordProducer(){
        return new YannyWordProducer();
    }
}
