package com.gamevault.util;

import com.gamevault.model.Platform;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class PlatformListConverter implements AttributeConverter<Set<Platform>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Platform> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        // Transforme le Set [PC, XBOX_ONE] en une chaîne "PC,XBOX_ONE"
        return attribute.stream()
                .map(Platform::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Platform> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Collections.emptySet();
        }
        // Transforme "PC,XBOX_ONE" en un Set [Platform.PC, Platform.XBOX_ONE]
        return Arrays.stream(dbData.split(","))
                .map(Platform::valueOf)
                .collect(Collectors.toSet());
    }
}