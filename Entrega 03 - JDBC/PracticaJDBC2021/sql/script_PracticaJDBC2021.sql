drop table clientes cascade constraints;
drop table cuentas cascade constraints;
drop table cuentas_clientes cascade constraints;

drop sequence seq_idCliente;
create sequence seq_idCliente;
create table clientes(
  idCliente  integer primary key,
  dni        numeric(8) not null unique
);

drop sequence seq_idCta;
create sequence seq_idCta;
create table cuentas(
  idCta          integer primary key,
  idOficina      integer,
  fechaCreacion  date default current_date,
  saldo          numeric(8,2) default 0
);

create table cuentas_clientes(
   idCta       integer references cuentas,
   idCli       integer references clientes,   
   primary key( idCta, idCli)
);

insert into clientes (idCliente, dni) values(seq_idCliente.nextval, 12345678);
insert into clientes (idCliente, dni) values(seq_idCliente.nextval, 11111111);

insert into cuentas (idCta, idOficina) values(seq_idCta.nextval, 1);

exit;