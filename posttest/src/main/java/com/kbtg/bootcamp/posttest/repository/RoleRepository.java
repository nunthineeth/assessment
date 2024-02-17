package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.model.Role;
import com.kbtg.bootcamp.posttest.securityconfig.CustomUserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(String code);

}
