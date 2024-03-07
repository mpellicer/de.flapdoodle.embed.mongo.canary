package example;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBRepository extends PagingAndSortingRepository<ProductB, String> {

    Optional<ProductB> findById(ObjectId id);
    Page<ProductB> findAllByBudgetBetween(Range<Double> budgetRange, Pageable pageable);
}