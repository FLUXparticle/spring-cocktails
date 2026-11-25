package org.example.bars.repository;

import org.example.bars.entry.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BarRepository extends JpaRepository<Bar, Long> {
    List<Bar> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String name, String email, String address);
}
