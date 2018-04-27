package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
    private Id productId;
    private Money productPrice;
    private String productName;
    private ProductType productType;

    public void withId(Id id) {
        this.productId = id;
    }

    public void withPrice(Money productPrice) {
        this.productPrice = productPrice;
    }

    public void withName(String name) {
        this.productName = name;
    }

    public void withType(ProductType type) {
        this.productType = type;
    }

    public Product build() {
        return new Product(productId, productPrice, productName, productType);
    }
}
