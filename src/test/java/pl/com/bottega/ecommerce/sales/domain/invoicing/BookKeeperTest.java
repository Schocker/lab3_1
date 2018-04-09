package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    TaxPolicy taxPolicy;
    ClientData clientData;
    InvoiceRequest invoiceRequest;
    BookKeeper bookKeeper;
    Money price;

    @Before
    public void setUp() throws Exception {
        bookKeeper = new BookKeeper( new InvoiceFactory() );
        clientData = mock( ClientData.class );
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

        ProductData productData = mock( ProductData.class );
        RequestItem requestItem = new RequestItem( productData, 1, price );

        invoiceRequest.add( requestItem );
        Invoice invoice = bookKeeper.issuance( invoiceRequest, taxPolicy );

        assertThat( invoice.getItems().size(), is( 1 ) );
    }

    @Test
    public void emptyInvoiceRequestShouldCallCalculateTexZeroTimes() {
        bookKeeper.issuance( invoiceRequest, taxPolicy );

        verify( taxPolicy, times( 0 ) ).calculateTax( any( ProductType.class ), any( Money.class ) );
    }

    @Test
    public void invoiceRequestWithTwoFieldShouldCallCalculateTexTwoTimes() {
        ProductData productData1 = mock( ProductData.class );
        ProductData productData2 = mock( ProductData.class );
        RequestItem requestItem1 = new RequestItem( productData1, 1, price );
        RequestItem requestItem2 = new RequestItem( productData2, 2, price );
        invoiceRequest.add( requestItem1 );
        invoiceRequest.add( requestItem2 );
        bookKeeper.issuance( invoiceRequest, taxPolicy );
        verify( taxPolicy, times( 2 ) ).calculateTax( any( ProductType.class ), any( Money.class ) );
    }
}