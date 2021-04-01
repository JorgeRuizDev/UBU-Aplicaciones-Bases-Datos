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
        where MATRICULA = arg_matricula;

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
        num_dias:= 4;
    end if;

    if num_dias < 0 then
        raise NEGATIVE_DATES_DIFF;
    end if;


    -- Requisito 2: Modelo Vehículo existe
    open obtener_modelo;
    fetch obtener_modelo into vehiculo_a_reservar;

    if obtener_modelo%notfound then
        raise NONEXISTENT_VEHICLE;
    end if;
    close obtener_modelo;
    -- TODO: Lo de bloquear la tabla

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


/*
    Preguntas y Respuestas!
*/
SET SERVEROUT ON
-- Ejecución de los tests
begin
    TEST_ALQUILA_COCHES();
end;


