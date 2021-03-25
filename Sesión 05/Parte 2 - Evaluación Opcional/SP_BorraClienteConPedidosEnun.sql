set SERVEROUT ON

-- Jorge Ruiz Gómez & Iván Ruiz Gázquez

DROP TABLE clientes CASCADE CONSTRAINTS;
DROP TABLE pedidos CASCADE CONSTRAINTS;

DROP SEQUENCE sec_clientes;
DROP SEQUENCE sec_pedidos;

CREATE SEQUENCE sec_clientes;
CREATE SEQUENCE sec_pedidos;

CREATE TABLE clientes
(
    idCliente INTEGER PRIMARY KEY,
    nombre    VARCHAR(20) NOT NULL
);

CREATE TABLE pedidos
(
    idPedido  INTEGER PRIMARY KEY,
    idCliente INTEGER REFERENCES clientes NOT NULL,
    articulo  CHAR(10),
    cantidad  INTEGER,
    precio    NUMERIC(5, 2)
);


commit;
--exit;


create or replace procedure borrarCliente(arg_nroDelCliente clientes.idCliente%type) is

    -- Declaración de Excepciones:
    NO_CLIENT exception;
    pragma exception_init ( NO_CLIENT, -20001 );

    CLIENT_HAS_ORDERS exception;
    pragma exception_init ( CLIENT_HAS_ORDERS, -20002 );

    HAS_CHILDREN exception;
    pragma exception_init ( HAS_CHILDREN, -2292 );
    -- Declaración de Cursores:

begin

    delete
    from clientes
    where idCliente = arg_nroDelCliente;

    if sql%rowcount != 1 then
        raise NO_CLIENT;
    end if;

    commit;

exception
    when NO_CLIENT then
        rollback;
        raise_application_error(-20001, 'El cliente ' || arg_nroDelCliente || ' no existe');

    when HAS_CHILDREN then
        rollback;
        raise_application_error(-20002, 'El cliente ' || arg_nroDelCliente || ' aun tiene pedidos');

    when OTHERS then
        rollback;
        raise;
end;
/

create or replace procedure test_borra_cliente is


    cursor vTest3 is select nombre
                     from clientes;
    test3_nombre      clientes.nombre%type;
    test3_rowcount    integer;
    test4_listagg_out varchar(20);

begin
    delete from clientes where idCliente is not null;
    delete from pedidos where idPedido is not null;


    INSERT INTO clientes
    VALUES (sec_clientes.nextval, 'PEPE');
--PEPE compra 5 unidades del articulo 1
    INSERT INTO pedidos
    VALUES (sec_pedidos.nextval, sec_clientes.currval, 'ARTICULO 1', 5, 10);
--PEPE compra 5 unidades del articulo 2
    INSERT INTO pedidos
    VALUES (sec_pedidos.nextval, sec_clientes.currval, 'ARTICULO 2', 5, 10);


    INSERT INTO clientes
    VALUES (sec_clientes.nextval, 'ANA');

    commit;


    -- Caso 1: Borrado de un cliente inexistente:
    begin
        borrarCliente(3);
        DBMS_OUTPUT.PUT_LINE('MAL c1: No detecta un borrado de un cliente inexistente');
    exception
        when others then
            if sqlcode = -20001 then
                DBMS_OUTPUT.PUT_LINE('BIEN c1: ' || sqlerrm);
            else
                DBMS_OUTPUT.PUT_LINE('MAL c1: la excepción lanzada es incorrecta ' || sqlerrm);
            end if;
    end;

    -- Caso 2: Borrado de cliente con pedidos:
    begin
        borrarCliente(1);
        DBMS_OUTPUT.PUT_LINE('MAL c2: No detecta un borrado de un cliente con pedidos');
    exception
        when others then
            if sqlcode = -20002 then
                DBMS_OUTPUT.PUT_LINE('BIEN c2: ' || sqlerrm);
            else
                DBMS_OUTPUT.PUT_LINE('MAL c2: la excepción lanzada es incorrecta ' || sqlerrm);
            end if;
    end;


    -- Caso 3:

    begin
        borrarCliente(2);

        open vTest3;
        fetch vTest3 into test3_nombre;
        test3_rowcount := vTest3%rowcount;
        close vTest3;


        if upper(test3_nombre) = 'PEPE' and test3_rowcount = 1 then
            DBMS_OUTPUT.PUT_LINE('BIEN c3: Borrado Correcto');
        else
            DBMS_OUTPUT.PUT_LINE('MAL c3: Borrado incorrecto');
        end if;

    exception
        when OTHERS then
            DBMS_OUTPUT.PUT_LINE('MAL c3: Ha habido una excepción indeseada - ' || sqlerrm);
    end;

    -- Caso 4: Caso 3 pero con Listagg
    begin
        INSERT INTO clientes values (2, 'ANA');
        borrarCliente(2);

        select listagg(idCliente || nombre, '#')
                       within group (order by idCliente)
        into test4_listagg_out
        from clientes;

        if test4_listagg_out = '1PEPE' then
            DBMS_OUTPUT.PUT_LINE('BIEN c4: Borrado Correcto ' || test4_listagg_out);
        else
            DBMS_OUTPUT.PUT_LINE('MAL c4: Borrado incorrecto');
            DBMS_OUTPUT.PUT_LINE('Esperado: 1PEPE - Obtenido ' || test4_listagg_out);
        end if;

    exception
        when OTHERS then
            DBMS_OUTPUT.PUT_LINE('MAL c4: Ha habido una excepción indeseada - ' || sqlerrm);
    end;


end;
/
begin
    test_borra_cliente;
end;
/
-- Jorge Ruiz Gómez & Iván Ruiz Gázquez
