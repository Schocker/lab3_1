package lab3_1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@RunWith(MockitoJUnitRunner.class) 

public class BookKeeperTests {
	
	private InvoiceFactory invoiceFactory;	
	private BookKeeper bookKeeper;
	private TaxPolicy mockedTaxPolicy;
	private InvoiceRequest invoiceRequest;
	private ProductData productData;
		
	@Before
	public void setUp() throws Exception {
		bookKeeper = new BookKeeper(new InvoiceFactory());
		mockedTaxPolicy = mock(TaxPolicy.class);
		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
		Money moneyEUR = new Money(125.0);
		Date snapshotDate = new Date();
		productData = new ProductData(Id.generate(), moneyEUR, "Kartofel", ProductType.FOOD, snapshotDate);
	}

	@Test
	public void requestingOneInvoiceShouldReturnInvoiceWithOnePosition() {
		when(mockedTaxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(new Tax(new Money(0.50), "Item Tax"));		 
		RequestItem requestItem = new RequestItem(productData, 5, productData.getPrice());
		invoiceRequest.add(requestItem);		
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockedTaxPolicy);		
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void bookKeeperBehaviourTest() {
		fail("Not yet implemented");
	}

}
