package Reiudemo.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				// Nếu bạn dùng PasswordEncoder thì hãy mã hóa nó, nếu không thì để thô
				admin.setPassword("admin123");
				admin.setRole("ADMIN");
				userRepository.save(admin);
				System.out.println("--- Đã tạo tài khoản admin mặc định: admin/admin123 ---");
			}
		};
	}

}