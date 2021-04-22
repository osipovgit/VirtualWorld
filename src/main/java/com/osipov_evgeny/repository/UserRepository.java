package com.osipov_evgeny.repository;

import com.osipov_evgeny.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    @Override
    List<User> findAll();

    @Query(nativeQuery = true, value = "select * from users order by longest_game desc")
    List<User> selectTopByLongestGame();

//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true, value = "update users set count_games =:countGames where id =:id")
//    void updateCountGamesById(@Param("id") Long id, @Param("countGames") Integer countGames);
//
//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true, value = "update users set longest_game =:longest_game where id =:id")
//    void updateLongestGameById(@Param("id") Long id, @Param("longest_game") Integer longestGame);
}
