package com.judit.catshelter.dto;

import com.judit.catshelter.model.AdoptionStatus;
import com.judit.catshelter.model.CatMood;

public record CatResponse (
        Long id,
        String name,
        int friendship,
        CatMood mood,
        AdoptionStatus adoptionStatus,
        boolean canBeAdopted
){
}
