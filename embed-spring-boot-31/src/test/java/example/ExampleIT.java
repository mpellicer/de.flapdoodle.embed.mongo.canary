package example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest()
@ExtendWith(SpringExtension.class)
class ExampleIT {

    @Autowired
    private ProductRepository productRepository;

    private static final String COLLECTION = "products";

    @BeforeAll
    static void populateCollection(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.insert(new Product(null, "One item", BigDecimal.valueOf(100)), COLLECTION);
        mongoTemplate.insert(new Product(null, "Another item", BigDecimal.valueOf(200)), COLLECTION);
        mongoTemplate.insert(new Product(null, "And Another one item", BigDecimal.valueOf(300)), COLLECTION);
        mongoTemplate.insert(new Product(null, "Xtra item", BigDecimal.valueOf(400)), COLLECTION);
        mongoTemplate.insert(new Product(null, "Fifth item", BigDecimal.valueOf(500)), COLLECTION);
    }

    @Test
    void testAllItemsArePresent() {
        PageRequest page = PageRequest.of(0, 100);
        Page<Product> productList = productRepository.findAll(page);
        assertEquals(productList.getTotalElements(), 5);
    }

    @Test
    void testFilterByBudgetAll() {
        PageRequest page = PageRequest.of(0, 100);
        Range<Double> range = Range.closed(100.0, 199.0);
        Page<Product> productList = productRepository.findAllByBudgetBetween(range, page);
        assertEquals(1, productList.getTotalElements());
    }

}
