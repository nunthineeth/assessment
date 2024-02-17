-- Drop tables if they exist
DROP TABLE IF EXISTS roles CASCADE;

CREATE TABLE roles
(
    role_id SERIAL PRIMARY KEY,
    role_code VARCHAR(255) UNIQUE NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false NOT NULL
);

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    user_id BIGINT(10) PRIMARY KEY,
    username VARCHAR(16) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false NOT NULL,

    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS permissions CASCADE;

CREATE TABLE permissions
(
    permission_id SERIAL PRIMARY KEY,
    permission_code VARCHAR(255) UNIQUE NOT NULL,
    permission_name VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false NOT NULL
);

DROP TABLE IF EXISTS role_permissions CASCADE;

CREATE TABLE role_permissions
(
    role_permission_id SERIAL PRIMARY KEY,
    role_id INT REFERENCES roles(role_id) ON DELETE CASCADE,
    permission_id INT REFERENCES permissions(permission_id),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    CONSTRAINT unique_role_permission UNIQUE (role_id, permission_id)
);

-- Initial data
INSERT INTO roles (role_code, role_name)
VALUES ('ADMIN', 'Administrator'),
       ('USER', 'Regular User');

INSERT INTO permissions (permission_code, permission_name)
VALUES  ('USER_CREATE', 'Create User'),  --1
        ('USER_MANAGE', 'Manage User'), --2
        ('USER_READ', 'Read User'), --3
        ('LOTTERY_CREATE', 'Create Lottery'), --4
        ('LOTTERY_MANAGE', 'Manage Lottery'), --5
        ('LOTTERY_READ', 'Read Lottery'); --6

INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (2, 1),
       (2, 2),
       (2, 6);

INSERT INTO users (username,"password",email,firstname,lastname,role_id,is_deleted) VALUES
	 ('admin','$2a$10$jsdHRyrqEI0eQN08CsFbieaFW4bg589J6dH4czpiYY3TYk90cPO8a','admin@gmail.com','admin','lottery',1,false),
	 ('user1','$2a$10$HUvXwawtE7P/9YgE7XFivueW6kviQ02d2tkUWZkLJ6pEu9g9jDZ/C','user1@gmail.com','user1','lottery',2,false);
