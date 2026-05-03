package Reiudemo.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments") // Mọi đường dẫn sẽ bắt đầu bằng /api/payments
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization") // Cho phép
                                                                                                        // frontend
                                                                                                        // React truy
                                                                                                        // cập
public class PaymentRestController {

    @Autowired
    private PaymentRepository paymentRepository;

    // Lấy toàn bộ danh sách giao dịch dưới dạng JSON
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Đã có import ở trên nên sẽ không còn đỏ
    public Payment createPayment(@Valid @RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id)
                .map(ResponseEntity::ok) // Nếu thấy thì trả về code 200 (OK)
                .orElse(ResponseEntity.notFound().build()); // Không thấy thì trả về code 404 (Not Found)
    }

    // 1. Hàm xóa từng cái
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 2. Hàm xóa tất cả
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPayments() {
        paymentRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
