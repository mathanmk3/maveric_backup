package com.citi.train.repository;

import com.citi.train.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserDetails,Long> {
}
