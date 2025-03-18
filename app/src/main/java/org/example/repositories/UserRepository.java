package org.example.repositories;

import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo, String> {

    public UserInfo findByUsername(String username);
}
