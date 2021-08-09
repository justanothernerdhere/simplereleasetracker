package com.an0nn30.releasetracker.repos;

import com.an0nn30.releasetracker.entites.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/***
 * ReleaseRepo.java
 *
 * Pretty standard repository...not much going on here.
 */

@Repository
public interface ReleaseRepo extends JpaRepository<Release, Integer>, JpaSpecificationExecutor<Release> { }
