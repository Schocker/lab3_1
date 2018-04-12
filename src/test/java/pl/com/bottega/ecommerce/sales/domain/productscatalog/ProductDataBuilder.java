package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {

    private Id id = Id.generate();
    private Money money;
    private String name;
    private ProductType productType = ProductType.STANDARD;
    private Date date;
    
    public ProductData build() {
        ProductData productData = new ProductData(id, money, name, productType, date);
        return productData;
    }
    
    public static ProductDataBuilder productData() {
        return new ProductDataBuilder();
    }
    
    public ProductDataBuilder withId(Id id) {
        this.id = id;
        return this;
    }
    
    public ProductDataBuilder withMoney(Money money) {
        this.money = money;
        return this;
    }

    public ProductDataBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public ProductDataBuilder withProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }
    
    public ProductDataBuilder withDate(Date date) {
        this.date = date;
        return this;
    }
    
}
