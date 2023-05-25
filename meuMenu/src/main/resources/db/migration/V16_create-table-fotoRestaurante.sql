CREATE TABLE fotoRestaurante (
	                         id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                             fkRestaurante INT,
                             nomeFoto VARCHAR(110),
                             fachada BOOLEAN,
                             interior BOOLEAN,
                             FOREIGN KEY (fkRestaurante)
                             REFERENCES restaurante (id)
);

alter table cardapio add column foto_prato varchar(110);
alter table usuario add column foto_perfil varchar(110);
