drop table reservas;
drop table pistas;
drop sequence seq_pistas;



create table pistas
(
    nro integer primary key
);

create table reservas
(
    pista integer references pistas (nro),
    fecha date,
    hora  integer check (hora >= 0 and hora <= 23),
    socio varchar(20),
    primary key (pista, fecha, hora)
);

create sequence seq_pistas;

insert into pistas
values (seq_pistas.nextval);
insert into reservas
values (seq_pistas.currval, '20/03/2018', 14, 'Pepito');
insert into pistas
values (seq_pistas.nextval);
insert into reservas
values (seq_pistas.currval, '24/03/2018', 18, 'Pepito');
insert into reservas
values (seq_pistas.currval, '21/03/2018', 14, 'Juan');
insert into pistas
values (seq_pistas.nextval);
insert into reservas
values (seq_pistas.currval, '22/03/2018', 13, 'Lola');
insert into reservas
values (seq_pistas.currval, '22/03/2018', 12, 'Pepito');

commit;

create or replace function anularReserva(
    p_socio varchar,
    p_fecha date,
    p_hora integer,
    p_pista integer)
    return integer is
begin
    DELETE
    FROM reservas
    WHERE fecha = p_fecha
      and pista = p_pista
      AND hora = p_hora
      AND socio = p_socio;

    if sql%rowcount = 1 then
        commit;
        return 1;
    else
        rollback;
        return 0;
    end if;

end;
/
create or replace FUNCTION reservarPista(
    p_socio VARCHAR,
    p_fecha DATE,
    p_hora INTEGER
)
    RETURN INTEGER IS

    CURSOR vPistasLibres IS
        select NRO from PISTAS
        where NRO not in (
            select distinct  NRO
            from RESERVAS
            right join PISTAS P on RESERVAS.PISTA = P.NRO
            where fecha = TO_CHAR(p_fecha, 'DD-MM-YYYY')
            and hora = p_hora
        );
    vPista INTEGER;

BEGIN
    open vPistasLibres;
    fetch vPistasLibres into vPista;

    if vPistasLibres%ROWCOUNT = 0
    then
        close vPistasLibres;
        rollback;
        DBMS_OUTPUT.PUT_LINE('NO HAY PISTAS LIBRES');
        return 0;
    end if;

    DBMS_OUTPUT.PUT_LINE('Asignando la pista ' || vPista);
    insert into reservas (pista, fecha, hora, socio) values (vPista, TO_CHAR(p_fecha, 'DD-MM-YYYY'), p_hora, p_socio);
    COMMIT;
    RETURN 1;
END;
/


set SERVEROUTPUT ON
declare
    resultado integer;
begin
    resultado := anularReserva('Pepito', '20/03/18', 14, 1);
    DBMS_OUTPUT.put_line(resultado);
end;
/

-- Declaración de Funcioón hasta el IS
create or replace function numeroPistas(p_socio varchar)
    return integer is -- Fin declaración
-- DECLARACIÓN DE VARIABLES:
-- Un cursor almacena el resultado de una consulta (select):
    cursor vPistas is
        select count(*)
        from reservas
        where socio = p_socio;
    vNPistas integer;

begin
    open vPistas;
    fetch vPistas into vNPistas;

    if vPistas%NOTFOUND
    then
        close vPistas;
        rollback;
        return 0;
    end if;

    close vPistas;
    commit;
    return vNPistas;
end;
/


set SERVEROUTPUT ON
declare
    pistas  integer;
    usuario varchar(50);
begin
    -- resultado := anularReserva('Pepito', '20/03/2018', 14, 1);
    -- DBMS_OUTPUT.put_line(resultado);

    usuario := 'Pepito';

    pistas := numeroPistas(usuario);
    DBMS_OUTPUT.put_line('Pistas para ' || usuario || ': ' || pistas);

end;
/

declare
    valor integer;
begin
    -- En una llamada a función es obligatorio el uso de := para almacenar el return;
    valor := reservarPista('Socio 1', CURRENT_DATE, 12);
    valor := reservarPista('Socio 2', CURRENT_DATE, 12);
    valor := reservarPista('Socio 3', CURRENT_DATE, 12);
    valor := reservarPista('Socio 4', CURRENT_DATE, 12);

end;
/

