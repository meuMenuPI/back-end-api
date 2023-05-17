CREATE TABLE endereco(
	`id` INT auto_increment ,
	`fk_restaurante` INT ,
	`fk_usuario` INT ,
	`cep` VARCHAR(45) ,
	`numero` VARCHAR(45) ,
	`complemento` VARCHAR(45) ,
	PRIMARY KEY (`id`),
    FOREIGN KEY (`fk_restaurante`)
    REFERENCES `restaurante` (`id`),
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `usuario` (`id`)
    );