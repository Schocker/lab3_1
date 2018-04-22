package lab3_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation.ReservationStatus;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@RunWith(MockitoJUnitRunner.class) 

public class AddProductCommandHandlerTests {

	private ReservationRepository mockedReservationRepository;
	private ProductRepository mockedProductRepository;
	private SuggestionService mockedSuggestionService;
	private ClientRepository mockedClientRepository;
	private AddProductCommand addProductCommand;
	private AddProductCommandHandler addProductCommandHandler;
	private Reservation reservation;
	private Product product;
	private ClientData clientData;
	private Client client;
	private Date date;
	
	@Before
	public void setUp() {
		mockedReservationRepository = mock(ReservationRepository.class);
		mockedProductRepository = mock(ProductRepository.class);
		mockedSuggestionService = mock(SuggestionService.class);
		mockedClientRepository = mock(ClientRepository.class);
		addProductCommandHandler = new AddProductCommandHandler();
		addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
		client = new Client();
		date = new Date();
		clientData = new ClientData(Id.generate(), "John Doe");
		reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, clientData, new Date());		
		product = new Product(Id.generate(), new Money(50.0), "Kalafior", ProductType.STANDARD);
		
		Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", mockedReservationRepository);
		Whitebox.setInternalState(addProductCommandHandler, "productRepository", mockedProductRepository);
		Whitebox.setInternalState(addProductCommandHandler, "clientRepository", mockedClientRepository);
		
		when(mockedReservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);		
	}

}
