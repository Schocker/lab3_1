package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemMock extends RequestItem {

    public RequestItemMock(ProductData productData, int quantity, Money totalCost) {
        super(productData, quantity, totalCost);
    }

}
