package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.SimulationSession;
import com.osipov_evgeny.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulationSessionRepository extends JpaRepository<SimulationSession, Long> {
    SimulationSession findSimulationSessionByOwner(User owner);

}
