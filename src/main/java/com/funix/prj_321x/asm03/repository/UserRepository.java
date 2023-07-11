package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByEmail(String email);

    User getUserById(int theId);
}
