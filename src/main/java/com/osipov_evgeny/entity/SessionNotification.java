package com.osipov_evgeny.entity;

import javax.persistence.*;

@Entity
@Table(name = "session_notifications")
public class SessionNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sessionId")
    private SimulationSession sessionId;
    private String notification;

    public SessionNotification() {
    }

    public SessionNotification(SimulationSession sessionId, String notification) {
        this.sessionId = sessionId;
        this.notification = notification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimulationSession getSessionId() {
        return sessionId;
    }

    public void setSessionId(SimulationSession sessionId) {
        this.sessionId = sessionId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "SessionNotification{" +
                "sessionId=" + sessionId +
                ", notification='" + notification + '\'' +
                '}';
    }
}
