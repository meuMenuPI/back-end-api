CREATE TABLE  `review` (
	`dataHora` datetime,
	`fk_restaurante` INT ,
	`fk_usuario` INT ,
	`descricao` VARCHAR(200) ,
	`nt_comida` DOUBLE ,
	`nt_ambiente` DOUBLE ,
	`nt_atendimento` DOUBLE ,
	`foto` VARCHAR(45) ,
	PRIMARY KEY (`dataHora`, `fk_restaurante`, `fk_usuario`),
    FOREIGN KEY (`fk_restaurante`)
    REFERENCES `restaurante` (`id`),
    FOREIGN KEY (`fk_usuario`)
    REFERENCES `usuario` (`id`));