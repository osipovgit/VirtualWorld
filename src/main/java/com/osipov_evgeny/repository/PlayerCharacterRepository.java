package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.PlayerCharacter;
import com.osipov_evgeny.entity.SimulationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
    @Override
    PlayerCharacter getOne(Long id);

    List<PlayerCharacter> findAllBySimulationSessionId(SimulationSession simulationSessionId);

    PlayerCharacter findBySimulationSessionIdAndSerialNumber(SimulationSession simulationSessionId, Long serialNumber);

    PlayerCharacter findBySimulationSessionIdAndIdMarriage(SimulationSession simulationSessionId, Integer idMarriage);

    @Modifying
    @Transactional
    void deleteAllBySimulationSessionId(@Param("simulation_session_id") SimulationSession simulation_session_id);

    @Modifying
    @Transactional
    void deleteBySimulationSessionIdAndSerialNumber(SimulationSession simulationSessionId, Long serialNumber);

    @Modifying
    @Transactional
    void deletePlayerCharacterById(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from player_characters where simulation_session_id=:simulation_session_id and id=:id")
    void deleteBySimulationSessionIdAndId(@Param("simulation_session_id") SimulationSession simulation_session_id, @Param("id") Long id);
}
