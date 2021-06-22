package com.osipov.evgeny.repository;

import com.osipov.evgeny.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    @Override
    List<User> findAll();

    @Query(nativeQuery = true, value = "select * from users order by longest_game desc")
    List<User> selectTopByLongestGame();

}
