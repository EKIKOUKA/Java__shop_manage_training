package com.shop.sample.repository;

import com.shop.sample.model.UserEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEducationRepository extends JpaRepository<UserEducation, Long> {
}
