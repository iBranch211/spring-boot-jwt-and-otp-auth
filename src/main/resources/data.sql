DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS employees;

CREATE TABLE users (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  enabled BOOLEAN
);

INSERT INTO users (username, password, enabled) VALUES
  ('admin', '$2a$10$e6H1Jgrft/scpmpzbMFO0uqF1gxqop73l5wOlwF30Aem6Tty1nI2G', true),
  ('azhwani', '$2a$10$kWWOnNOiToOxcIQ7UJ.cB.XFAflYvMS5BPASR1eqqojc6H9ELWUfC', true),
  ('guest', '$2a$10$kWWOnNOiToOxcIQ7UJ.cB.XFAflYvMS5BPASR1eqqojc6H9ELWUfC', true),
  ('yojita@localhost.com', '', true);

CREATE TABLE roles (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

INSERT INTO roles (name) VALUES
  ('USER'),
  ('ADMIN'),
  ('MANAGER');


CREATE TABLE users_roles (
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  FOREIGN KEY(user_id) REFERENCES users(id),
  FOREIGN KEY(role_id) REFERENCES roles(id),
  PRIMARY KEY (user_id, role_id)
);

INSERT INTO users_roles (user_id, role_id) VALUES
  (1, 2),
  (2, 1),
  (2, 2),
  (2, 3),
  (3, 1),
  (4, 2);

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  job VARCHAR(250) NOT NULL,
  email VARCHAR(250),
  salary DECIMAL(9,2) NOT NULL
);

INSERT INTO employees (first_name, last_name, job, salary) VALUES
  ('JAMES', 'Jones', 'Laborer', 3000),
  ('ROBERT', 'Williams', 'Retail sales associate', 4500),
  ('MICHAEL', 'Johnson', 'Administrative assistant', 6600);