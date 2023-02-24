CREATE TABLE usuario (
                         idUsuario int primary key not null auto_increment,
                         nome VARCHAR(45),
                         sobrenome VARCHAR(45),
                         cpf CHAR(11),
                         email VARCHAR(99),
                         senha VARCHAR(99),
                         tipo_comida_preferida VARCHAR(45)
);