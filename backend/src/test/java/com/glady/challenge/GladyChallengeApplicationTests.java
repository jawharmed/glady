package com.glady.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GladyChallengeApplicationTests {

    @Test
    void justSimpleTest() {
        GladyChallengeApplication application = new GladyChallengeApplication();
        String result = application.getClass().getSimpleName();

        assertEquals("GladyChallengeApplication", result);
    }
}
