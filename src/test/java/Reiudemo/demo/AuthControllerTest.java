package Reiudemo.demo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class) // Sử dụng Mockito để giả lập dữ liệu
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController; // Đối tượng cần test

    @Mock
    private JwtTokenProvider tokenProvider; // Giả lập cái này để không phải chạy logic thật

    @Test
    @DisplayName("Đăng nhập thành công với tài khoản admin")
    void login_Success() {
        // 1. Chuẩn bị dữ liệu giả (Given)
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        // Giả lập: hễ gọi generateToken với "admin" thì trả về "mock-token"
        when(tokenProvider.generateToken("admin")).thenReturn("mock-token");

        // 2. Chạy hàm cần test (When)
        ResponseEntity<?> result = authController.login(request);

        // 3. Kiểm tra kết quả (Then)

        Map<String, String> body = (Map<String, String>) result.getBody();
        assertEquals("mock-token", body.get("token"));
    }

    @Test
    @DisplayName("Đăng nhập thất bại khi sai mật khẩu")
    void login_Fail() {
        // 1. Given
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-pass");

        // 2. When
        ResponseEntity<?> result = authController.login(request);

        // 3. Then: Kiểm tra xem status code trả về có phải là 401 (UNAUTHORIZED) không
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        // Kiểm tra thêm nội dung thông báo lỗi nếu muốn
        Map<String, String> body = (Map<String, String>) result.getBody();
        assertEquals("Invalid username or password", body.get("message"));
    }

}
