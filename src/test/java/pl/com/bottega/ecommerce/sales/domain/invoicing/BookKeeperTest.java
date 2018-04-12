package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    private RequestItem requestItemOne;
    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private RequestItem requestItemTwo;

    @Before
    public void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());

        ProductData productDataOne = new ProductData(Id.generate(), new Money(10), "product name one",
                ProductType.FOOD, new Date());
        requestItemOne = new RequestItem(productDataOne, 10, new Money(100));

        ProductData productDataTwo = new ProductData(Id.generate(), new Money(50), "product name two",
                ProductType.DRUG, new Date());
        requestItemTwo = new RequestItem(productDataTwo, 3, new Money(150));

        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "test client"));
    }

    @Test
    public void issuanceShouldReturnInvoiceWithOnePositionWhenOnePositionRequestGiven() {
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(5), "test tax");
            }
        };

        invoiceRequest.add(requestItemOne);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(1));
    }

    @Test
    public void issuanceShouldReturnEmptyInvoiceWhenZeroPositionRequestGiven() {
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(5), "test tax");
            }
        };

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems().size(), is(0));
    }

    @Test
    public void issuanceShouldCallTaxPolicyTwiceWhenTwoPositionRequestGiven() {
        TaxPolicy taxPolicyMock = mock(TaxPolicy.class);

        invoiceRequest.add(requestItemOne);
        invoiceRequest.add(requestItemTwo);

        when(taxPolicyMock.calculateTax(
                Mockito.any(ProductType.class), Mockito.any(Money.class))).
                thenReturn(new Tax(new Money(5), "test tax"));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        ArgumentCaptor<ProductType> argumentCaptorProductType = ArgumentCaptor.forClass(ProductType.class);
        ArgumentCaptor<Money> argumentCaptorMoney = ArgumentCaptor.forClass(Money.class);

        verify(taxPolicyMock, times(2)).
                calculateTax(argumentCaptorProductType.capture(), argumentCaptorMoney.capture());

        List<ProductType> capturedArgumentProductType = argumentCaptorProductType.getAllValues();
        assertThat(capturedArgumentProductType.get(0), is(ProductType.FOOD));
        assertThat(capturedArgumentProductType.get(1), is(ProductType.DRUG));

        List<Money> capturedArgumentMoney = argumentCaptorMoney.getAllValues();
        assertThat(capturedArgumentMoney.get(0), is(new Money(100)));
        assertThat(capturedArgumentMoney.get(1), is(new Money(150)));
    }

    @Test
    public void issuanceShouldNotCallTaxPolicyWhenZeroRequestGiven() {
        TaxPolicy taxPolicyMock = mock(TaxPolicy.class);

        when(taxPolicyMock.calculateTax(
                Mockito.any(ProductType.class), Mockito.any(Money.class))).
                thenReturn(new Tax(new Money(5), "test tax"));

        bookKeeper.issuance(invoiceRequest, taxPolicyMock);

        verify(taxPolicyMock,  never()).
                calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
    }
}
