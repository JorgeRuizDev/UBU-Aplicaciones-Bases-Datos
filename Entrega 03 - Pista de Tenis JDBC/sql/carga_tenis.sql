drop table reservas;
drop table pistas;
drop sequence sec_num_pista;

create table pistas(
	nro integer primary key
);

create table reservas(
	fecha date,
	hora integer check (hora between 0 and 23 ),
	pista integer references pistas,
	socio varchar(30) not null,
	primary key ( fecha, hora, pista)
);

create sequence sec_num_pista;
/*
--Pistas de la 1 la 5
insert into pistas values (sec_num_pista.nextval); 
insert into pistas values (sec_num_pista.nextval);
insert into pistas values (sec_num_pista.nextval);
insert into pistas values (sec_num_pista.nextval);
insert into pistas values (sec_num_pista.nextval);

insert into reservas values ( current_date, 15, 1, 'Pepe'); --Pista 1 a las 3
insert into reservas values ( current_date, 15, 2, 'Pepe'); --Pista 2 a las 3

select * from pistas;
*/
commit;
exit; 

/*No quites el exit si vas a ejecutar el script con

  lsi.ubu.util.ExecuteScript.java
  
  Si lo quitas java se queda colgado esperando
  que acabe el proceso de ejecucion del script
*/


/*
Estas selects no se ejecutan porque estan detras del exit,
pero te pueden servir para depurar
*/
select * from pistas;
select * from reservas;
