package org.example.cocktails.recipes.repository;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;

class FridgeStoreTest {
    @Test void concurrentWritersAndReadersDoNotCorruptTheStock() throws Exception {
        var store = new FridgeStore();
        store.setIngredient(54L, true);
        var start = new CyclicBarrier(3);
        int count = 100_000;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            var add = executor.submit(() -> {
                start.await();
                for (int i = 0; i < count; i++) store.setIngredient(1L + i % 53, true);
                return null;
            });
            var remove = executor.submit(() -> {
                start.await();
                for (int i = 0; i < count; i++) store.setIngredient(1L + i % 53, false);
                return null;
            });
            var read = executor.submit(() -> {
                start.await();
                for (int i = 0; i < count; i++) {
                    assertThat(store.ingredients()).contains(54L).allMatch(id -> id >= 1 && id <= 54);
                }
                return null;
            });
            add.get(10, TimeUnit.SECONDS); remove.get(10, TimeUnit.SECONDS); read.get(10, TimeUnit.SECONDS);
            // Do not assume which writer finished last. Set a known final state.
            for (long id = 1; id <= 53; id++) store.setIngredient(id, false);
            store.setIngredient(1L, true);
            assertThat(store.ingredients()).containsExactlyInAnyOrder(1L, 54L);
        } finally { executor.shutdownNow(); }
    }
}
