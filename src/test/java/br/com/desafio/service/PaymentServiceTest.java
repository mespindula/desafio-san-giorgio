package br.com.desafio.service;

import br.com.desafio.controller.Payment;
import br.com.desafio.controller.PaymentItem;
import br.com.desafio.domain.exception.DataNotFoundException;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.PaymentStatusEnum;
import br.com.desafio.domain.model.VendorModel;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.repository.VendorRepository;
import br.com.desafio.service.messaging.PaymentEventProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class PaymentServiceTest {


    @Autowired
    protected VendorRepository vendorRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    protected PaymentService paymentService;

    @Mock
    protected PaymentEventProducer paymentEventProducer;


    @BeforeAll
    public void setup() {

        vendorRepository.save(VendorModel.builder().id("01").name("João").build());

        paymentRepository.save(PaymentModel.builder().id("01").value(new BigDecimal("100")).build());
        paymentRepository.save(PaymentModel.builder().id("02").value(new BigDecimal("1390")).build());
        paymentRepository.save(PaymentModel.builder().id("03").value(new BigDecimal("7000.10")).build());

        paymentService = new PaymentService(vendorRepository, paymentRepository, paymentEventProducer);
    }

    private List<PaymentItem> mockPaymentItemSuccess() {
        List<PaymentItem> paymentItems = new ArrayList<>();

        PaymentItem paymentItem = PaymentItem.builder()
                .paymentId("01")
                .paymentValue(new BigDecimal("99"))
                .build();

        paymentItems.add(paymentItem);

        paymentItem = PaymentItem.builder()
                .paymentId("02")
                .paymentValue(new BigDecimal("1390"))
                .build();

        paymentItems.add(paymentItem);

        paymentItem = PaymentItem.builder()
                .paymentId("02")
                .paymentValue(new BigDecimal("7000.10"))
                .build();

        paymentItems.add(paymentItem);

        return paymentItems;
    }

    @Test
    public void test_processPaymentSuccess() {

        Payment payment = Payment.builder()
                .vendorId("01")
                .paymentItems(mockPaymentItemSuccess())
                .build();

        Payment paymentProcessed = paymentService.processPayment(payment);

        Assertions.assertNotNull(paymentProcessed.getPaymentItems());
        Assertions.assertEquals(paymentProcessed.getPaymentItems().size(), 3);

        paymentProcessed.getPaymentItems().forEach(paymentItem -> {
            if (paymentItem.getPaymentValue().compareTo(new BigDecimal("99")) == 0) {
                Assertions.assertEquals(PaymentStatusEnum.LESS.getStatusDescription(), paymentItem.getPaymentStatus());
            } else if (paymentItem.getPaymentValue().compareTo(new BigDecimal("1390")) == 0) {
                Assertions.assertEquals(PaymentStatusEnum.EQUAL.getStatusDescription(), paymentItem.getPaymentStatus());
            } else if (paymentItem.getPaymentValue().compareTo(new BigDecimal("7000.10")) == 0) {
                Assertions.assertEquals(PaymentStatusEnum.GREATER.getStatusDescription(), paymentItem.getPaymentStatus());
            }
        });
    }

    @Test
    public void test_VendorNotFound() {

        Payment payment = Payment.builder()
                .vendorId("02")
                .paymentItems(mockPaymentItemSuccess())
                .build();

        assertThrows(DataNotFoundException.class, () -> paymentService.processPayment(payment));
    }

    @Test
    public void test_PaymentNotFound() {

        List<PaymentItem> paymentItems = new ArrayList<>();

        PaymentItem paymentItem = PaymentItem.builder()
                .paymentId("99")
                .paymentValue(new BigDecimal("50"))
                .build();

        paymentItems.add(paymentItem);

        Payment payment = Payment.builder()
                .vendorId("01")
                .paymentItems(paymentItems)
                .build();

        assertThrows(DataNotFoundException.class, () -> paymentService.processPayment(payment));
    }

}
