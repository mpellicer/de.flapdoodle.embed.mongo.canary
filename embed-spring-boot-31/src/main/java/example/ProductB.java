package example;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "productsB")
@Data
@AllArgsConstructor
public class ProductB {

    @Id
    private ObjectId id;
    private String title;
    private Double budget;
}
