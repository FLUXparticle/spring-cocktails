package org.example.cocktails.bars.repository;

import org.example.cocktails.bars.entry.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BarRepository extends JpaRepository<Bar, Long> {
    // TODO: Aufgabe - Paging: Die Suchmethode auf Page<Bar> und Pageable umstellen.
    List<Bar> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String name, String email, String address, Sort sort);
}
