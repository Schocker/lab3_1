package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
    private Id id;

    private Money price;

    private String name;

    private ProductType productType;

    public ProductBuilder() {

    }
    public ProductBuilder withId(Id id){
        this.id = id;
        return this;
    }

    public ProductBuilder withPrice(Money price){
        this.price = price;
        return this;
    }

    public ProductBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductBuilder withProductType(ProductType productType){
        this.name = name;
        return this;
    }

    public Product build(){
        return new Product( id, price, name, productType );
    }
}
