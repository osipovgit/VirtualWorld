package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.SimulationSession;
import com.osipov_evgeny.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface SimulationSessionRepository extends JpaRepository<SimulationSession, Long> {
    SimulationSession findSimulationSessionByOwner(User owner);

    @Modifying
    @Transactional
    void deleteSimulationSessionById(Long id);
}
