CREATE TABLE usuario (
                         idUsuario int primary key not null auto_increment,
                         nome VARCHAR(45),
                         sobrenome VARCHAR(45),
                         cpf CHAR(11),
                         email VARCHAR(99),
                         senha VARCHAR(99),
                         tipo_comida_preferida VARCHAR(45)
);


CREATE TABLE restaurante (
                             idRestaurante INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                             usuario INT NOT NULL,
                             nome VARCHAR(45) NULL,
                             cnpj CHAR(14) NULL,
                             especialidade VARCHAR(45) NULL,
                             telefone VARCHAR(20) NULL,
                             site VARCHAR(99) NULL,
                             estrela int NULL,
                             FOREIGN KEY (usuario)
                                 REFERENCES usuario (idUsuario));