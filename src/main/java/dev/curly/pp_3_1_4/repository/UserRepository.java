package dev.curly.pp_3_1_4.repository;

import dev.curly.pp_3_1_4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    @Query(
        "SELECT CASE WHEN (count(u) > 0) THEN true ELSE false END " +
            "FROM User u join Role r on r.name = :roleName"
    )
    boolean existsWithRole(@Param("roleName") String roleName);
}
