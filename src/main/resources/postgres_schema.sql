CREATE TABLE client (
  client_id BIGINT PRIMARY KEY,
  client_name VARCHAR(64) NOT NULL,
  client_surname VARCHAR(64) NOT NULL,
  client_code VARCHAR(32) NOT NULL,
  created_on TIMESTAMP DEFAULT NOW()
);

CREATE TABLE account (
  account_number BIGINT PRIMARY KEY,
  client_id BIGINT NOT NULL REFERENCES client(client_id),
  balance DECIMAL(10,4) NOT NULL DEFAULT 0,
  created_on TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transactions (
  debit_from BIGINT NOT NULL REFERENCES account(account_number),
  credit_to BIGINT NOT NULL REFERENCES account(account_number),
  amount DECIMAL(10, 4) NOT NULL,
  operation_date TIMESTAMP NOT NULL,
  created_on TIMESTAMP DEFAULT NOW()
);

CREATE SEQUENCE account_number_seq;
CREATE SEQUENCE client_id_seq;