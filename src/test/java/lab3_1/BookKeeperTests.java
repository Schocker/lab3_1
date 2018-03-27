package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
		bookKeeper.issuance(invoiceRequest, mockedTaxPolicy);
	}
	
	@Test
	public void bookKeeperBehaviourTest() {
		fail("Not yet implemented");
	}

}
