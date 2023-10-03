package com.citi.train.repository;

import com.citi.train.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainRepo extends JpaRepository<Train,Long> {

    Optional<Train> findByTrainNumber(Long aLong);
}
