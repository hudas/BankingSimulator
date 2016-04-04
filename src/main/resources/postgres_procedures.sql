CREATE OR REPLACE FUNCTION create_client (
  in_client_name VARCHAR(32),
  in_client_surname VARCHAR(32),
  in_client_code VARCHAR(32))
  RETURNS VOID
AS $$
BEGIN
  INSERT INTO client (client_id, client_name, client_surname, client_code)
    VALUES(nextval('client_id_seq'), in_client_name, in_client_surname, in_client_code);
END; $$ language plpgsql;


CREATE OR REPLACE FUNCTION create_account (
  in_client_code VARCHAR(32))
  RETURNS BOOLEAN
AS $$
DECLARE
  var_client_id BIGINT := NULL;
BEGIN
  SELECT client_id INTO var_client_id FROM client WHERE client_code = in_client_code;
  IF var_client_id IS NULL THEN
    RETURN FALSE;
  END IF;

  INSERT INTO account (account_number, client_id, balance) VALUES (nextval('account_number_seq'), var_client_id, '0');
  RETURN TRUE ;
END; $$ language plpgsql;


CREATE OR REPLACE FUNCTION add_funds (
  in_account_number VARCHAR(32),
  in_amount DECIMAL)
  RETURNS VOID
AS $$
DECLARE
  var_account_id BIGINT;
BEGIN
  SELECT account_id INTO var_account_id
    FROM account
  WHERE account_number = in_account_number LIMIT 1;
  INSERT INTO transactions(debit_from, credit_to, debit_account_number, credit_account_number, amount, operation_date)
  VALUES (0, var_account_id, '0', in_account_number, in_amount, NOW());
  UPDATE account SET balance = balance + in_amount WHERE account_id = var_account_id;
END; $$ language plpgsql;



CREATE OR REPLACE FUNCTION withdraw_funds (
  in_account_number VARCHAR(32),
  in_amount DECIMAL)
  RETURNS VOID
AS $$
DECLARE
  var_account_id BIGINT;
BEGIN
  SELECT account_id INTO var_account_id
  FROM account
  WHERE account_number = in_account_number LIMIT 1;
  INSERT INTO transactions(debit_from, credit_to, debit_account_number, credit_account_number, amount, operation_date)
  VALUES (var_account_id, 0, in_account_number, '0', in_amount, NOW());
  UPDATE account SET balance = balance - in_amount WHERE account_id = var_account_id;
END; $$ language plpgsql;



CREATE OR REPLACE FUNCTION transfer_funds (
  in_debet_account_number VARCHAR(32),
  in_credit_account_number VARCHAR(32),
  in_amount DECIMAL)
  RETURNS BOOLEAN
AS $$
DECLARE
  var_debet_account_id BIGINT;
  var_credit_account_id BIGINT;
  var_balance DECIMAL;
BEGIN
  SELECT account_id, balance INTO var_debet_account_id, var_balance
    FROM account
      WHERE account_number = in_debet_account_number LIMIT 1;
  IF var_balance < in_amount THEN
    RETURN FALSE ;
  END IF;

  SELECT account_id INTO var_credit_account_id
    FROM account
      WHERE account_number = in_credit_account_number LIMIT 1;

  INSERT INTO transactions(debit_from, credit_to, debit_account_number, credit_account_number, amount, operation_date)
  VALUES (var_debet_account_id, var_credit_account_id, in_debet_account_number, in_credit_account_number, in_amount, NOW());

  UPDATE account SET balance = balance - in_amount WHERE account_id = var_debet_account_id;
  UPDATE account SET balance = balance + in_amount WHERE account_id = var_credit_account_id;
  RETURN TRUE;
END; $$ language plpgsql;
