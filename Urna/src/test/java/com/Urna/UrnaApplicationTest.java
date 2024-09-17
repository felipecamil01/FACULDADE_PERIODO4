package com.Urna;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UrnaApplicationTest {

    @Test
    public void contextLoads() {
    	
    }
    
    @Test
    public void testMain() {
        UrnaApplication.main(new String[]{});
    }
}
