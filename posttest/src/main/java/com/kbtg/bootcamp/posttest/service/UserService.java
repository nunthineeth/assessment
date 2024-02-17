package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.UserDto;
import com.kbtg.bootcamp.posttest.mapper.UserMapper;
import com.kbtg.bootcamp.posttest.model.Role;
import com.kbtg.bootcamp.posttest.model.User;
import com.kbtg.bootcamp.posttest.repository.RoleRepository;
import com.kbtg.bootcamp.posttest.repository.UserRepository;
import com.kbtg.bootcamp.posttest.securityconfig.CustomUserDetail;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Transactional
    public User createUserResource(UserDto request) throws BadRequestException {

        Optional<User> userOptional = userRepository.findByUsername(request.username());

        if (userOptional.isPresent()) {
            //TODO
            throw new BadRequestException("user duplicate");
        }

        Optional<Role> roleOptional = roleRepository.findByCode(request.roleCode());
        if (roleOptional.isEmpty()) {
            //TODO
            throw new BadRequestException("role not found");
        }

        User user = UserMapper.MAPPER.map(request);
        user.setRole(roleOptional.get());
        return userRepository.save(user);
    }


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public CustomUserDetail findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("No user was found");
        }

        User user = userOptional.get();

        CustomUserDetail customUserDetail = new CustomUserDetail(user.getUsername(), user.getPassword());
        customUserDetail.setRoles(List.of(user.getRole().getCode()));
        customUserDetail.setPermissions(user.getRole().getPermissions());

        return customUserDetail;
    }
}
