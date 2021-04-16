/*
 Iván Ruiz Gázquez
 Jorge Ruiz Gómez
 Encode del Script UTF-8
 */

drop table precio_combustible cascade constraints;
drop table modelos cascade constraints;
drop table vehiculos cascade constraints;
drop table clientes cascade constraints;
drop table facturas cascade constraints;
drop table lineas_factura cascade constraints;
drop table reservas cascade constraints;

drop sequence seq_modelos;
drop sequence seq_num_fact;
drop sequence seq_reservas;

create table clientes
(
    NIF       varchar(9) primary key,
    nombre    varchar(20) not null,
    ape1      varchar(20) not null,
    ape2      varchar(20) not null,
    direccion varchar(40)
);

create table precio_combustible
(
    tipo_combustible varchar(10) primary key,
    precio_por_litro numeric(4, 2) not null
);

create sequence seq_modelos;

create table modelos
(
    id_modelo          integer primary key,
    nombre             varchar(30)   not null,
    precio_cada_dia    numeric(6, 2) not null check (precio_cada_dia >= 0),
    capacidad_deposito integer       not null check (capacidad_deposito > 0),
    tipo_combustible   varchar(10)   not null references precio_combustible
);


create table vehiculos
(
    matricula varchar(8) primary key,
    id_modelo integer not null references modelos,
    color     varchar(10)
);

create sequence seq_reservas;
create table reservas
(
    idReserva integer primary key,
    cliente   varchar(9) references clientes,
    matricula varchar(8) references vehiculos,
    fecha_ini date not null,
    fecha_fin date,
    check (fecha_fin >= fecha_ini)
);

create sequence seq_num_fact;
create table facturas
(
    nroFactura integer primary key,
    importe    numeric(8, 2),
    cliente    varchar(9) not null references clientes
);

create table lineas_factura
(
    nroFactura integer references facturas,
    concepto   char(40),
    importe    numeric(7, 2),
    primary key (nroFactura, concepto)
);

/*
PREGUNTAS DE LA PRÁCTICA:

   El resultado de la SELECT del paso anterior ¿sigue siendo fiable en este paso?:

    En este caso, el select sigue siendo fiable, ya que en el Requisito 2 se realiza
    un bloqueo de la Fila del vehículo a reservar mediante la matrícula y un FOR UPDATE OF,
    si otra transacción intenta reservar el mismo vehículo, va a intentar realizar el mismo
    bloqueo, por lo que se va a quedar en espera.

    Este bloqueo es liberado tras un Commit o Rollback.


   1. En este paso, la ejecución concurrente del mismo procedimiento ALQUILA con,
   quizás otros o los mimos argumentos, ¿podría habernos añadido una reserva no
   recogida en esa SELECT que fuese incompatible con nuestra reserva?, ¿por qué?.

    No, por la misma razón explicada en el punto anterior, el Requisito 2 Bloquea
    durante nustra transacción el Vehículo, por lo que si otra transacción quiere
    reservar dicho vehículo (independientemente de si es en nuestro mismo intervalo
    o no), debe esperar.

    Si se usa un vehículo diferente, dicha reserva no tendría esperas y sería compatible.

   2. En este paso otra transacción concurrente cualquiera ¿podría hacer INSERT o
   UPDATE sobre reservas y habernos añadido una reserva no recogida en esa SELECT
   que fuese incompatible con nuestra reserva?, ¿por qué?.
   Explica los por qués en un comentario sobre el script.

    Si, porque la transacción paralela puede no realizar las comprobaciones de los
    3 primeros requisitos, insertando un vehículo y una fecha que pueden ser icompatibles
    con nuestra transacción.

    Como no se realizan las comprobaciones, y el bloqueo SOLO ES DE ESCRITURA, puede
    insertarse cualquier dato compatible con la tabla.
    Si se realizase mediante el procedmiento "Alquilar", la transacción se quedaría
    en espera en el SELECT ... FOR UPDATE OF ..., ya que intentaría realizar un bloqueo
    sobre un bloqueo.


   5. Piensa por qué en este paso ninguna transacción concurrente podría habernos borrado
   el cliente o haber cambiado su NIF. Explica los por qués en un comentario sobre el
   script.
    Porque el planificador realiza un bloqueo de índice siempre que se trabaja con claves
    foráneas.

    En este caso, al actualizar a un padre durante un insert, se bloquean los índices de los hijos,
    por lo que si la T2 se queda esperando a comprobar si hay hijos para dicha FK hasta que T1 los añade
    con el COMMIT que cierra la transacción.


    En nuestras pruebas, nos ha lanzado el error después del bloqueo, ya que ya se había insertado el hijo.
   [23000][2292] ORA-02292: integrity constraint (HR.SYS_C0012542) violated - child record found Position: 0
*/

