package com.example.board.repository;

import com.example.board.entity.CabinetFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinetFileRepository extends JpaRepository<CabinetFile, Long> {
}
