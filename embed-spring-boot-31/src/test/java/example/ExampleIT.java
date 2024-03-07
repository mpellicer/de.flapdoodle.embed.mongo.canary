package example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.BsonType;
import org.bson.Document;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest()
@ExtendWith(SpringExtension.class)
class ExampleIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBRepository productBRepository;

    private static final String PRODUCT_COLLECTION = "products";
    private static final String PRODUCTB_COLLECTION = "productsB";

    @BeforeAll
    static void populateCollection(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.insert(new Product(null, "One item", BigDecimal.valueOf(100)), PRODUCT_COLLECTION);
        mongoTemplate.insert(new Product(null, "Another item", BigDecimal.valueOf(200)), PRODUCT_COLLECTION);
        mongoTemplate.insert(new Product(null, "And Another one item", BigDecimal.valueOf(300)), PRODUCT_COLLECTION);
        mongoTemplate.insert(new Product(null, "Xtra item", BigDecimal.valueOf(400)), PRODUCT_COLLECTION);
        mongoTemplate.insert(new Product(null, "Fifth item", BigDecimal.valueOf(500)), PRODUCT_COLLECTION);

        mongoTemplate.insert(new ProductB(null, "One item", 100.0), PRODUCTB_COLLECTION);
        mongoTemplate.insert(new ProductB(null, "Another item", 200.0), PRODUCTB_COLLECTION);
        mongoTemplate.insert(new ProductB(null, "And Another one item", 300.0), PRODUCTB_COLLECTION);
        mongoTemplate.insert(new ProductB(null, "Xtra item", 400.0), PRODUCTB_COLLECTION);
        mongoTemplate.insert(new ProductB(null, "Fifth item", 500.0), PRODUCTB_COLLECTION);
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

    @Test
    void testAllItemsArePresentB() {
        PageRequest page = PageRequest.of(0, 100);
        Page<ProductB> productList = productBRepository.findAll(page);
        assertEquals(productList.getTotalElements(), 5);
    }

    @Test
    void testFilterByBudgetAllB() {
        PageRequest page = PageRequest.of(0, 100);
        Range<Double> range = Range.closed(100.0, 199.0);
        Page<ProductB> productList = productBRepository.findAllByBudgetBetween(range, page);
        assertEquals(1, productList.getTotalElements());
    }

    @Test
    void withQuery(@Autowired MongoClient mongoClient) {
        MongoCollection<Document> produktCollection = mongoClient.getDatabase("test")
          .getCollection(PRODUCT_COLLECTION);

        MongoCollection<Document> produktBCollection = mongoClient.getDatabase("test")
          .getCollection(PRODUCTB_COLLECTION);

        ArrayList<Document> productList = produktCollection.find()
          .filter(Filters.type("budget", BsonType.STRING))
          .into(new ArrayList<>());

        ArrayList<Document> productBList = produktBCollection.find()
          .filter(Filters.type("budget", BsonType.DOUBLE))
          .into(new ArrayList<>());

        assertEquals(5, productList.size());
        assertEquals(5, productBList.size());

        ArrayList<Document> filtered = produktCollection
          .find(Document.parse("{ \"budget\" : { \"$gte\" : 100.0, \"$lte\" : 199.0}}"))
          .into(new ArrayList<>());

        ArrayList<Document> filteredB = produktBCollection
          .find(Document.parse("{ \"budget\" : { \"$gte\" : 100.0, \"$lte\" : 199.0}}"))
          .into(new ArrayList<>());

        assertEquals(0, filtered.size()); // type mismatch, so no result
        assertEquals(1, filteredB.size());
    }

}
