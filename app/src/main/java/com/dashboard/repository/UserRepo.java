package com.dashboard.repository;

import com.dashboard.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo
        extends MongoRepository<UserModel, String> {
     UserModel findByUserName(String userName);


}