create or replace procedure alquilar(arg_NIF_cliente varchar,
                                     arg_matricula varchar,
                                     arg_fecha_ini date,
                                     arg_fecha_fin date) is
    -- Declaración de Excepciones
    NEGATIVE_DATES_DIFF exception; -- Req. 1
    pragma exception_init ( NEGATIVE_DATES_DIFF, -20003 );

    NONEXISTENT_VEHICLE exception; -- Req. 2
    pragma exception_init ( NONEXISTENT_VEHICLE, -20002 );

    VEHICLE_NOT_AVAILABLE exception; -- Req. 3
    pragma exception_init ( VEHICLE_NOT_AVAILABLE, -20004 );

    FK_VIOLATED exception; --ORA-02291: integrity constraint (la que sea) violated - parent key not found // Req. 5
    pragma exception_init (FK_VIOLATED, -2291);

    CLIENT_DOES_NOT_EXIST exception; -- Req. 5
    pragma exception_init ( CLIENT_DOES_NOT_EXIST, -20001 );

    -- Declaración de Records:
    type type_modelo_reserva is record
                                (
                                    modelo             modelos.id_modelo%type,
                                    precio_cada_dia    modelos.precio_cada_dia%type,
                                    capacidad_deposito modelos.capacidad_deposito%type,
                                    tipo_combustible   precio_combustible.tipo_combustible%type,
                                    precio_por_litro   precio_combustible.precio_por_litro%type
                                );


    -- Declaración de Variables:
    fecha_inicial            date; -- Req. 1
    fecha_final              date; -- Req. 1
    num_dias                 integer; -- Req. 1 y Req. 5
    vehiculo_a_reservar      type_modelo_reserva; -- Req. 2
    reserva_actual           reservas.idreserva%type; -- Req. 3; La variable almacena un fetch
    precio_total_combustible NUMBER; -- Req. 5.1
    precio_total_alquiler    NUMBER; -- Req. 5.2

    -- Declaración de Cursores
    -- Req. 2
    cursor obtener_modelo is
        select ID_MODELO, PRECIO_CADA_DIA, CAPACIDAD_DEPOSITO, TIPO_COMBUSTIBLE, PRECIO_POR_LITRO
        from MODELOS MOD
                 join PRECIO_COMBUSTIBLE using (TIPO_COMBUSTIBLE)
                 join VEHICULOS using (ID_MODELO)
        where MATRICULA = arg_matricula
        for update of VEHICULOS.matricula;

    -- Req. 3
    cursor obtener_reservas_intervalo is
        select IDRESERVA
        from RESERVAS R
        where MATRICULA = arg_matricula
        minus
        select IDRESERVA
        from RESERVAS R
        where (R.fecha_ini < fecha_inicial and R.FECHA_FIN < fecha_inicial
            or
               R.fecha_ini > fecha_final and R.FECHA_FIN > fecha_final);


