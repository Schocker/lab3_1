package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BookKeeperTest {
    private BookKeeper bookKeeper;
    private ProductData productData;
    private InvoiceRequest invoiceRequest;
    private TaxPolicy taxPolicy;
    private RequestItem requestItem;

    @Before
    public void setUp() throws Exception {
        InvoiceFactory invoiceFactory = mock(InvoiceFactory.class);
        when(invoiceFactory.create(any(ClientData.class))).thenReturn(new Invoice(Id.generate(), new ClientData(Id.generate(), "Test")));

        ClientData clientData = new ClientData(Id.generate(), "Test");

        bookKeeper = new BookKeeper(invoiceFactory);
        productData = new ProductData(Id.generate(), new Money(1), "Test", ProductType.STANDARD, new Date());
        invoiceRequest = new InvoiceRequest(clientData);

        taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax( any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(1), "Tax"));
    }

    @Test
    public void invoiceRequestWithOneItemShouldReturnInvoiceWithOneItem() {
        requestItem = new RequestItem(productData, 1, new Money(1));
        invoiceRequest.add(requestItem);


        Invoice issuedInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(issuedInvoice.getItems().size(), is(1));
    }

    @Test
    public void invoiceRequestWithTwoItemsShouldInvokeCalculateTaxMethodTwice() {
        requestItem = new RequestItem(productData, 1, new Money(1));

        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(Mockito.<ProductType>any(), Mockito.<Money>any());
    }

    @Test
    public void invoiceRequestWithNoItemsShouldReturnEmptyInvoice() {
        assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size(), is(0));
    }

    @Test
    public void invoiceRequestWithNoItemsShouldNotInvokeCalculateTaxMethod() {
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(0)).calculateTax(Mockito.<ProductType>any(), Mockito.<Money>any());
    }
}