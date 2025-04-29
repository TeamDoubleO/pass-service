package com.doubleo.passservice.domain.pass.repository;

import com.doubleo.passservice.domain.pass.domain.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<Pass, Long> {}
