/*
    El objetivo de esta función es el de separar los procedimientos de los tests
    y tablas.

    Cuando lo tengamos hecho lo copiamos y pegamos directamente sobre el script principal,
    pero así es más fácil navegar por el código.
*/


create or replace procedure alquilar(arg_NIF_cliente varchar,
  arg_matricula varchar, arg_fecha_ini date, arg_fecha_fin date) is
begin
null;
end;
/


-- Ejecución de los tests
set serveroutput on
exec test_alquila_coches;
