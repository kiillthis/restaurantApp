create table comment_dish (
       id integer not null auto_increment,
        comment_dish varchar(255),
        dish_id integer,
        user_id integer,
        primary key (id)
    ) engine=InnoDB;

    create table comment_restaurant (
       id integer not null auto_increment,
        comment_rest varchar(255),
        restaurant_id integer,
        user_id integer,
        primary key (id)
    ) engine=InnoDB;

    create table cuisine (
       id integer not null auto_increment,
        cuisine_name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table cuisine_restaurant (
       cuisine_id integer not null,
        restaurant_id integer not null,
        primary key (cuisine_id, restaurant_id)
    ) engine=InnoDB;

    create table dish (
       id integer not null auto_increment,
        description varchar(255),
        dish_name varchar(255),
        price double precision,
        menu_id integer,
        primary key (id)
    ) engine=InnoDB;

    create table dish_ingredient (
       dish_id integer not null,
        ingredient_id integer not null,
        primary key (dish_id, ingredient_id)
    ) engine=InnoDB;

    create table ingredient (
       id integer not null auto_increment,
        ingredient_name varchar(255),
        weight double precision,
        primary key (id)
    ) engine=InnoDB;

    create table menu (
       id integer not null auto_increment,
        description varchar(255),
        restaurant_id integer,
        primary key (id)
    ) engine=InnoDB;

    create table restaurant (
       id integer not null auto_increment,
        address varchar(255),
        description varchar(255),
        restaurant_name varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table user (
       id integer not null auto_increment,
        first_name varchar(255),
        last_name varchar(255),
        login varchar(255),
        password varchar(255),
        primary key (id)
    ) engine=InnoDB;