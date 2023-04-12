create table favorito(
    fk_usuario int,
    FOREIGN KEY (fk_usuario)
        REFERENCES usuario (id),
    fk_restaurante int,
    FOREIGN KEY (fk_restaurante)
        REFERENCES restaurante (id)
    );
