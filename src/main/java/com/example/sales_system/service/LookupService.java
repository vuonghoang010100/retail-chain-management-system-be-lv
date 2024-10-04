package com.example.sales_system.service;

import com.example.sales_system.entity.master.Lookup;
import com.example.sales_system.repository.master.LookupRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LookupService {
    LookupRepository lookupRepository;

    public void initTestSchema() {
        Lookup lookup = Lookup.builder()
                .name("test")
                .schema("test")
                .build();

        lookupRepository.save(lookup);
    }

    public String getSchema(String tenant) {
        return lookupRepository.findLookupByName(tenant).getSchema();
    }

    public Lookup createLookup(String name, String schema) {Lookup lookup = Lookup.builder()
            .name(name)
            .schema(schema)
            .build();

        return lookupRepository.save(lookup);
    }
}
