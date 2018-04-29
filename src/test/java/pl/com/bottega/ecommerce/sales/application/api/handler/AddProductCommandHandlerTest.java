package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import java.util.Date;

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
    @Mock
    private SystemContext systemContextMock;

    private Product product;
    private Client client;

    @Before
    public void setUp() {
        Reservation reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
                new ClientData(Id.generate(), "Test"), new Date());
        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(reservation);

        product = new ProductBuilder().build();
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);

        client = new Client();
        when(clientRepositoryMock.load(any(Id.class))).thenReturn(client);

        when(systemContextMock.getSystemUser()).thenReturn(new SystemUser(Id.generate()));

        when(suggestionServiceMock.suggestEquivalent(any(Product.class), any(Client.class))).thenReturn(new ProductBuilder().build());
    }

    @Test
    public void addingAProductShouldGetReservationFromRepository() {
        addProductCommandHandler.handle(new AddProductCommandBuilder().build());

        verify(reservationRepositoryMock, times(1)).load(any(Id.class));
    }

    @Test
    public void addingAProductShouldGetProductFromRepository() {
        addProductCommandHandler.handle(new AddProductCommandBuilder().build());

        verify(productRepositoryMock, times(1)).load(any(Id.class));
    }

    @Test
    public void addingAnAvailableProductShouldNotSuggestAProduct() {
        addProductCommandHandler.handle(new AddProductCommandBuilder().build());

        verify(suggestionServiceMock, never()).suggestEquivalent(any(Product.class), any(Client.class));
    }

    @Test
    public void addingAnAvailableProductShouldSaveTheReservation() {
        addProductCommandHandler.handle(new AddProductCommandBuilder().build());

        verify(reservationRepositoryMock, times(1)).save(any(Reservation.class));
    }

    @Test
    public void addingANotAvailableProductShouldSuggestAProduct() {
        product.markAsRemoved();
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);

        addProductCommandHandler.handle(new AddProductCommandBuilder().build());
        verify(suggestionServiceMock, times(1)).suggestEquivalent(any(Product.class), any(Client.class));
    }

    @Test
    public void addingANotAvailableProductShouldSaveTheReservation() {
        product.markAsRemoved();
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);

        client = new Client();
        when(clientRepositoryMock.load(any(Id.class))).thenReturn(client);
        when(systemContextMock.getSystemUser()).thenReturn(new SystemUser(Id.generate()));

        addProductCommandHandler.handle(new AddProductCommandBuilder().build());
        verify(reservationRepositoryMock, times(1)).save(any(Reservation.class));
    }
}