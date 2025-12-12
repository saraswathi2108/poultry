package com.auth.farm.service;

import com.auth.farm.dto.LoginRequest;
import com.auth.farm.dto.LoginResponse;
import com.auth.farm.dto.SignupRequest;
import com.auth.farm.entity.User;
import com.auth.farm.enums.Role;
import com.auth.farm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthService {

    private  final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;




    public String registerVendor(SignupRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User vendor = new User();
        vendor.setUsername(request.getUsername());
        vendor.setEmail(request.getEmail());
        vendor.setPassword(encoder.encode(request.getPassword()));
        vendor.setPhone(request.getPhone());
        vendor.setIsActive(true);
        vendor.getRoles().add(Role.VENDOR);

        userRepo.save(vendor);
        return "Vendor Registered Successfully!";
    }

    public String registerAdmin(SignupRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }
        if (userRepo.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User admin = new User();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(encoder.encode(request.getPassword()));
        admin.setPhone(request.getPhone());
        admin.setIsActive(true);
        admin.getRoles().add(Role.ADMIN);

        userRepo.save(admin);
        return "Admin Registered Successfully!";
    }

    public LoginResponse login(LoginRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Generate JWT with userId + roles + fullName
        String token = jwtService.generateToken(user);

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        // 🔥 Include userId in the login response
        return new LoginResponse(token, user.getUsername(), roles, user.getId());
    }


}
