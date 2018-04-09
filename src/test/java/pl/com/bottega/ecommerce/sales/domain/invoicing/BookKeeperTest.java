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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
                //.thenThrow( new IllegalArgumentException(  ) );
                //.thenReturn( new Tax( new Money( 1.25 ), "Second item Tax" ) );
    }

    @Test
    public void invoiceWithOneFieldShouldReturnInvoiceWithOneField() {

        ProductData productData = mock( ProductData.class );
        RequestItem requestItem = new RequestItem( productData, 1, price );

        invoiceRequest.add( requestItem );
        //when(taxPolicy.calculateTax(ProductType.FOOD, requestItem.getTotalCost())) .thenReturn(new Tax(new Money(0.25), "Item Tax"));
        Invoice invoice = bookKeeper.issuance( invoiceRequest, taxPolicy );

        assertThat( invoice.getItems().size(), is( 1 ) );

    }

    @Test
    public void invoiceWithTwoFieldShouldCallCalculateTexTwoTimes() {

    }

}