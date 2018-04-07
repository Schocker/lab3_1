package lab3_1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItemMock;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

class TestTaxPolicy implements TaxPolicy {

    private int numberOfCalculateTaxMethodCalls = 0;
    
    @Override
    public Tax calculateTax(ProductType productType, Money net) {
        numberOfCalculateTaxMethodCalls++;
        return new Tax(net, "");
    }
    
    public int getNumberOfCalculateTaxMethodCalls() {
        return numberOfCalculateTaxMethodCalls;
    }
    
}

public class BookKeeperTest {

    @Test
    public void requestingInvoiceWithOneItemShouldReturnInvoiceWithOneItem() {
        ClientData client = mock(ClientData.class);
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);
        ProductData product = mock(ProductData.class);
        Money money = new Money(new BigDecimal(1), Currency.getInstance(Locale.UK));
        RequestItemMock item = new RequestItemMock(product, 1, money);
        invoiceRequest.add(item);
        TaxPolicy taxPolicy = new TestTaxPolicy();
        Invoice invoice = new BookKeeper(new InvoiceFactory()).issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }
       
    @Test
    public void requestingInvoiceWithTwoItemsShouldCallCalculateTaxMethodTwoTimes() {
        ClientData client = mock(ClientData.class);
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);
        ProductData product = mock(ProductData.class);
        ProductData product2 = mock(ProductData.class);
        Money money = new Money(new BigDecimal(1), Currency.getInstance(Locale.UK));
        Money money2 = new Money(new BigDecimal(2), Currency.getInstance(Locale.UK));
        RequestItemMock item = new RequestItemMock(product, 1, money);
        RequestItemMock item2 = new RequestItemMock(product2, 1, money2);
        invoiceRequest.add(item);
        invoiceRequest.add(item2);
        TestTaxPolicy taxPolicy = new TestTaxPolicy();
        Invoice invoice = new BookKeeper(new InvoiceFactory()).issuance(invoiceRequest, taxPolicy);
        assertThat(taxPolicy.getNumberOfCalculateTaxMethodCalls(), is(2));
    }
    
}
