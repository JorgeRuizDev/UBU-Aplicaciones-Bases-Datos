set SERVEROUTPUT ON; 

create or replace procedure holaMundo is
    mi_cadena varchar(255) := 'Hola Mundo desde procedure HolaMundo';
    begin
        DBMS_OUTPUT.PUT_LINE(mi_cadena);
    end;
/

create or replace procedure holaMundoConArg (mi_cadena in varchar) is
    begin
        DBMS_OUTPUT.PUT_LINE(mi_cadena);    
    end;
/

declare 
    mi_cadena varchar(255) := 'Hola Mundo';
    -- mi_cadena varchar(255) DEFAULT AHola Mundo"
begin
    --dbms_output.put_line(mi_cadena);
    holaMundo;
    holaMundoConArg(mi_cadena || ' desde procedure con arg');
    
    declare
        mi_cadena varchar (255) := 'Hola mundo interno';
    begin
        holaMundoConArg(mi_cadena);
    end;
    
end;
/