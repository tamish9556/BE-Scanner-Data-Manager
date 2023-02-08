package com.dashboard.repository;
import com.dashboard.model.UnifiedDataScanner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnifiedDataRepository extends MongoRepository<UnifiedDataScanner, String> {

    Optional<UnifiedDataScanner> findByBuildNumber(String buildNumber);

}