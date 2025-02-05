package br.com.desafio.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @JsonProperty("vendor_id")
    private String vendorId;
    @JsonProperty("payment_items")
    private List<PaymentItem> paymentItems;
}
