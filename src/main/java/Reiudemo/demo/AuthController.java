package Reiudemo.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private JwtTokenProvider tokenProvider;

    // Thay vì trả về String, hãy trả về một Map hoặc Object
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            String token = tokenProvider.generateToken(request.getUsername());
            return ResponseEntity.ok(Map.of("token", token)); // Trả về dạng {"token": "..."}
        }
        return ResponseEntity.status(401).body("Sai tài khoản hoặc mật khẩu");
    }

}

// Class phụ để nhận dữ liệu từ React gửi sang
class LoginRequest {
    private String username;
    private String password;

    // Getter và Setter (Bạn nhớ dùng Source Action để tạo nhanh nhé)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
