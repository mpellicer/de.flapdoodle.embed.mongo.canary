package example;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, String> {

    Optional<Product> findById(ObjectId id);
    Page<Product> findAllByBudgetBetween(Range<Double> budgetRange, Pageable pageable);
}