begin

    -- Requisito 1: Comprobar Fechas
    -- Limpiamos las fechas con TRUNC
    fecha_inicial := trunc(arg_fecha_ini, 'DDD');
    fecha_final := trunc(arg_fecha_fin, 'DDD');

    num_dias := (fecha_final - fecha_inicial);

    if num_dias is null then
        num_dias := 4;
    elsif num_dias < 0 then
        raise NEGATIVE_DATES_DIFF;
    end if;


    -- Requisito 2: Modelo Vehículo existe
    open obtener_modelo;
    fetch obtener_modelo into vehiculo_a_reservar;

    if obtener_modelo%notfound then
        raise NONEXISTENT_VEHICLE;
    end if;
    close obtener_modelo;

    -- Requisito 3: Comprobar Reservas
    open obtener_reservas_intervalo; -- Realizamos la select
    fetch obtener_reservas_intervalo into reserva_actual;

    if obtener_reservas_intervalo%found then
        raise VEHICLE_NOT_AVAILABLE;
    end if;
    close obtener_reservas_intervalo;

    -- Requisito 4: Insertar en la BBDD
    insert into RESERVAS values (SEQ_RESERVAS.nextval, arg_NIF_cliente, arg_matricula, fecha_inicial, fecha_final);

    -- Requisito 5: Factura y Total
    precio_total_alquiler := num_dias * vehiculo_a_reservar.precio_cada_dia;
    precio_total_combustible := vehiculo_a_reservar.capacidad_deposito * vehiculo_a_reservar.precio_por_litro;

    insert into FACTURAS
    values (SEQ_NUM_FACT.nextval, precio_total_combustible + precio_total_alquiler, arg_NIF_cliente);

    -- Requisito 5.1 Primera línea de factura
    insert into LINEAS_FACTURA
    values (SEQ_NUM_FACT.currval,
            num_dias || ' dias de alquiler, vehiculo modelo ' || vehiculo_a_reservar.modelo,
            precio_total_alquiler);

    -- Requisito 5.2 Segunda línea de factura
    insert into LINEAS_FACTURA
    values (SEQ_NUM_FACT.currval,
            'Deposito lleno de ' || vehiculo_a_reservar.capacidad_deposito || ' litros de ' ||
            vehiculo_a_reservar.tipo_combustible,
            precio_total_combustible);

    -- Fin de la transacción
    -- Pausa el Procedure para comprobar los bloqueos:
    -- dbms_lock.sleep(30);
    commit;

exception
    when NEGATIVE_DATES_DIFF then
        rollback;
        raise_application_error(-20003, 'El numero de dias sera mayor que cero.');

    when NONEXISTENT_VEHICLE then
        rollback;
        raise_application_error(-20002, 'Vehiculo inexistente.');

    when VEHICLE_NOT_AVAILABLE then
        rollback;
        raise_application_error(-20004, 'El vehiculo no está disponible.');

    when FK_VIOLATED then
        rollback;
        raise_application_error(-20001, 'Cliente inexistente.');

    when OTHERS then
        rollback;
        raise;
end;
/


create or replace
    procedure reset_seq(p_seq_name varchar)
--From https://stackoverflow.com/questions/51470/how-do-i-reset-a-sequence-in-oracle
    is
    l_val number;
begin
    --Averiguo cual es el siguiente valor y lo guardo en l_val
    execute immediate
        'select ' || p_seq_name || '.nextval from dual' INTO l_val;

    --Utilizo ese valor en negativo para poner la secuencia cero, pimero cambiando el incremento de la secuencia
    execute immediate
            'alter sequence ' || p_seq_name || ' increment by -' || l_val ||
            ' minvalue 0';
    --segundo pidiendo el siguiente valor
    execute immediate
        'select ' || p_seq_name || '.nextval from dual' INTO l_val;

    --restauro el incremento de la secuencia a 1
    execute immediate
        'alter sequence ' || p_seq_name || ' increment by 1 minvalue 0';

end;
/

create or replace procedure inicializa_test is
begin
    reset_seq('seq_modelos');
    reset_seq('seq_num_fact');
    reset_seq('seq_reservas');


    delete from lineas_factura where NROFACTURA is not null;
    delete from facturas where NROFACTURA is not null;
    delete from reservas where IDRESERVA is not null;
    delete from vehiculos where MATRICULA is not null;
    delete from modelos where ID_MODELO is not null;
    delete from precio_combustible where TIPO_COMBUSTIBLE is not null;
    delete from clientes where NIF is not null;


    insert into clientes values ('12345678A', 'Pepe', 'Perez', 'Porras', 'C/Perezoso n1');
    insert into clientes values ('11111111B', 'Beatriz', 'Barbosa', 'Bernardez', 'C/Barriocanal n1');

    insert into precio_combustible values ('Gasolina', 1.5);
    insert into precio_combustible values ('Gasoil', 1.4);

    insert into modelos values (seq_modelos.nextval, 'Renault Clio Gasolina', 15, 50, 'Gasolina');
    insert into vehiculos values ('1234-ABC', seq_modelos.currval, 'VERDE');

    insert into modelos values (seq_modelos.nextval, 'Renault Clio Gasoil', 16, 50, 'Gasoil');
    insert into vehiculos values ('1111-ABC', seq_modelos.currval, 'VERDE');
    insert into vehiculos values ('2222-ABC', seq_modelos.currval, 'GRIS');

    commit;
