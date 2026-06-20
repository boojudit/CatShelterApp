package com.judit.catshelter.service;

import com.judit.catshelter.dto.CatInteractionRequest;
import com.judit.catshelter.dto.CatResponse;
import com.judit.catshelter.dto.CreateCatRequest;
import com.judit.catshelter.exception.CatAlreadyAdoptedException;
import com.judit.catshelter.exception.CatNotFoundException;
import com.judit.catshelter.exception.CatNotReadyForAdoptionException;
import com.judit.catshelter.exception.CooldownException;
import com.judit.catshelter.model.AdoptionStatus;
import com.judit.catshelter.model.Cat;
import com.judit.catshelter.model.CatInteractionType;
import com.judit.catshelter.model.CatMood;
import com.judit.catshelter.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatService {
    private static final int ADOPTION_FRIENDSHIP_THRESHOLD = 10;
    private static final Duration FEED_COOLDOWN = Duration.ofSeconds(60);
    private static final Duration PET_COOLDOWN = Duration.ofSeconds(30);
    private static final Duration PLAY_COOLDOWN  = Duration.ofSeconds(90);

    private final CatRepository catRepository;

    public List<CatResponse> getAllCats() {
        return catRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CatResponse createCat(CreateCatRequest request) {
        Cat cat = Cat.builder()
                .name(request.name())
                .friendship(0)
                .mood(CatMood.HUNGRY)
                .adoptionStatus(AdoptionStatus.IN_SHELTER)
                .build();

        Cat savedCat = catRepository.save(cat);

        return toResponse(savedCat);
    }

    public CatResponse interactWithCat(Long id, CatInteractionRequest request) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new CatNotFoundException(id));

        validateCooldown(cat, request.type());

        switch (request.type()) {
            case FEED -> {
                cat.setFriendship(Math.min(100, cat.getFriendship() + 5));
                cat.setMood(CatMood.HAPPY);
                cat.setLastFedAt(Instant.now());
            }
            case PET -> {
                cat.setFriendship(Math.min(100, cat.getFriendship() + 5));
                cat.setMood(CatMood.HAPPY);
                cat.setLastPettedAt(Instant.now());
            }
            case PLAY -> {
                cat.setFriendship(Math.min(100, cat.getFriendship() + 5));
                cat.setMood(CatMood.PLAYFUL);
                cat.setLastPlayedAt(Instant.now());
            }
        }

        Cat savedCat = catRepository.save(cat);
        return toResponse(savedCat);
    }

    public CatResponse adoptCat(Long id) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new CatNotFoundException(id));

        if (cat.getAdoptionStatus() == AdoptionStatus.ADOPTED) {
            throw new CatAlreadyAdoptedException(cat.getName());
        }
        if (cat.getFriendship() < ADOPTION_FRIENDSHIP_THRESHOLD) {
            throw new CatNotReadyForAdoptionException(cat.getName());
        }

        cat.setAdoptionStatus(AdoptionStatus.ADOPTED);

        Cat savedCat = catRepository.save(cat);
        return toResponse(savedCat);
    }

    private CatResponse toResponse(Cat cat) {
        return new CatResponse(
                cat.getId(),
                cat.getName(),
                cat.getFriendship(),
                cat.getMood(),
                cat.getAdoptionStatus(),
                canBeAdopted(cat)
        );
    }

    private boolean canBeAdopted(Cat cat) {
        return cat.getAdoptionStatus() == AdoptionStatus.IN_SHELTER
                && cat.getFriendship() >= ADOPTION_FRIENDSHIP_THRESHOLD;
    }

    private void validateCooldown(Cat cat, CatInteractionType type) {
        switch (type) {
            case FEED -> {
                if(isOnCooldown(cat.getLastFedAt(), FEED_COOLDOWN)) {
                    throw new CooldownException(type);
                }
            }
            case PET -> {
                if(isOnCooldown(cat.getLastPettedAt(), PET_COOLDOWN)) {
                    throw new CooldownException(type);
                }
            }
            case PLAY -> {
                if(isOnCooldown(cat.getLastPlayedAt(), PLAY_COOLDOWN)) {
                    throw new CooldownException(type);
                }
            }

        }
    }

    private boolean isOnCooldown(Instant lastInteraction, Duration cooldown) {
        if (lastInteraction == null) {
            return false;
        }
        Duration timePassed = Duration.between(lastInteraction, Instant.now());
        return timePassed.compareTo(cooldown) < 0;
    }

}
