package org.example.cocktails.recipes.repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FridgeStore {
    private final Set<Long> stock = new HashSet<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Set<Long> ingredients() {
        lock.readLock().lock();
        try {
            return Set.copyOf(stock);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setIngredient(Long id, boolean present) {
        lock.writeLock().lock();
        try {
            if (present) stock.add(id);
            else stock.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
