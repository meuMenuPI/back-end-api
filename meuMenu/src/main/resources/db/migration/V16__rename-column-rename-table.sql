alter table foto_restaurante rename restaurante_foto;
alter table restaurante_foto rename column fkRestaurante to fk_restaurante;
alter table restaurante_foto rename column nomeFoto to nome_foto;