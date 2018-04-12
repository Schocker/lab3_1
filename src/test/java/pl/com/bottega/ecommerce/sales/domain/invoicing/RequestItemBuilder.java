package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    
    private ProductData productData;
    private int quantity;
    private Money totalCost;
    
    public RequestItem build() {
        RequestItem item = new RequestItem(productData, quantity, totalCost);
        return item;
    }
    
    public static RequestItemBuilder requestItem() {
        return new RequestItemBuilder();
    }
    
    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public RequestItemBuilder withTotalCost(Money totalCost) {
        this.totalCost = totalCost;
        return this;
    }
    
}
