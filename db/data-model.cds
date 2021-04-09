namespace my.models;

using {
    my.models as my,
    managed,
    cuid
} from '@sap/cds/common';

entity Users : cuid, managed{
    key ID : Integer;
        name : String;
		password : String;
        pets : Association to many Pets on pets.user = $self;
}

entity Pets : cuid, managed{
    key ID     : Integer;
        name   : String;
        type   : String enum {CAT; DOG};
        user   : Association to one Users;
        cat   : Composition of one Cats
                             on cat.parent = $self;
        dog   : Composition of one Dogs
                             on dog.parent = $self;}

entity Cats {
        parent     : Association to Pets;
        clippedClaws  : Boolean;
}

entity Dogs {
        parent : Association to Pets;
        pawColour   : String enum {PINK; BLACK};
}