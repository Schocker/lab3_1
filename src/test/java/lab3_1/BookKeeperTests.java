package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@RunWith(MockitoJUnitRunner.class) 

public class BookKeeperTests {
	
	private BookKeeper keeper;	
	private InvoiceFactory invoiceFactory;	
	BookKeeper bookKeeper;
	TaxPolicy mockedTaxPolicy;
	InvoiceRequest invoiceRequest;
		
	@Before
	public void setUp() throws Exception {
		bookKeeper = new BookKeeper(new InvoiceFactory());
		mockedTaxPolicy = mock(TaxPolicy.class);
		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
		productData = new 
	}

	@Test
	public void requestingOneInvoiceShouldReturnInvoiceWithOnePosition() {
		keeper.issuance(invoiceRequest, mockedTaxPolicy);
	}
	
	@Test
	public void bookKeeperBehaviourTest() {
		fail("Not yet implemented");
	}

}
