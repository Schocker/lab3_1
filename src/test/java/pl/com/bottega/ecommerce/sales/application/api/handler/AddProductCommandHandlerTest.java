package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() throws Exception {
        Reservation reservation =  new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
                                new ClientData(Id.generate(), "Test"), new Date());
        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(reservation);
    }

    @Test
    public void addingValidProductShouldAddItToReservation() {

    }
}