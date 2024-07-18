CREATE SEQUENCE seq_fact_id;
CREATE SEQUENCE seq_fact_numerofactura;
CREATE SEQUENCE seq_item_id;

CREATE DATABASE facturacion;

CREATE TABLE fact (
    fact_id NUMERIC(20,0) DEFAULT nextval('seq_fact_id'),
    fact_numerofactura INT DEFAULT nextval('seq_fact_numerofactura'),
    fact_nombrecliente VARCHAR(100) NOT NULL,
    fact_fecha DATE NOT NULL,
    fact_subtotal NUMERIC(10, 2) NOT NULL,
    fact_iva NUMERIC(10, 2) NOT NULL,
    fact_total NUMERIC(10, 2) NOT NULL
);

ALTER TABLE fact
ADD CONSTRAINT facturas_pk PRIMARY KEY (fact_id);

CREATE TABLE items (
    item_id NUMERIC(20,0) DEFAULT nextval('seq_item_id'),
    item_articulo VARCHAR(255) NOT NULL,
    item_cantidad INT NOT NULL,
    item_valor NUMERIC(10, 2) NOT NULL,
    item_total NUMERIC(10, 2) NOT NULL,
    item_facturaid NUMERIC(20,0) 
);

ALTER TABLE items
ADD CONSTRAINT items_pk PRIMARY KEY (item_id);

ALTER TABLE items
ADD CONSTRAINT item_fact_fk FOREIGN KEY (item_facturaid) REFERENCES fact (fact_id);