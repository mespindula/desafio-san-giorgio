package br.com.desafio.repository;

import br.com.desafio.domain.model.VendorModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends MongoRepository<VendorModel, ObjectId> {

    Optional<VendorModel> findById(final String id);
}
