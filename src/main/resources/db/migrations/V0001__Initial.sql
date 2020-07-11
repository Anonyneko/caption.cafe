-- Add UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users
CREATE TABLE users
(
    id       uuid          NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    username VARCHAR(1024) NOT NULL UNIQUE,
    email    VARCHAR(1024) UNIQUE,
    password VARCHAR(1024),
    deleted  BOOLEAN       NOT NULL        DEFAULT false,
    PRIMARY KEY (id)
);

-- Roles
CREATE TABLE roles
(
    id   uuid    NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id   uuid    NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles_permissions
(
    role_id       uuid REFERENCES roles (id),
    permission_id uuid REFERENCES permissions (id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users_roles
(
    user_id uuid REFERENCES users (id),
    role_id uuid REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);