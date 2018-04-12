package pl.com.bottega.ecommerce.sales.application.api.handler;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {
    private AddProductCommandHandler handler;
    private AddProductCommand command;
    private Id orderId;
    private Id productId;

    @Mock
    private ReservationRepository reservationRepositoryMock;
    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private SuggestionService suggestionServiceMock;
    @Mock
    private ClientRepository clientRepositoryMock;

    @Before
    public void setUp() {
        handler = new AddProductCommandHandler();
        orderId = Id.generate();
        productId = Id.generate();
        command = new AddProductCommand(orderId, productId, 10);

        Reservation reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED,
                                                new ClientData(Id.generate(), "Test Client"), new Date());
        when(reservationRepositoryMock.load(any(Id.class))).thenReturn(reservation);


        Product product = new Product(Id.generate(), new Money(10),
                                    "test product name", ProductType.FOOD);
        when(productRepositoryMock.load(any(Id.class))).thenReturn(product);

        Whitebox.setInternalState(handler, "reservationRepository", reservationRepositoryMock);
        Whitebox.setInternalState(handler, "productRepository", productRepositoryMock);
    }

    @Test
    public void handleShouldCallReservationRepositoryLoad() {
        handler.handle(command);

        ArgumentCaptor<Id> argumentCaptor = ArgumentCaptor.forClass(Id.class);
        verify(reservationRepositoryMock, times(1)).load(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(command.getOrderId()));
    }

    @Test
    public void handleShouldCallProductRepositoryLoad() {
        handler.handle(command);

        ArgumentCaptor<Id> argumentCaptor = ArgumentCaptor.forClass(Id.class);
        verify(productRepositoryMock, times(1)).load(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(command.getProductId()));
    }

}

//should call reservation (mock).add
//should call repo.save

//if not available
    //should call loadclient
    //should call suggestEquiv

//should not call them when available

