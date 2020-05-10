package com.alphago365.octopus.controller;

import com.alphago365.octopus.exception.UserForbiddenException;
import com.alphago365.octopus.model.RoleName;
import com.alphago365.octopus.model.User;
import com.alphago365.octopus.payload.*;
import com.alphago365.octopus.security.JwtTokenProvider;
import com.alphago365.octopus.security.JwtUserDetailsService;
import com.alphago365.octopus.security.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Api(tags = "user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "sign in by username and password", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully sign in", response = SignInResponse.class)
    })
    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername()
                    , signInRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(signInRequest.getUsername());

        final String token = jwtTokenProvider.generateToken(userDetails);

        return ResponseEntity.ok(new SignInResponse(token));
    }

    @ApiOperation(value = "check if username in use", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return true or false", response = Boolean.class)
    })
    @GetMapping(value = "/username-in-use")
    public Boolean existsByUsername(@NotBlank @RequestParam(value = "username") String username) {
        return userDetailsService.existsByUsername(username);
    }

    @ApiOperation(value = "check if email in use", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return true or false", response = Boolean.class)
    })
    @GetMapping(value = "/email-in-use")
    public Boolean existsByEmail(@Email @RequestParam(value = "email") String email) {
        return userDetailsService.existsByEmail(email);
    }

    @ApiOperation(value = "check if cellphone in use", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return true or false", response = Boolean.class)
    })
    @GetMapping(value = "/cellphone-in-use")
    public Boolean existsByCellphone(@NotBlank @RequestParam(value = "cellphone") String cellphone) {
        return userDetailsService.existsByCellphone(cellphone);
    }

    @ApiOperation(value = "user sign up", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create user", response = AppResponse.class)
    })
    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setCellphone(signUpRequest.getCellphone());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        User savedUser = userDetailsService.save(newUser);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(savedUser.getUsername()).toUri();
        return ResponseEntity.created(location).body(new AppResponse(true, "Successfully sign up, user created"));
    }

    @ApiOperation(value = "get user by username", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get user by username", response = User.class),
            @ApiResponse(code = 401, message = "current user not authorized"),
            @ApiResponse(code = 404, message = "user not found by username"),
            @ApiResponse(code = 403, message = "current user not allow to visit other username")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findByUsername(@Valid @NotBlank @RequestParam(value = "username") String username) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currUsername = userPrincipal.getUsername();
        Collection<GrantedAuthority> authorities = userPrincipal.getAuthorities();
        for(GrantedAuthority grantedAuthority : authorities) {
            String authority = grantedAuthority.getAuthority();
            RoleName roleName = RoleName.valueOf(authority);
            if(roleName.compareTo(RoleName.ROLE_USER) > 0) {
                break;
            }
            if ((roleName.equals(RoleName.ROLE_USER) && !username.equals(currUsername))) {
                throw new UserForbiddenException("Only yourself username can be allowed");
            }
        }
        return ResponseEntity.ok(userDetailsService.findByUsernameOrEmailOrCellphone(username).<UsernameNotFoundException>orElseThrow(() -> {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }));
    }

    @ApiOperation(value = "get current sign in user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get current signed in user", response = User.class),
            @ApiResponse(code = 401, message = "current user not authorized"),
            @ApiResponse(code = 404, message = "user not found by username")
    })
    @GetMapping(value = "/current")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userPrincipal.getUsername();
        return ResponseEntity.ok(userDetailsService.findByUsername(username).<UsernameNotFoundException>orElseThrow(() -> {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully change password", response = AppResponse.class),
            @ApiResponse(code = 401, message = "Username or old password incorrect")
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        String username = changePasswordRequest.getUsername();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        userDetailsService.changePassword(username, oldPassword, newPassword);
        return ResponseEntity.ok(new AppResponse(true, "Successfully change password"));
    }
}