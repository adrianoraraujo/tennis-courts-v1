package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.model.CreateGuestDTO;
import com.tenniscourts.guests.model.GuestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO saveGuest(CreateGuestDTO createGuestDTO) {
        return guestMapper.map(guestRepository.save(guestMapper.map(createGuestDTO)));
    }

    public GuestDTO findGuestById(Long id) {
        return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found");
        });
    }

    public List<GuestDTO> findByGuestName(String guestName) {
       var guests =  guestRepository.findByNameIgnoreCaseContaining(guestName).stream()
                .map(guestMapper::map).collect(Collectors.toList());
       if(guests.isEmpty()){
           throw new EntityNotFoundException("No one guests found");
       }
       return guests;
    }

    public List<GuestDTO> findAllGuests() {
        return guestRepository.findAll().stream().map(guestMapper::map).collect(Collectors.toList());
    }

    public GuestDTO updateGuest(Long guestId, CreateGuestDTO createGuestDTO) {
        var guest = this.findGuestById(guestId);
        guest.setName(createGuestDTO.getName());
        return guestMapper.map(guestRepository.save(guestMapper.map(guest)));
    }

    public void deleteGuest(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new EntityNotFoundException("Guest not exist");
        }
        guestRepository.deleteById(guestId);
    }
}
