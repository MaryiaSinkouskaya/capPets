using my.models as my from '../db/data-model';

service CatalogService {

   entity Cats as SELECT from my.Cats;
   entity Dogs as SELECT from my.Dogs;

    view viewCats as select from Pets JOIN Cats on parent.ID=Pets.ID {
      Pets.ID, type, name, Cats.clippedClaws, Pets.user.ID as user_id
    } where Pets.type='CAT';

    view viewDogs as select from Pets JOIN Dogs on parent.ID=Pets.ID {
      Pets.ID, type, name, Dogs.pawColour, Pets.user.ID as user_id
    } where Pets.type='DOG';

    entity Pets as projection on my.Pets actions {
            action changeUser(user : Users) returns Pets;
    };

    entity Users as projection on my.Users actions {
            action attachUser(type : String) returns Users;
    };

}