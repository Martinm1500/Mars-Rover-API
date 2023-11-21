package com.martinm1500.marsrover.repositories;

import com.martinm1500.marsrover.models.Rover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoverRepository extends JpaRepository<Rover,Long> {
}
