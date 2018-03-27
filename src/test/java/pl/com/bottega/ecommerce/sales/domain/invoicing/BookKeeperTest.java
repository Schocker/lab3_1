package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BookKeeperTest {


    @Mock
    TaxPolicy taxPolicy;
//    @Mock
    InvoiceRequest invoiceRequest;
//    @Mock
    RequestItem requestItem;

    InvoiceFactory invoiceFactory;
    BookKeeper keepBooker;

    @Before
    public void setUp() throws Exception {
        invoiceFactory = new InvoiceFactory();
        keepBooker = new BookKeeper(invoiceFactory);

    }

    @Test
    public void invoiceWithOneFieldShouldReturnOneField() {

//        ClientData clientData;

        Integer expected = 1;

        ClientData clientData = new ClientData(Id.generate(),"Nazwa_usera");
//        ProductData productData = new ProductData(Id.generate(),new Money(123.4), "Warzywo", new Date(1), ProductType.FOOD);
        int quantity = 1;
//        requestItem = new RequestItem(productData, quantity,new Money(123.23));
        invoiceRequest = new InvoiceRequest(clientData);
        invoiceRequest.add(requestItem);

        Invoice invoice = keepBooker.issuance(invoiceRequest, taxPolicy);
        //verify(invoice);
        assertThat(invoice.getItems().size(), is(expected));

    }

    @Test
    public void invoiceWithTwoFieldShouldCallCalculateTexTwoTimes() {
        Invoice invoice = keepBooker.issuance(invoiceRequest, taxPolicy);

    }
//    mockito when
//    matcher mockito.any()
//mockito.verify(user.save()).same()
//
//    ArgumentCaptor
//    https://static.javadoc.io/org.mockito/mockito-core/2.6.9/org/mockito/ArgumentCaptor.html
//
//    https://www.mkyong.com/unittest/junit-4-tutorial-2-expected-exception-test/
//    ExpectedException
//
//    Assert.notNull
//-zły kod
//-błąd wywołany brakiem podpięcia bazy danych.
}