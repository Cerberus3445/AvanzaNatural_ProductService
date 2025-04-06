create schema if not exists products;

create table if not exists products.category(
                                                id int generated by default as identity primary key ,
                                                title varchar(40) not null unique,
                                                image varchar
);

create table if not exists products.subcategory(
                                                   id int generated by default as identity primary key ,
                                                   category_id int references products.category(id) on delete cascade,
                                                   title varchar(40) not null,
                                                   image varchar
);

create table if not exists products.product_type(
                                                    id int generated by default as identity primary key ,
                                                    subcategory_id int references products.subcategory(id) on delete cascade ,
                                                    title varchar(40) not null,
                                                    image varchar
);

create table if not exists products.product(
                                               id int generated by default as identity primary key ,
                                               title varchar(60) not null ,
                                               brand varchar(60) not null ,
                                               description varchar(1000) ,
                                               price float not null ,
                                               in_stock boolean not null,
                                               category_id int references products.category(id) on delete cascade,
                                               subcategory_id int references products.subcategory(id) on delete cascade,
                                               product_type_id int references products.product_type(id) on delete cascade
);

-- this is for tests
insert into products.category(title, image) values ('Water','Image');
insert into products.category(title, image) values ('Chocolate','Image');
insert into products.category(title, image) values ('Meat','Image');
insert into products.category(title, image) values ('Clothes', 'Image');

insert into products.subcategory(category_id, title, image) values (4,'T-shirts','Image');
insert into products.subcategory(category_id, title, image) values (4,'Skirts','Image');
insert into products.subcategory(category_id, title, image) values (4,'Trousers','Image');

insert into products.product_type(subcategory_id, title, image) values (1,'Orange T-Shirt','Image');
insert into products.product_type(subcategory_id, title, image) values (1,'Blue T-Shirt','Image');
insert into products.product_type(subcategory_id, title, image) values (1,'Ping T-Shirt','Image');

insert into products.product(title, brand, description, price, in_stock, category_id, subcategory_id, product_type_id) values ('First Product', 'brand', 'description',19.9,true,4,1,1);
insert into products.product(title, brand, description, price, in_stock, category_id, subcategory_id, product_type_id) values ('Second Product', 'brand', 'description',19.9,true,4,1,1);
insert into products.product(title, brand, description, price, in_stock, category_id, subcategory_id, product_type_id) values ('Third Product', 'brand', 'description',19.9,true,4,1,1);