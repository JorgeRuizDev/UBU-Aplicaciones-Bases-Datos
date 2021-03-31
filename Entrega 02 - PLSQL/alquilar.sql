create or replace procedure alquilar(arg_NIF_cliente varchar,
                                     arg_matricula varchar,
                                     arg_fecha_ini date,
                                     arg_fecha_fin date) is
    -- Declaración de Excepciones
    NEGATIVE_DATES_DIFF exception; -- Req. 1
    pragma exception_init ( NEGATIVE_DATES_DIFF, -20003 );

    NONEXISTENT_VEHICLE exception; -- Req. 2
    pragma exception_init ( NONEXISTENT_VEHICLE, -20002 );


    -- Declaración de Estructuras:
    modelo_fila     MODELOS%rowtype;
    -- Declaración de Variables:
    fecha_ini       date;
    fecha_fin       date;
    modelo_vehiculo number;


    -- Declaración de Cursores
    cursor obtener_modelo is
        select NOMBRE, PRECIO_CADA_DIA, CAPACIDAD_DEPOSITO, PC.TIPO_COMBUSTIBLE, PRECIO_POR_LITRO
        from MODELOS MOD
                 join PRECIO_COMBUSTIBLE PC on MOD.TIPO_COMBUSTIBLE = PC.TIPO_COMBUSTIBLE
                 join VEHICULOS on VEHICULOS.ID_MODELO = MOD.ID_MODELO
        where MATRICULA = arg_matricula;


begin


    -- Requisito 1: Comprobar Fechas:
    -- Limpiamos las fechas con TRUNC
    fecha_ini := trunc(arg_fecha_ini, 'DDD');
    fecha_fin := trunc(arg_fecha_fin, 'DDD');

    if fecha_ini > fecha_fin then
        raise NEGATIVE_DATES_DIFF;
    end if;


    -- Requisito 2: Modelo Vehículo:
    open obtener_modelo;
    fetch obtener_modelo into modelo_fila;

    if obtener_modelo%notfound then
        raise NONEXISTENT_VEHICLE;
    end if;
    close obtener_modelo;

exception
    when NEGATIVE_DATES_DIFF then
        rollback;
        raise_application_error(-20003, 'El numero de dias sera mayor que cero.');

    when NONEXISTENT_VEHICLE then
        rollback;
        raise_application_error(-20002, 'Vehiculo inexistente.');


    when OTHERS then
        -- TODO: Tratamiento genérico:
        rollback;
        raise;
end;
/

-- Ejecución de los tests
begin
    TEST_ALQUILA_COCHES();
end;