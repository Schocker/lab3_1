package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;

public class AddProductCommandBuilder {
    private Id orderId;
    private Id productId;
    private int quantity;

    public AddProductCommandBuilder() {
        orderId = Id.generate();
        productId = Id.generate();
        quantity = 1;
    }

    public void withOrderId(Id orderId) {
        this.orderId = orderId;
    }

    public void withProductId(Id productId) {
        this.productId = productId;
    }

    public void withQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AddProductCommand build() {
        return new AddProductCommand(orderId, productId, quantity);
    }
}
