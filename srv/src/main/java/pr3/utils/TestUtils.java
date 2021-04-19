package pr3.utils;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.random;
import static java.util.Arrays.*;

public class TestUtils {

    public static Integer invalidId() {
        return (int) (-random());
    }

    public static Integer validId() {
        return (int) (random() * 1000);
    }

    public static Users createUser(){
        Integer id = validId();
        Users user = Users.create();
        user.setId(id);
        user.setName("User" + id);
        return user;
    }

    public static Pets createPet(String type){
        Integer id = validId();
        Pets pet = Pets.create();
        pet.setId(id);
        pet.setType(type);
        pet.setName("Pet" + id);
        return pet;
    }
    public static List<Pets> createPets(){
        return asList(
                createPet("CAT"),
                createPet("CAT"),
                createPet("DOG"),
                createPet("DOG"));
    }
}
