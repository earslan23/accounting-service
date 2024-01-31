CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 100;

CREATE TABLE products (
                          id BIGINT NOT NULL,
                          date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          description VARCHAR(255),
                          price NUMERIC(10,2) NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          CONSTRAINT "pk_products" PRIMARY KEY (id)
);

CREATE SEQUENCE seq_user START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
                          id BIGINT NOT NULL,
                          date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          first_name VARCHAR(100),
                          last_name VARCHAR(100),
                          email VARCHAR(100) NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          role_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          CONSTRAINT "pk_users" PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_status_null_idx ON users (email) WHERE status IS NULL;

CREATE SEQUENCE seq_roles START WITH 1 INCREMENT BY 1;

CREATE TABLE roles (
                          id BIGINT NOT NULL,
                          date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          name VARCHAR(30) NOT NULL,
                          CONSTRAINT "pk_roles" PRIMARY KEY (id)
);


ALTER TABLE users ADD CONSTRAINT FK_USERS_ON_ROLE_ID FOREIGN KEY (role_id) REFERENCES roles (id);


CREATE SEQUENCE seq_invoices START WITH 1 INCREMENT BY 100;

CREATE TABLE invoices (
                          id BIGINT NOT NULL,
                          date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          product_id BIGINT NOT NULL,
                          bill_no BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          CONSTRAINT "pk_invoices" PRIMARY KEY (id)
);

ALTER TABLE invoices ADD CONSTRAINT FK_INVOICES_ON_PRODUCT_ID FOREIGN KEY (product_id) REFERENCES products (id);
ALTER TABLE invoices ADD CONSTRAINT FK_INVOICES_ON_USER_ID FOREIGN KEY (user_id) REFERENCES users (id);


CREATE SEQUENCE seq_invoice_alerts START WITH 1 INCREMENT BY 100;

CREATE TABLE invoice_alerts (
                       id BIGINT NOT NULL,
                       date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                       last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                       invoice_id BIGINT NOT NULL,
                       status VARCHAR(30) NOT NULL,
                       CONSTRAINT "pk_invoice_alerts" PRIMARY KEY (id)
);

ALTER TABLE invoice_alerts ADD CONSTRAINT FK_INVOICE_ALERTS_ON_INVOICE_ID FOREIGN KEY (invoice_id) REFERENCES invoices (id);
