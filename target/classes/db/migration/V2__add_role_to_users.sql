-- Thêm cột role vào bảng users (nếu bạn đã có bảng users)
-- Hoặc nếu chưa có, hãy tạo bảng users có cột role
ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'ROLE_USER';

-- Cập nhật một tài khoản thành ADMIN để test
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'admin';
