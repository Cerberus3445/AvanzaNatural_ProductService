create table if not exists Categories(
    id int generated by default as identity primary key ,
    title varchar(40) not null unique,
    image varchar
);

create table if not exists Subcategories(
    id int generated by default as identity primary key ,
    category_id int references Categories(id) on delete cascade,
    title varchar(40) not null,
    image varchar
);

create table if not exists Products_Types(
    id int generated by default as identity primary key ,
    subcategory_id int references Subcategories(id) on delete cascade ,
    title varchar(40) not null,
    image varchar
);

create table if not exists Products(
    id int generated by default as identity primary key ,
    title varchar(60) not null ,
    brand varchar(60) not null ,
    description varchar(1000) ,
    price float not null ,
    in_stock boolean not null,
    category_id int references Categories(id) on delete cascade,
    subcategory_id int references Subcategories(id) on delete cascade,
    product_type_id int references Products_Types(id) on delete cascade
);