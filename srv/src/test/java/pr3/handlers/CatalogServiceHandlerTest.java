package pr3.handlers;

import cds.gen.catalogservice.AttachUserContext;
import cds.gen.catalogservice.ChangeUserContext;
import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Users;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.provider.IdProvider;
import pr3.services.PetService;
import pr3.services.UserService;
import pr3.validators.CatalogServiceValidator;

import java.util.Collections;
import java.util.List;

import static cds.gen.catalogservice.ChangeUserContext.create;
import static cds.gen.catalogservice.Users_.CDS_NAME;
import static com.sap.cds.ql.impl.SelectBuilder.from;
import static java.util.Collections.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.CAT;
import static pr3.utils.TestUtils.createPet;
import static pr3.utils.TestUtils.createPets;
import static pr3.utils.TestUtils.createUser;
import static pr3.utils.TestUtils.invalidId;
import static pr3.utils.TestUtils.validId;

public class CatalogServiceHandlerTest {

    @Mock
    private PetService petService;

    @Mock
    private UserService userService;

    @Mock
    private IdProvider idProvider;

    @Mock
    private CatalogServiceValidator catalogServiceValidator;

    private CatalogServiceHandler catalogServiceHandler;

    @Before
    public void setUp() {
        initMocks(this);
        catalogServiceHandler =
                new CatalogServiceHandler(petService, userService, idProvider, catalogServiceValidator);
    }

    @Test
    public void onChangeUser_GivenContext_ShouldAttachUserToRequiredPet() {
        //Given
        Users user = createUser();
        Integer userId = user.getId();

        Pets pet = createPet(CAT);
        pet.setUserId(validId());
        Integer petId = pet.getId();

        ChangeUserContext context = create();
        context.setUser(user);
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(petId);
        when(petService.getPet(petId)).thenReturn(pet);
        doNothing().when(catalogServiceValidator).checkAttaching(pet.getUserId(), userId);
        pet.setUserId(userId);
        when(petService.updatePet(pet)).thenReturn(pet);
        //When
        catalogServiceHandler.onChangeUser(context);
        //Then
        verify(petService, times(1)).updatePet(pet);
        verify(petService, times(1)).getPet(petId);
        verify(idProvider, times(1)).getId(any(CqnSelect.class));
    }

    @Test(expected = ServiceException.class)
    public void onChangeUser_GivenContextWithInvalidUserId_ShouldTrowServiceException() {
        //Given
        Users user = createUser();
        user.setId(invalidId());

        Pets pet = createPet(CAT);
        pet.setUserId(validId());
        Integer petId = pet.getId();

        ChangeUserContext context = create();
        context.setUser(user);
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(petId);
        when(petService.getPet(petId)).thenReturn(pet);
        doThrow(new ServiceException("User not found or doesn't exist"))
                .when(catalogServiceValidator).checkUserExistence(user.getId());
        //When
        catalogServiceHandler.onChangeUser(context);
    }

    @Test(expected = ServiceException.class)
    public void onChangeUser_GivenContextWithInvalidPetId_ShouldTrowServiceException() {
        //Given
        Users user = createUser();

        Pets pet = createPet(CAT);
        pet.setUserId(validId());
        pet.setId(invalidId());

        ChangeUserContext context = create();
        context.setUser(user);
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(pet.getId());
        when(petService.getPet(pet.getId()))
                .thenThrow(new ServiceException("Pet not found or doesn't exist"));
        //When
        catalogServiceHandler.onChangeUser(context);
    }


    @Test
    public void onAttachUser_GivenContext_ShouldAttachUserToRequiredTypeOfPets() {
        //Given
        Users user = createUser();
        String type = CAT;
        List<Pets> pets = createPets();
        Pets updatedPet = createPet(type);
        pets.forEach(pet -> pet.setType(type));

        AttachUserContext context = AttachUserContext.create();
        context.setType(type);
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(user.getId());
        when(userService.getUser(user.getId())).thenReturn(user);
        when(petService.getStrangersTypedPets(type, user.getId())).thenReturn(pets);
        doNothing().when(catalogServiceValidator).checkAttaching(any(Integer.class), any(Integer.class));
        when(petService.updatePet(any(Pets.class))).thenReturn(updatedPet);
        //When
        catalogServiceHandler.onAttachUser(context);
        //Then
        verify(idProvider, times(1)).getId(any());
        verify(userService, times(1)).getUser(user.getId());
        verify(petService, times(1)).getStrangersTypedPets(type, user.getId());
        verify(petService, times(pets.size())).updatePet(any(Pets.class));
    }

    @Test(expected = ServiceException.class)
    public void onAttachUser_GivenContextWithInvalidUserId_ShouldTrowServiceException() {
        //Given
        Users user = createUser();
        user.setId(invalidId());

        AttachUserContext context = AttachUserContext.create();
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(user.getId());
        when(userService.getUser(user.getId()))
                .thenThrow(new ServiceException("User not found or doesn't exist"));
        //When
        catalogServiceHandler.onAttachUser(context);
    }

    @Test(expected = ServiceException.class)
    public void onAttachUser_GivenContextWithInvalidPetType_ShouldTrowServiceException() {
        //Given
        Users user = createUser();
        String type = "invalidType";
        List<Pets> pets = emptyList();

        AttachUserContext context = AttachUserContext.create();
        context.setType(type);
        context.setCqn(from(CDS_NAME).asSelect());

        when(idProvider.getId(any(CqnSelect.class))).thenReturn(user.getId());
        when(userService.getUser(user.getId())).thenReturn(user);
        when(petService.getStrangersTypedPets(type, user.getId())).thenReturn(pets);
        doThrow(new ServiceException("Pets not found or don't exist"))
                .when(catalogServiceValidator).checkPetsExistence(pets, type);
        //When
        catalogServiceHandler.onAttachUser(context);
    }
}
