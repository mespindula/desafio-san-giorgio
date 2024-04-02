package br.com.desafio.controller;


import br.com.desafio.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PutMapping(path = "/api/payment")
    public ResponseEntity<Payment> setPayment(Payment request) {

        Payment payment = paymentService.processPayment(request);

        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }
}
