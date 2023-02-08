package com.dashboard.repository;

import com.dashboard.model.SessionDetailsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionDetailsRepo
        extends MongoRepository<SessionDetailsModel, String> {

     SessionDetailsModel findBySessionId(String sessionId);

}

