package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
    @Override
    PlayerCharacter getOne(Long id);

    List<PlayerCharacter> findAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update players_character set age = age + 1 where user_id =:user_id")
    void becomeAYearOlder(@Param("user_id") Long userId);

    @Modifying
    @Transactional
    void deleteAllByUserId(@Param("user_id") Long userId);

    @Modifying
    @Transactional
    @Override
    void delete(PlayerCharacter entity);
}
