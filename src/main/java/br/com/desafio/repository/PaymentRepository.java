package br.com.desafio.repository;

import br.com.desafio.domain.model.PaymentModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentModel, ObjectId> {

    Optional<PaymentModel> findById(String idCobranca);
}
