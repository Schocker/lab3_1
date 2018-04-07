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

    @Override
    public Tax calculateTax(ProductType productType, Money net) {
        return new Tax(net, "");
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
        Invoice invoice = new BookKeeper(new InvoiceFactory()).issuance(invoiceRequest, new TestTaxPolicy());
        assertThat(invoice.getItems().size(), is(1));
    }
       
}
