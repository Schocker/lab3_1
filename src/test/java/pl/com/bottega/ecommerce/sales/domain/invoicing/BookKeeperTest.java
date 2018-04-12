package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

    private ClientData client;
    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private Money money;
    private TaxPolicy taxPolicy;
    
    @Before
    public void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        client = new Client().generateSnapshot();
        invoiceRequest = new InvoiceRequest(client);
        taxPolicy = mock(TaxPolicy.class);
        money = new Money(new BigDecimal(1), Currency.getInstance(Locale.UK));
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(money, ""));
    }
    
    @Test
    public void requestingInvoiceWithOneItemShouldReturnInvoiceWithOneItem() {
        ProductData product = ProductDataBuilder.productData()
                .withMoney(money)
                .withName("test")
                .build();
        RequestItem item = RequestItemBuilder.requestItem()
                .withProductData(product)
                .withQuantity(1)
                .withTotalCost(money)
                .build();
        invoiceRequest.add(item);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }
       
    @Test
    public void requestingInvoiceWithTwoItemsShouldCallCalculateTaxMethodTwoTimes() {
        ProductData product = ProductDataBuilder.productData()
                .withMoney(money)
                .withName("test")
                .build();
        ProductData product2 = ProductDataBuilder.productData()
                .withMoney(money)
                .withName("test2")
                .build();
        RequestItem item = RequestItemBuilder.requestItem()
                .withProductData(product)
                .withQuantity(1)
                .withTotalCost(money)
                .build();
        RequestItem item2 = RequestItemBuilder.requestItem()
                .withProductData(product2)
                .withQuantity(1)
                .withTotalCost(money)
                .build();
        invoiceRequest.add(item);
        invoiceRequest.add(item2);
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }
    
    @Test
    public void requestingInvoiceWithoutAnyItemShouldReturnEmptyInvoice() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(0));
    }
    
    @Test
    public void requestingInvoiceWithoutAnyItemShouldNotCallCalculateTaxMethod() {
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(any(ProductType.class), any(Money.class));
    }
    
}
