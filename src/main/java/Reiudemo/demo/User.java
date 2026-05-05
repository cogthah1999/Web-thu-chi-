package Reiudemo.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users") // Phải khớp với tên bảng trong file V2__.sql
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Đây chính là "chìa khóa" giải quyết lỗi đỏ trong ảnh của bạn
    @Column(nullable = false)
    private String role; // Ví dụ: "ROLE_USER" hoặc "ROLE_ADMIN"
}
