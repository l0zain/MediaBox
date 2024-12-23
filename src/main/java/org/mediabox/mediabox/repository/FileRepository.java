package org.mediabox.mediabox.repository;

import org.mediabox.mediabox.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT f FROM File f WHERE f.user.id = :userId")
    List<File> findByUserId(Long userId);
}