end;
/



create or replace procedure test_alquila_coches is
begin

    --caso 1 nro dias negativo
    begin
        inicializa_test;
        alquilar('12345678A', '1234-ABC', current_date, current_date - 1);
        dbms_output.put_line('MAL: Caso nro dias negativo no levanta excepcion');
    exception
        when others then
            if sqlcode = -20003 then
                dbms_output.put_line('OK: Caso nro dias negativo correcto');
            else
                dbms_output.put_line('MAL: Caso nro dias negativo levanta excepcion ' || sqlcode || ' ' || sqlerrm);
            end if;
    end;

    --caso 2 vehiculo inexistente
    begin
        inicializa_test;
        alquilar('87654321Z', '9999-ZZZ', date '2013-3-20', date '2013-3-22');
        dbms_output.put_line('MAL: Caso vehiculo inexistente no levanta excepcion');
    exception
        when others then
            if sqlcode = -20002 then
                dbms_output.put_line('OK: Caso vehiculo inexistente correcto');
            else
                dbms_output.put_line('MAL: Caso vehiculo inexistente levanta excepcion ' || sqlcode || ' ' || sqlerrm);
            end if;
    end;

    --caso 3 cliente inexistente
    begin
        inicializa_test;
        alquilar('87654321Z', '1234-ABC', date '2013-3-20', date '2013-3-22');
        dbms_output.put_line('MAL: Caso cliente inexistente no levanta excepcion');
    exception
        when others then
            if sqlcode = -20001 then
                dbms_output.put_line('OK: Caso cliente inexistente correcto');
            else
                dbms_output.put_line('MAL: Caso cliente inexistente levanta excepcion ' || sqlcode || ' ' || sqlerrm);
            end if;
    end;

    --caso 4 Todo correcto pero NO especifico la fecha final
    declare

        resultadoPrevisto varchar(200) :=
                '1234-ABC11/03/1313512345678A4 dias de alquiler, vehiculo modelo 1   60#' ||
                '1234-ABC11/03/1313512345678ADeposito lleno de 50 litros de Gasolina 75';
        resultadoReal     varchar(200) := '';
        fila              varchar(200);
    begin
        inicializa_test;
        alquilar('12345678A', '1234-ABC', date '2013-3-11', null);

        SELECT listAgg(matricula || fecha_ini || fecha_fin || facturas.importe || cliente
                           || concepto || lineas_factura.importe, '#')
                       within group (order by nroFactura, concepto)
        into resultadoReal
        FROM facturas
                 join lineas_factura using (NroFactura)
                 join reservas using (cliente);

        dbms_output.put_line('Caso Todo correcto pero NO especifico la fecha final:');
        if resultadoReal = resultadoPrevisto then
            dbms_output.put_line('--OK SI Coinciden la reserva, la factura y las linea de factura');
        else
            dbms_output.put_line('--MAL NO Coinciden la reserva, la factura o las linea de factura');
            dbms_output.put_line('resultadoPrevisto=' || resultadoPrevisto);
            dbms_output.put_line('resultadoReal    =' || resultadoReal);
        end if;

    exception
        when others then
            dbms_output.put_line('--MAL: Caso Todo correcto pero NO especifico la fecha final devuelve ' || sqlerrm);
    end;

    --caso 5 Intentar alquilar un coche ya alquilado

    --5.1 la fecha ini del alquiler esta dentro de una reserva
    begin
        inicializa_test;
        --Reservo del 2013-3-10 al 12
        insert into reservas
        values (seq_reservas.NEXTVAL, '11111111B', '1234-ABC', date '2013-3-11' - 1, date '2013-3-11' + 1);
        --Fecha ini de la reserva el 11
        alquilar('12345678A', '1234-ABC', date '2013-3-11', date '2013-3-13');

        dbms_output.put_line('MAL: Caso vehiculo ocupado solape de fecha_ini no levanta excepcion');

    exception
        when others then
            if sqlcode = -20004 then
                dbms_output.put_line('OK: Caso vehiculo ocupado solape de fecha_ini correcto');
            else
                dbms_output.put_line(
                            'MAL: Caso vehiculo ocupado solape de fecha_ini levanta excepcion ' || sqlcode || ' ' ||
                            sqlerrm);
            end if;
    end;

    --5.2 la fecha fin del alquiler esta dentro de una reserva
    begin
        inicializa_test;
        --Reservo del 2013-3-10 al 12
        insert into reservas
        values (seq_reservas.NEXTVAL, '11111111B', '1234-ABC', date '2013-3-11' - 1, date '2013-3-11' + 1);
        --Fecha fin de la reserva el 11
        alquilar('12345678A', '1234-ABC', date '2013-3-7', date '2013-3-11');

        dbms_output.put_line('MAL: Caso vehiculo ocupado solape de fecha_fin no levanta excepcion');

    exception
        when others then
            if sqlcode = -20004 then
                dbms_output.put_line('OK: Caso vehiculo ocupado solape de fecha_fin correcto');
            else
                dbms_output.put_line(
                            'MAL: Caso vehiculo ocupado solape de fecha_fin levanta excepcion ' || sqlcode || ' ' ||
                            sqlerrm);
            end if;
    end;

    --5.3 la el intervalo del alquiler esta dentro de una reserva
    begin
        inicializa_test;
        --Reservo del 2013-3-9 al 13
        insert into reservas
        values (seq_reservas.NEXTVAL, '11111111B', '1234-ABC', date '2013-3-11' - 2, date '2013-3-11' + 2);
        -- reserva del 4 al 19
        alquilar('12345678A', '1234-ABC', date '2013-3-11' - 7, date '2013-3-12' + 7);

        dbms_output.put_line(
                'MAL: Caso vehiculo ocupado intervalo del alquiler esta dentro de una reserva no levanta excepcion');

    exception
        when others then
            if sqlcode = -20004 then
                dbms_output.put_line(
                        'OK: Caso vehiculo ocupado intervalo del alquiler esta dentro de una reserva correcto');
            else
                dbms_output.put_line(
                            'MAL: Caso vehiculo ocupado intervalo del alquiler esta dentro de una reserva levanta excepcion '
                            || sqlcode || ' ' || sqlerrm);
            end if;
    end;

    --caso 6 Todo correcto pero SI especifico la fecha final
    declare

        resultadoPrevisto varchar(400) :=
                '12222-ABC11/03/1313/03/1310212345678A2 dias de alquiler, vehiculo modelo 2   32#' ||
                '12222-ABC11/03/1313/03/1310212345678ADeposito lleno de 50 litros de Gasoil   70';
        resultadoReal     varchar(400) := '';
        fila              varchar(200);
    begin
        inicializa_test;
        alquilar('12345678A', '2222-ABC', date '2013-3-11', date '2013-3-13');

        SELECT listAgg(nroFactura || matricula || fecha_ini || fecha_fin || facturas.importe || cliente
                           || concepto || lineas_factura.importe, '#')
                       within group (order by nroFactura, concepto)
        into resultadoReal
        FROM facturas
                 join lineas_factura using (NroFactura)
                 join reservas using (cliente);


        dbms_output.put_line('Caso Todo correcto pero SI especifico la fecha final');

        if resultadoReal = resultadoPrevisto then
            dbms_output.put_line('--OK SI Coinciden la reserva, la factura y las linea de factura');
        else
            dbms_output.put_line('--MAL NO Coinciden la reserva, la factura o las linea de factura');
            dbms_output.put_line('resultadoPrevisto=' || resultadoPrevisto);
            dbms_output.put_line('resultadoReal    =' || resultadoReal);
        end if;

    exception
        when others then
            dbms_output.put_line('--MAL: Caso Todo correcto pero SI especifico la fecha final devuelve ' || sqlerrm);
    end;

end;
/

set serveroutput on

exec test_alquila_coches;
