package pl.com.bottega.ecommerce.sales.application.api.handler;

import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationItem;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {


    AddProductCommandHandler addProductCommandHandler;
    ReservationRepository reservationRepository;
    ProductRepository productRepository;
    ClientRepository clientRepository;
    SystemContext systemContext;
    SuggestionService suggestionService;
    Reservation reservation;
    Product product;
    Client client;
    Field access;

    @Before
    public void setUp() throws Exception {
        addProductCommandHandler = new AddProductCommandHandler();
        reservationRepository = mock( ReservationRepository.class );
        productRepository = mock( ProductRepository.class );
        clientRepository = mock( ClientRepository.class );
        systemContext = new SystemContext();
        suggestionService = mock( SuggestionService.class );
        reservation = mock( Reservation.class );
        product = mock( Product.class );
        client = mock( Client.class );

        try {
            access = AddProductCommandHandler.class.getDeclaredField( "reservationRepository" );
            access.setAccessible( true );
            access.set( addProductCommandHandler, reservationRepository );

            access = AddProductCommandHandler.class.getDeclaredField( "productRepository" );
            access.setAccessible( true );
            access.set( addProductCommandHandler, productRepository );

            access = AddProductCommandHandler.class.getDeclaredField( "clientRepository" );
            access.setAccessible( true );
            access.set( addProductCommandHandler, clientRepository );

            access = AddProductCommandHandler.class.getDeclaredField( "suggestionService" );
            access.setAccessible( true );
            access.set( addProductCommandHandler, suggestionService );

            access = AddProductCommandHandler.class.getDeclaredField( "systemContext" );
            access.setAccessible( true );
            access.set( addProductCommandHandler, systemContext );

        } catch (Exception e) {
            e.printStackTrace();
        }

        when( reservationRepository.load( any( Id.class ) ) ).thenReturn( reservation );
        when( productRepository.load( any( Id.class ) ) ).thenReturn( product );
        when( clientRepository.load( any( Id.class ) ) ).thenReturn( client );
        when( suggestionService.suggestEquivalent( any( Product.class ), any( Client.class ) ) ).thenReturn( product );
    }
    @Test
    public void handleMethodShouldCallOneTimeLoadReservation() {

        AddProductCommand addProductCommand = new AddProductCommand( new Id( "1" ), new Id( "2" ), 1 );
        //when( product.isAvailable() ).thenReturn( true );

        addProductCommandHandler.handle( addProductCommand );
        verify( reservationRepository, times( 1 ) ).load( any( Id.class ) );
    }

    @Test
    public void handleMethodShouldCallOneTimeSaveReservation() {

        AddProductCommand addProductCommand = new AddProductCommand( new Id( "1" ), new Id( "2" ), 1 );
        when( product.isAvailable() ).thenReturn( true );

        addProductCommandHandler.handle( addProductCommand );
        verify( reservationRepository, times( 1 ) ).save( any( Reservation.class ) );
    }
    @Test
    public void handleMethodWhenProductIsUnavailableShouldCallOneTimeSaveReservation(){

        AddProductCommand addProductCommand = new AddProductCommand( new Id( "1" ), new Id( "2" ), 2 );
        when( product.isAvailable() ).thenReturn( false );

        addProductCommandHandler.handle( addProductCommand );
        verify( reservationRepository, times( 1 ) ).save( any( Reservation.class ) );
    }

}