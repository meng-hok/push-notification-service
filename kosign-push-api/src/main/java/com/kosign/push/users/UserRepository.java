package com.kosign.push.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User,String>{

	User findByUsernameAndStatus(String username, Character status);

    
}