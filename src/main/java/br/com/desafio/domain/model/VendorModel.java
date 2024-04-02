package br.com.desafio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vendor")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorModel {

    @Id
    private String id;
    private String name;

}
