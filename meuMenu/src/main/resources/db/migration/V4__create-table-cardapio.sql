create table cardapio(
    id int primary key auto_increment,
    fk_restaurante int,
    foreign key (fk_restaurante) references restaurante (id),
    nome varchar(50),
    preco double,
    estiloGastronomico varchar(20)
)