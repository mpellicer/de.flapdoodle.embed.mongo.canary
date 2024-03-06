package example;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "products")
@Data
@AllArgsConstructor
public class Product {

    @Id
    private ObjectId id;
    private String title;
    private BigDecimal budget;
}
