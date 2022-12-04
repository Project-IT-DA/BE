package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.CommunityFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityFileRepository extends JpaRepository<CommunityFile, Long> {
    List<CommunityFile> findAllByCommunityId(Long id);
}
