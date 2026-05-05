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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        // .thenThrow(new IllegalArgumentException("Số tiền không được âm"));
        // Ham when & Then AI viết
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.savePayment(input);
        });

        // try {
        // paymentService.savePayment(input);
        // } catch (IllegalArgumentException e) {
        // assertEquals("Amount must be positive", e.getMessage());
        // }
        // thay vì dùng try catch thì dùng assertThrows sẽ gọn hơn và dễ đọc hơn
        assertEquals("Số tiền không được âm", exception.getMessage());

        // Kiểm tra xem hàm save của Repo có thực sự được gọi 1 lần không
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test: Admin có quyền xóa thanh toán")
    @WithMockUser(username = "admin", roles = { "ADMIN" }) // Giả lập Admin đăng nhập
    void admin_can_delete_payment() throws Exception {
        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isOk()); // Mong đợi thành công (200)
    }

    @Test
    @DisplayName("Test: User bình thường KHÔNG có quyền xóa")
    @WithMockUser(username = "user", roles = { "USER" }) // Giả lập User đăng nhập
    void user_cannot_delete_payment() throws Exception {
        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isForbidden()); // Mong đợi bị chặn (403)
    }

    private RequestBuilder delete(String apipayments1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
