package com.tenniscourts.reservations.service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.guests.model.Guest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tenniscourts.reservations.stubs.GuestStubs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceTests {

    @Mock
    GuestRepository guestRepository;

    @Mock
    GuestMapper guestMapper;

    @InjectMocks
    GuestService guestService;

    @Test
    void whenSaveShouldReturnTheSavedGuest() {
        var createGuestDTOStub = createGuestDTOStub();
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        when(guestRepository.save(guestStub)).thenReturn(guestStub);
        when(guestMapper.map(createGuestDTOStub)).thenReturn(guestStub);
        when(guestMapper.map(guestStub)).thenReturn(guestDTO);

        var stubActual = guestService.saveGuest(createGuestDTOStub);

        assertEquals(guestDTO, stubActual);
    }

    @Test
    void whenFindGuestByIdShouldReturnAGuest() {
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guestStub));
        when(guestMapper.map(guestStub)).thenReturn(guestDTO);

        var stubActual = guestService.findGuestById(1L);

        assertEquals(guestDTO, stubActual);
    }

    @Test
    void whenFindByIdShouldThrowAnException() {
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        var guestId = 1L;

        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> guestService.findGuestById(guestId));
    }

    @Test
    void whenFindGuestByNameShouldReturnAListOfGuests() {
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        var guestName = "Andrews";

        when(guestRepository.findByNameIgnoreCaseContaining(guestName)).thenReturn(List.of(guestStub));
        when(guestMapper.map(guestStub)).thenReturn(guestDTO);

        var stubActual = guestService.findByGuestName(guestName);

        assertEquals(List.of(guestDTO), stubActual);
    }

    @Test
    void whenFindGuestByNameShouldThrowAnException() {
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        var guestName = "Andrews";

        when(guestRepository.findByNameIgnoreCaseContaining(guestName)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> guestService.findByGuestName(guestName));
    }

    @Test
    void whenFindAllGuestsShouldReturnAListOfGuests() {
        var guestStub = guestStub();
        var guestDTO = guestDTOStub();

        when(guestRepository.findAll()).thenReturn(List.of(guestStub));
        when(guestMapper.map(guestStub)).thenReturn(guestDTO);

        var stubActual = guestService.findAllGuests();

        assertEquals(List.of(guestDTO), stubActual);
    }

    @Test
    void whenUpdateAGuestShouldReturnTheUpdatedGuest() {
        var guestId = 1L;

        var createGuestDTOStub = createGuestDTOStub();
        var guestStub = guestStub();
        var guestDTOStub = guestDTOStub();

        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guestStub));
        when(guestRepository.save(guestStub)).thenReturn(guestStub);
        when(guestMapper.map(guestDTOStub)).thenReturn(guestStub);
        when(guestMapper.map(guestStub)).thenReturn(guestDTOStub);

        var stubActual = guestService.updateGuest(guestId, createGuestDTOStub);

        assertEquals(guestDTOStub, stubActual);
    }

    @Test
    void whenDeleteByIdShouldTouchTheMethod() {
        var guestId = 1L;

        when(guestRepository.existsById(guestId)).thenReturn(true);

        guestService.deleteGuest(guestId);

        verify(guestRepository, times(1)).deleteById(guestId);
    }

    @Test
    void whenDeleteByIdShouldThrowException() {
        var guestId = 1L;

        when(guestRepository.existsById(guestId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> guestService.deleteGuest(guestId));
    }


}
