package com.tenniscourts.tenniscourts;

import com.tenniscourts.tenniscourts.model.TennisCourt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TennisCourtRepository extends JpaRepository<TennisCourt, Long> {
}
