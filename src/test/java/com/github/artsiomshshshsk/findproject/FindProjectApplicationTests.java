package com.github.artsiomshshshsk.findproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles(profiles = "test")
@SpringBootTest
class FindProjectApplicationTests {

    @Test
    void contextLoads() {

    }

}
