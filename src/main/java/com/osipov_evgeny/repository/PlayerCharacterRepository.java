package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.InnateTalent;
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

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update player_characters set age = age + 1 where simulation_session_id =:simulation_session_id")
    void becomeAYearOlder(@Param("simulation_session_id") SimulationSession simulationSessionId);

    @Modifying
    @Transactional
    void deleteAllBySimulationSessionId(@Param("simulation_session_id") SimulationSession simulation_session_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from player_characters where profession = :profession and simulation_session_id =:simulationSessionId")
    void deleteAllByProfessionAndSessionId(@Param("profession") String profession, @Param("simulationSessionId") Long simulationSessionId);
}
