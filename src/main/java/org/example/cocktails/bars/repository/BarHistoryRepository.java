package org.example.cocktails.bars.repository;

import org.example.cocktails.bars.entry.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BarHistoryRepository extends JpaRepository<BarHistory, Long> {
    List<BarHistory> findByBarIdOrderByOccurredAtDescIdDesc(Long barId);
}
