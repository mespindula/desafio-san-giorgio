package br.com.desafio.service;

import br.com.desafio.controller.Payment;
import br.com.desafio.controller.PaymentItem;
import br.com.desafio.domain.exception.DataNotFoundException;
import br.com.desafio.domain.model.PaymentStatusEnum;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.repository.VendorRepository;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.service.messaging.PaymentEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final VendorRepository vendorRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(
            VendorRepository vendorRepository,
            PaymentRepository paymentRepository,
            PaymentEventProducer paymentEventProducer) {
        this.vendorRepository = vendorRepository;
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }
    public Payment processPayment(final Payment payment) {
        validateVendor(payment.getVendorId());
        final Map<String, BigDecimal> originalValues = validatePaymentsAndGetValues(payment.getPaymentItems());

        payment.getPaymentItems().forEach(
                paymentItem -> {
                    if (paymentItem.getPaymentValue().compareTo(originalValues.get(paymentItem.getPaymentId())) < 0) {
                        paymentEventProducer.SendToParcialPaymentProcessing(paymentItem);
                        paymentItem.setPaymentStatus(PaymentStatusEnum.LESS.getStatusDescription());
                    } else if (paymentItem.getPaymentValue().compareTo(originalValues.get(paymentItem.getPaymentId())) == 0) {
                        paymentEventProducer.SendToTotalPaymentProcessing(paymentItem);
                        paymentItem.setPaymentStatus(PaymentStatusEnum.EQUAL.getStatusDescription());
                    } else {
                        paymentEventProducer.SendToExcessPaymentProcessing(paymentItem);
                        paymentItem.setPaymentStatus(PaymentStatusEnum.GREATER.getStatusDescription());
                    }
                }
        );

        return payment;
    }

    private void validateVendor(final String vendorId) {
        vendorRepository.findById(vendorId).orElseThrow(DataNotFoundException::new);
    }

    private Map<String, BigDecimal> validatePaymentsAndGetValues(final List<PaymentItem> paymentItems) {
        final Map<String, BigDecimal> originalValues = new HashMap<>();
        paymentItems.forEach(paymentItem -> {
            PaymentModel paymentDocument = paymentRepository.findById(paymentItem.getPaymentId()).orElseThrow(DataNotFoundException::new);
            originalValues.put(paymentDocument.getId(), paymentDocument.getValue());
        });
        return originalValues;
    }
}
