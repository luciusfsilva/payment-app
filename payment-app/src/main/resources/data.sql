DELETE FROM TRANSACTIONS;

DELETE FROM WALLETS;

INSERT INTO WALLETS (ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE)
VALUES (1, 'Joao-user', 12345678900, 'joao@test.com', 123456, 1, 1000.00)

INSERT INTO WALLETS (ID, FULL_NAME, CPF, EMAIL, "PASSWORD", "TYPE", BALANCE)
VALUES (1, 'Maria-lojista', 12345678901, 'maria@test.com', 123456, 2, 1000.00)