package pl.com.bottega.ecommerce.sales.application.api.handler;

import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {


    AddProductCommandHandler addProductCommandHandler;
    ReservationRepository reservationRepository;
    ProductRepository productRepository;
    ClientRepository clientRepository;
    SuggestionService suggestionService;
    SystemContext systemContext = new SystemContext();
    Reservation reservation;
    Product product;
    Client client;

    @Before
    public void setUp() throws Exception {
        addProductCommandHandler = new AddProductCommandHandler();
        reservationRepository = mock( ReservationRepository.class );
        productRepository = mock( ProductRepository.class );
        clientRepository = mock( ClientRepository.class );
        suggestionService = mock( SuggestionService.class );
        reservation = mock( Reservation.class );
        product = mock( Product.class );
        client = mock( Client.class );

        try {
            Field reservationRepositoryField = AddProductCommandHandler.class.getDeclaredField( "reservationRepository" );
            reservationRepositoryField.setAccessible( true );
            reservationRepositoryField.set( addProductCommandHandler, reservationRepository );

            Field productRepositoryField = AddProductCommandHandler.class.getDeclaredField( "productRepository" );
            productRepositoryField.setAccessible( true );
            productRepositoryField.set( addProductCommandHandler, productRepository );

            Field clientRepositoryField = AddProductCommandHandler.class.getDeclaredField( "clientRepository" );
            clientRepositoryField.setAccessible( true );
            clientRepositoryField.set( addProductCommandHandler, clientRepository );

            Field suggestionServiceField = AddProductCommandHandler.class.getDeclaredField( "suggestionService" );
            suggestionServiceField.setAccessible( true );
            suggestionServiceField.set( addProductCommandHandler, suggestionService );

        } catch (Exception e) {
            e.printStackTrace();
        }

        when( reservationRepository.load( any( Id.class ) ) ).thenReturn( reservation );
        when( productRepository.load( any( Id.class ) ) ).thenReturn( product );
        when( clientRepository.load( any( Id.class ) ) ).thenReturn( client );
        when( suggestionService.suggestEquivalent( any( Product.class ), any( Client.class ) ) ).thenReturn( product );
    }

    @Test
    public void saveInHandleMethodShouldCallOneSave() throws Exception {

        AddProductCommand addProductCommand = new AddProductCommand( new Id( "1" ), new Id( "2" ), 1 );
        when( product.isAvailable() ).thenReturn( true );

        addProductCommandHandler.handle( addProductCommand );
        verify( reservationRepository, times( 1 ) ).save( any( Reservation.class ) );
    }

}