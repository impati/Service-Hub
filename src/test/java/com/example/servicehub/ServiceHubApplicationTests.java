package com.example.servicehub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.event.TransactionalEventListener;

@ActiveProfiles("local")
@SpringBootTest
class ServiceHubApplicationTests {

    @Test
    void contextLoads() {
    }

}
