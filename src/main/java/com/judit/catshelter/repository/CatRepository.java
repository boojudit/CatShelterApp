package com.judit.catshelter.repository;

import com.judit.catshelter.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
