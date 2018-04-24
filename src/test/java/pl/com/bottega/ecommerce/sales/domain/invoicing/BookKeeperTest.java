package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private TaxPolicy taxPolicy;
    private ClientData clientData;
    private InvoiceRequest invoiceRequest;
    private BookKeeper bookKeeper;
    private Money price;

    @Before
    public void setUp() throws Exception {
        bookKeeper = new BookKeeper( new InvoiceFactory() );
        clientData = new ClientDataBuilder()
                .build();
        invoiceRequest = new InvoiceRequest( clientData );
        price = new Money( 12.3 );
        taxPolicy = mock( TaxPolicy.class );
        when( taxPolicy.calculateTax( any( ProductType.class ), any( Money.class ) ) )
                .thenReturn( new Tax( new Money( 1.23 ), "First item Tax" ) );
    }

    @Test
    public void emptyInvoiceRequestShouldReturnInvoiceWithZeroField() {
        Invoice invoice = bookKeeper.issuance( invoiceRequest, taxPolicy );

        assertThat( invoice.getItems().size(), is( 0 ) );
    }

    @Test
    public void invoiceRequestWithOneFieldShouldReturnInvoiceWithOneField() {

        ProductData productData = new ProductDataBuilder()
                .build();
        RequestItem requestItem = new RequestItem( productData, 1, price );

        invoiceRequest.add( requestItem );
        Invoice invoice = bookKeeper.issuance( invoiceRequest, taxPolicy );

        assertThat( invoice.getItems().size(), is( 1 ) );
    }

    @Test
    public void emptyInvoiceRequestShouldCallCalculateTaxZeroTimes() {
        bookKeeper.issuance( invoiceRequest, taxPolicy );

        verify( taxPolicy, times( 0 ) ).calculateTax( any( ProductType.class ), any( Money.class ) );
    }

    @Test
    public void invoiceRequestWithTwoFieldShouldCallCalculateTaxTwoTimes() {
        ProductData productData1 = new ProductDataBuilder()
                .build();
        ProductData productData2 = new ProductDataBuilder()
                .build();
        RequestItem requestItem1 = new RequestItem( productData1, 1, price );
        RequestItem requestItem2 = new RequestItem( productData2, 2, price );
        invoiceRequest.add( requestItem1 );
        invoiceRequest.add( requestItem2 );
        bookKeeper.issuance( invoiceRequest, taxPolicy );
        verify( taxPolicy, times( 2 ) ).calculateTax( any( ProductType.class ), any( Money.class ) );
    }
}