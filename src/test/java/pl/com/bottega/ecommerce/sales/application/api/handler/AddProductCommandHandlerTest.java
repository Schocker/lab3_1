package pl.com.bottega.ecommerce.sales.application.api.handler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;


public class AddProductCommandHandlerTest {
    
    private AddProductCommandHandler addProductCommandHandler;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private SystemContext systemContext;
    private Reservation reservation;
    private Product product;
    private Field field;
    
    @Before
    public void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        systemContext = new SystemContext();
        product = mock(Product.class);
        reservation = mock(Reservation.class);
        addProductCommandHandler = new AddProductCommandHandler();
        try {
            field = AddProductCommandHandler.class.getDeclaredField("reservationRepository");
            field.setAccessible(true);
            field.set(addProductCommandHandler, reservationRepository);
            field = AddProductCommandHandler.class.getDeclaredField("productRepository");
            field.setAccessible(true);
            field.set(addProductCommandHandler, productRepository);
            field = AddProductCommandHandler.class.getDeclaredField("suggestionService");
            field.setAccessible(true);
            field.set(addProductCommandHandler, suggestionService);
            field = AddProductCommandHandler.class.getDeclaredField("clientRepository");
            field.setAccessible(true);
            field.set(addProductCommandHandler, clientRepository);
            field = AddProductCommandHandler.class.getDeclaredField("systemContext");
            field.setAccessible(true);
            field.set(addProductCommandHandler, systemContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        when(clientRepository.load(any(Id.class))).thenReturn(new Client());
        when(suggestionService.suggestEquivalent(any(Product.class), any(Client.class))).thenReturn(product);
    }
    
    @Test
    public void handlingAvailableProductShouldCallOnceSaveOnReservationRepository() {
        AddProductCommand command = new AddProductCommand(anyId(), anyId(), 1);
        when(product.isAvailable()).thenReturn(true);
        addProductCommandHandler.handle(command);
        verify(reservationRepository, times(1)).save(reservation);
    }
    
    @Test
    public void handlingNotAvailableProductShouldCallOnceSaveOnReservationRepository() {
        AddProductCommand command = new AddProductCommand(anyId(), anyId(), 1);
        when(product.isAvailable()).thenReturn(false);
        addProductCommandHandler.handle(command);
        verify(reservationRepository, times(1)).save(reservation);
    }
    
    private Id anyId() {
        return new Id("1");
    }
    
}
