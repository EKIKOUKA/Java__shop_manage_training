package com.shop.sample.repository;

import com.shop.sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    Optional<User> findByUserEmail(String userEmail);

    @Query(
        value = """
            SELECT avatar, create_time, gender, is_active, update_time, user_edu, user_email, user_id, user_tel, username
            FROM sp_user
            WHERE 1 = 1
              AND (:username IS NULL OR username LIKE %:username%)
              AND (:gender IS NULL OR gender = :gender)
              AND (:userEdu IS NULL OR user_edu = :userEdu)
              AND (:userEmail IS NULL OR user_email LIKE %:userEmail%)
              AND is_active = :isActive
            ORDER BY user_id DESC
            LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    List<Map<String, Object>> findUsersWithFilters(
        @Param("username") String username,
        @Param("gender") String gender,
        @Param("userEdu") Integer userEdu,
        @Param("userEmail") String userEmail,
        @Param("isActive") Integer isActive,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

    @Query(
        value = """
            SELECT COUNT(*) FROM sp_user
            WHERE 1 = 1
              AND (:username IS NULL OR username LIKE %:username%)
              AND (:gender IS NULL OR gender = :gender)
              AND (:userEdu IS NULL OR user_edu = :userEdu)
              AND (:userEmail IS NULL OR user_email LIKE %:userEmail%)
              AND is_active = :isActive
            """,
        nativeQuery = true
    )
    int countUsersWithFilters(
        @Param("username") String username,
        @Param("gender") String gender,
        @Param("userEdu") Integer userEdu,
        @Param("userEmail") String userEmail,
        @Param("isActive") Integer isActive
    );
}
