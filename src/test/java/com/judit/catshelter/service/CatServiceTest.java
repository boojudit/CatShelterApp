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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {
    @Mock
    private CatRepository catRepository;
    @InjectMocks
    CatService catService;

    @Test
    void shouldCreateCatWithDefaultValues() {
        CreateCatRequest catRequest = new CreateCatRequest("Test Cat");

        Cat savedCat = Cat.builder()
                .id(1L)
                .name("Test Cat")
                .friendship(0)
                .mood(CatMood.HUNGRY)
                .adoptionStatus(AdoptionStatus.IN_SHELTER)
                .build();

        when(catRepository.save(any(Cat.class)))
                .thenReturn(savedCat);

        CatResponse catResponse = catService.createCat(catRequest);

        assertEquals("Test Cat", catResponse.name());
        assertEquals(1L, catResponse.id());
        assertEquals(CatMood.HUNGRY, catResponse.mood());
        assertEquals(0, catResponse.friendship());
        assertEquals(AdoptionStatus.IN_SHELTER, catResponse.adoptionStatus());
    }

    @Test
    void shouldAdoptCatWhenFriendshipThresholdReached() {
        Cat cat = Cat.builder()
                .id(1L)
                .name("Test Cat")
                .friendship(80)
                .mood(CatMood.HAPPY)
                .adoptionStatus(AdoptionStatus.IN_SHELTER)
                .build();

        when(catRepository.findById(1L))
                .thenReturn(java.util.Optional.of(cat));

        when(catRepository.save(any(Cat.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CatResponse catResponse = catService.adoptCat(1L);
        assertEquals(AdoptionStatus.ADOPTED, catResponse.adoptionStatus());
        assertFalse(catResponse.canBeAdopted());
    }

    @Test
    void shouldReturnOnlyCatsWithGivenAdoptionStatus() {
        Cat cat = Cat.builder()
                .id(1L)
                .name("Test Cat")
                .friendship(50)
                .adoptionStatus(AdoptionStatus.ADOPTED)
                .build();

        when(catRepository.findByAdoptionStatus(AdoptionStatus.ADOPTED))
                .thenReturn(List.of(cat));

        List<CatResponse> cats = catService.getCats(AdoptionStatus.ADOPTED);

        assertEquals(1, cats.size());
        assertEquals(AdoptionStatus.ADOPTED, cats.getFirst().adoptionStatus());
        assertFalse(cats.getFirst().canBeAdopted());

        verify(catRepository).findByAdoptionStatus(AdoptionStatus.ADOPTED);

    }

    @Test
    void shouldThrowWhenCatNotFound() {
        when(catRepository.findById(1L))
                .thenReturn(Optional.empty());

        CatNotFoundException exception =
                assertThrows(
                        CatNotFoundException.class,
                        () -> catService.adoptCat(1L)
                );

        assertEquals("Could not find cat with id 1", exception.getMessage());
    }

    @Test
    void shouldThrowWhenCatNotReadyForAdoption() {
        Cat cat = Cat.builder()
                .id(1L)
                .friendship(50)
                .adoptionStatus(AdoptionStatus.IN_SHELTER)
                .build();

        when(catRepository.findById(1L))
                .thenReturn(Optional.of(cat));

        assertThrows(
                CatNotReadyForAdoptionException.class,
                () -> catService.adoptCat(1L)
        );
    }

    @Test
    void shouldThrowWhenCatAlreadyAdopted() {
        Cat cat = Cat.builder()
                .id(1L)
                .friendship(80)
                .adoptionStatus(AdoptionStatus.ADOPTED)
                .build();

        when(catRepository.findById(1L))
                .thenReturn(java.util.Optional.of(cat));

        assertThrows(
                CatAlreadyAdoptedException.class,
                () -> catService.adoptCat(1L)
        );
    }

    @Test
    void shouldThrowCooldownExceptionWhenFeedingTooSoon() {
        Cat cat = Cat.builder()
                .id(1L)
                .name("Test Cat")
                .friendship(10)
                .mood(CatMood.HAPPY)
                .adoptionStatus(AdoptionStatus.IN_SHELTER)
                .lastFedAt(java.time.Instant.now())
                .build();

        when(catRepository.findById(1L))
                .thenReturn(java.util.Optional.of(cat));

        CatInteractionRequest request = new CatInteractionRequest(CatInteractionType.FEED);

        assertThrows(
                CooldownException.class,
                () -> catService.interactWithCat(1L, request)
        );
    }

}
