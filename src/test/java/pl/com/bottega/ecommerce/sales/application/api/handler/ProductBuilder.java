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

    public ProductBuilder() {
    }

    public void withId(Id id) {
        productId = id;
    }

    public Product build() {
        return new Product(productId, productPrice, productName, productType);
    }
}
