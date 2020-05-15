package com.alphago365.octopus.security;

import com.alphago365.octopus.exception.InvalidCredentialException;
import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Role;
import com.alphago365.octopus.model.RoleName;
import com.alphago365.octopus.model.User;
import com.alphago365.octopus.repository.RoleRepository;
import com.alphago365.octopus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmailOrCellphone(identifier, identifier, identifier)
                .<UsernameNotFoundException>orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found by username, email, or cellphone: " + identifier);
                });

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                user.getRoles().stream().map(r -> r.getName().toString()).collect(Collectors.joining(","))
        );

        return new UserPrincipal(user.getUsername(), user.getPassword(), grantedAuthorities, user.getId());
    }

    @Transactional
    public User save(User user) {
        RoleName roleName = RoleName.ROLE_USER;
        Role role = roleRepository.findByName(roleName).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Role not found by name: " + roleName.name());
        });
        user.setRoles(Collections.singleton(role));
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<User> findByUsernameOrEmailOrCellphone(String identifier) {
        return userRepository.findByUsernameOrEmailOrCellphone(identifier, identifier, identifier);
    }

    @Transactional
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Boolean existsByCellphone(String cellphone) {
        return userRepository.existsByCellphone(cellphone);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws InvalidCredentialException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new InvalidCredentialException("Username or old password incorrect");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialException("Username or old password incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}