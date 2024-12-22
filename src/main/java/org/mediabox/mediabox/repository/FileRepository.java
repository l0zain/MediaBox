package org.mediabox.mediabox.repository;

import org.mediabox.mediabox.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
