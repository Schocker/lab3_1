package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    private ProductData productData;
    private RequestItem requestItem;
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private int quantity = 10;

    @Before
    public void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());

        productData = new ProductData(Id.generate(), new Money(10), "product name",
                ProductType.FOOD, new Date());
        requestItem = new RequestItem(productData, quantity, new Money(10));

        taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(5), "test tax");
            }
        };
    }

    @Test
    public void issuanceShouldReturnInvoiceWithOnePositionWhenOnePositionRequestGiven() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "test client"));
        invoiceRequest.add(requestItem);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }
}