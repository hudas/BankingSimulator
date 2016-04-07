CREATE DATABASE simulation_data;

CREATE TABLE predefined_client (
  client_id BIGINT PRIMARY KEY,
  client_name VARCHAR(64) NOT NULL,
  client_surname VARCHAR(64) NOT NULL,
  client_code VARCHAR(32) NOT NULL,
);

CREATE TABLE predefined_account (
  account_id BIGINT PRIMARY KEY,
  account_number VARCHAR(32),
  client_id BIGINT NOT NULL REFERENCES client(client_id),
  balance DECIMAL(10,4) NOT NULL DEFAULT 0,
);

CREATE TABLE predefined_task (
  task_id BIGINT,
  type VARCHAR(32),
  debit_from VARCHAR(32),
  credit_to VARCHAR(32),
  amount DECIMAL(10, 4)
);

CREATE SEQUENCE account_id_seq;
CREATE SEQUENCE client_id_seq;
CREATE SEQUENCE task_id_seq;