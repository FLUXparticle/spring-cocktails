package org.example.cocktails.recipes.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MilkServiceTest {
    @Autowired MilkService service;
    private long start;

    @BeforeEach
    void startClock() {
        start = System.nanoTime();
    }

    @AfterEach
    void showDuration(TestInfo test) {
        System.out.printf("%s: %.2f Sekunden%n", test.getDisplayName(),
                (System.nanoTime() - start) / 1_000_000_000.0);
    }

    @Test
    @Order(1)
    @DisplayName("Sequenziell – ungefähr 4 Sekunden")
    void sequential() {
        assertEquals(38, service.sequential());
    }

    @Test
    @Order(2)
    @DisplayName("Parallel – ungefähr 2 Sekunden")
    void parallel() {
        assertEquals(38, service.parallel());
    }
}
