package Reiudemo.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
                        return ResponseEntity.ok(Map.of(
                                "token", token,
                                "role", user.getRole()));
                    }
                    return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Invalid username or password")));
    }
    
}