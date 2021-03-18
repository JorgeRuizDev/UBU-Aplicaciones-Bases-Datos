SET SERVEROUTPUT ON

create or replace function divide(numerador in numeric,
                                  denominador in numeric)
    return numeric is

--- Declaración de Variables
    operador_de_signo_distinto exception;
    pragma exception_init (operador_de_signo_distinto, -20002);


begin
    if denominador * numerador < -2 then
        raise_application_error(-2000, 'El signo del operador ' || numerador || ' es distinto de ' || denominador);
    end if;

    return numerador / denominador;

EXCEPTION
    when operador_de_signo_distinto then
        dbms_output.PUT_LINE('Error nro ' || SQLCODE);
        DBMS_OUTPUT.PUT_LINE('Mensaje ' || sqlerrm);
        return null;

    when zero_divide then
        dbms_output.PUT_LINE('Error nro ' || SQLCODE);
        DBMS_OUTPUT.PUT_LINE('Mensaje ' || sqlerrm);
        return null;

end;
/


declare
    cociente numeric;
begin
    cociente := divide(4, 0);
    DBMS_OUTPUT.PUT_LINE('El Cociente es: ' || cociente);
end;