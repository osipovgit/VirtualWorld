package com.osipov.evgeny.repository;

import com.osipov.evgeny.entity.SessionNotification;
import com.osipov.evgeny.entity.SimulationSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionNotificationRepository extends JpaRepository<SessionNotification, Long> {

    List<SessionNotification> findAllBySessionId(SimulationSession sessionId);

}
