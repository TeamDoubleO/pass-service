package com.doubleo.passservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
class PassServiceApplicationTests {

    @Test
    void contextLoads() {}
}
