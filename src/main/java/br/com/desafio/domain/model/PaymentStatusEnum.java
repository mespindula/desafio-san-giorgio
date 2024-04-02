package br.com.desafio.domain.model;

public enum PaymentStatusEnum {

    LESS("Parcial"),
    EQUAL("Total"),
    GREATER("Excedente");

    private final String statusDescription;

    public String getStatusDescription() {
        return statusDescription;
    }

    PaymentStatusEnum(final String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
