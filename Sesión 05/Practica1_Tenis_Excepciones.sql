SET SERVEROUTPUT ON

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

create or replace function anularReserva(p_socio varchar,
                                         p_fecha date,
                                         p_hora integer,
                                         p_pista integer)
    return integer is

    no_reserva exception;
    pragma exception_init (no_reserva, -20001);

begin
    DELETE
    FROM reservas
    WHERE trunc(fecha) = trunc(p_fecha)
      AND pista = p_pista
      AND hora = p_hora
      AND socio = p_socio;

    if sql%rowcount != 1 then
        RAISE_APPLICATION_ERROR(-20001, 'La reserva no existe.');
    end if;

    commit;
    return 1;

-- Tratamiento de excepciones:
exception
    when no_reserva then
        rollback;
        -- Imprimimos el error generado por la excepción.
        DBMS_OUTPUT.put_line(sqlerrm);
        return 0;


end;
/

create or replace FUNCTION reservarPista(p_socio VARCHAR,
                                         p_fecha DATE,
                                         p_hora INTEGER)
    RETURN INTEGER IS

    CURSOR vPistasLibres IS
        SELECT nro
        FROM pistas
        WHERE nro NOT IN (
            SELECT pista
            FROM reservas
            WHERE trunc(fecha) = trunc(p_fecha)
              AND hora = p_hora)
        order by nro;
    vPista INTEGER;

BEGIN
    OPEN vPistasLibres;
    FETCH vPistasLibres INTO vPista;

    IF vPistasLibres%NOTFOUND
    THEN
        CLOSE vPistasLibres;
        RETURN 0;
    END IF;

    INSERT INTO reservas VALUES (vPista, p_fecha, p_hora, p_socio);
    CLOSE vPistasLibres;
    COMMIT;
    RETURN 1;
END;
/

-- Probar Reservas:
declare
    resultado integer;
begin
    -- Resrva OK
    resultado := anularreserva('Juan', '21/03/2018', 14, 2);
    if resultado = 1 then
        dbms_output.put_line('Reserva 1 anulada: OK');
    else
        dbms_output.put_line('Reserva 1 anulada: MAL');
    end if;


    -- Reserva MAL
    resultado := anularreserva('Juan', '21/03/2038', 14, 2);
    if resultado = 1 then
        dbms_output.put_line('Reserva anulada: OK');
    else
        dbms_output.put_line('Reserva anulada: MAL');
    end if;
end;



create or replace procedure listaReservas is
    --Declaración de variables:

    -- Un "record" es similar a un struct de C
    type tReserva is record (pista reservas.pista%type,
        fecha reservas.fecha%type,
        -- Declaración estándar de datos:
        hora integer,
        socio varchar(20));

    -- Inicializamos un 'struct', solo almacena una ROW
    miReserva tReserva;

    cursor cReserva is
        select pista, fecha, hora, socio
        from reservas;

begin

    -- Realizamos la consulta y la almacenamos
    open cReserva;
    loop
        fetch cReserva into miReserva;
        exit when cReserva%notfound; --Salida del bucle

        DBMS_OUTPUT.PUT_LINE(miReserva.pista || ' ' || miReserva.fecha || miReserva.hora || ' ' || miReserva.socio);
    end loop;
    close cReserva;
    commit;

end;


declare
begin
    listaReservas;
end;
/*
SET SERVEROUTPUT ON
declare
 resultado integer;
begin

     resultado := reservarPista( 'Socio 1', CURRENT_DATE, 12 );
     if resultado=1 then
        dbms_output.put_line('Reserva 1: OK');
     else
        dbms_output.put_line('Reserva 1: MAL');
     end if;

     --Continua tu solo....


    resultado := anularreserva( 'Socio 1', CURRENT_DATE, 12, 1);
     if resultado=1 then
        dbms_output.put_line('Reserva 1 anulada: OK');
     else
        dbms_output.put_line('Reserva 1 anulada: MAL');
     end if;

     resultado := anularreserva( 'Socio 1', date '1920-1-1', 12, 1);
     --Continua tu solo....

end;
/
*/

