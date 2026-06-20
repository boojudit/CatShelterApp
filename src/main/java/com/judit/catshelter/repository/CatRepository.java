package com.judit.catshelter.repository;

import com.judit.catshelter.model.AdoptionStatus;
import com.judit.catshelter.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByAdoptionStatus(AdoptionStatus adoptionStatus);
}

