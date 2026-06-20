package com.judit.catshelter.controller;

import com.judit.catshelter.dto.CatInteractionRequest;
import com.judit.catshelter.dto.CatResponse;
import com.judit.catshelter.dto.CreateCatRequest;
import com.judit.catshelter.model.Cat;
import com.judit.catshelter.service.CatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
@AllArgsConstructor
public class CatController {
    private final CatService catService;

    @GetMapping
    public List<CatResponse> getAllCats() {
        return catService.getAllCats();
    }

    @PostMapping
    public CatResponse createCat(@Valid @RequestBody CreateCatRequest request) {
        return catService.createCat(request);
    }

    @PostMapping("/{id}/interactions")
    public CatResponse interactWithCat(
            @PathVariable long id,
            @Valid @RequestBody CatInteractionRequest request
    ) {
        return catService.interactWithCat(id, request);
    }

    @PostMapping("/{id}/adopt")
    public CatResponse adoptCat(@PathVariable long id) {
        return catService.adoptCat(id);
    }
}
