package Reiudemo.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository; // Giả lập DB

    @InjectMocks
    private PaymentService paymentService; // Đối tượng cần test logic

    @Test
    @DisplayName("Lưu giao dịch thành công")
    void savePayment_Success() {
        // 1. Given (Chuẩn bị dữ liệu mẫu)
        Payment input = new Payment();
        input.setAmount(100000.0);
        input.setDescription("Mua cafe");

        // Giả lập: Khi gọi save() thì trả về chính nó nhưng có thêm ID
        Payment savedPayment = new Payment();
        savedPayment.setId(1L);
        savedPayment.setAmount(100000.0);
        savedPayment.setDescription("Mua cafe");
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // 2. When (Chạy hàm logic)
        Payment result = paymentService.savePayment(input);

        // 3. Then (Kiểm tra xem logic đúng không)
        assertNotNull(result.getId()); // Phải có ID
        assertEquals(100000.0, result.getAmount());
        assertEquals("Mua cafe", result.getDescription());
        // Quan trọng: Kiểm tra xem hàm save của Repo có thực sự được gọi 1 lần không
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Lưu giao dịch với số tiền âm sẽ thất bại")
    void savePayment_Fail_NegativeAmount() {
        // 1. Given
        Payment input = new Payment();
        input.setAmount(-50000.0); // Số tiền âm
        input.setDescription("Giao dịch không hợp lệ");

        // Giả lập: Khi gọi save() với số tiền âm thì ném ra lỗi
        // when(paymentRepository.save(any(Payment.class)))
        //         .thenThrow(new IllegalArgumentException("Số tiền không được âm"));
        //Ham when & Then AI viết
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(input);
        });
        
        // try {
        //     paymentService.savePayment(input);
        // } catch (IllegalArgumentException e) {
        //     assertEquals("Amount must be positive", e.getMessage());
        // }
        //  thay vì dùng try catch thì dùng assertThrows sẽ gọn hơn và dễ đọc hơn
        assertEquals("Số tiền không được âm", exception.getMessage());

        // Kiểm tra xem hàm save của Repo có thực sự được gọi 1 lần không
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
