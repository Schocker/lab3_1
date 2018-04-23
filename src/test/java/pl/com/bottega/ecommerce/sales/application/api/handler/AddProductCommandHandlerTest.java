package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {
    @InjectMocks
    private AddProductCommandHandler addProductCommandHandler;
    @Mock
    private ReservationRepository reservationRepositoryMock;
    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private SuggestionService suggestionServiceMock;
    @Mock
    private ClientRepository clientRepositoryMock;
    Reservation reservation;
    Product product;

    @Before
    public void setUp() {
        reservation =  new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
                                new ClientData(Id.generate(), "Test"), new Date());
        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(reservation);

        product = new Product(Id.generate(), new Money(1), "Test Product", ProductType.STANDARD);
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);
    }

    @Test
    public void addingAnAvailableProductShouldGetReservationFromRepository() {
        addProductCommandHandler.handle(new AddProductCommand(Id.generate(), Id.generate(), 1));

        verify(reservationRepositoryMock, times(1)).load(any(Id.class));
    }

    @Test
    public void addingAnAvailableProductShouldGetProductFromRepository() {
        addProductCommandHandler.handle(new AddProductCommand(Id.generate(), Id.generate(), 1));

        verify(productRepositoryMock, times(1)).load(any(Id.class));
    }

    @Test
    public void addingAnAvailableProductShouldNotSuggestAProduct() {
        addProductCommandHandler.handle(new AddProductCommand(Id.generate(), Id.generate(), 1));

        verify(suggestionServiceMock, never()).suggestEquivalent(any(Product.class), any(Client.class));
    }

}