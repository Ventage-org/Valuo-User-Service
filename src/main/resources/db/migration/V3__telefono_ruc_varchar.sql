-- Hibernate valida VARCHAR; CHAR(n) (bpchar) no coincide con el mapeo de String.
ALTER TABLE usuario ALTER COLUMN telefono TYPE VARCHAR(9);
ALTER TABLE usuario ALTER COLUMN ruc TYPE VARCHAR(11);
