package com.martinm1500.marsrover.repositories;

import com.martinm1500.marsrover.models.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<Map,Long> {
}
