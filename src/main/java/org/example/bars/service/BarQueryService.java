package org.example.bars.service;

import org.example.bars.entry.*;
import org.example.bars.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BarQueryService {
    private final BarRepository repository;

    public BarQueryService(BarRepository repository) {
        this.repository = repository;
    }

    public List<Bar> find(String search) {
        String term = search.trim();
        return term.isEmpty() ? repository.findAll()
                : repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrAddressContainingIgnoreCase(term, term, term);
    }
}
