create class Person extends V

create property Person.personId string
create index UQ_Person_personId on Person (personId) unique
alter property Person.personId notnull true
alter property Person.personId mandatory true
alter property Person.personId default uuid()

create class Parent extends E
