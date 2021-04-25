package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.SessionNotification;
import com.osipov_evgeny.entity.SimulationSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionNotificationRepository extends JpaRepository<SessionNotification, Long> {

    List<SessionNotification> findAllBySessionId(SimulationSession sessionId);
}
