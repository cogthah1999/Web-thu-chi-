package Reiudemo.demo; // Đảm bảo dòng này giống hệt file DemoApplication.java

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public double calculateTotal(List<Payment> payments) {
        if (payments == null) return 0;
        return payments.stream().mapToDouble(p -> p.getAmount()).sum();
    }

    public Payment savePayment(Payment payment) {
        // TODO Auto-generated method stub
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Số tiền không được âm");
        }
        return paymentRepository.save(payment);
    }

}
