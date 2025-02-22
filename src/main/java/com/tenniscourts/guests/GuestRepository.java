package com.tenniscourts.guests;

import com.tenniscourts.guests.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest,Long> {

    List<Guest> findByNameIgnoreCaseContaining(String name);
}
