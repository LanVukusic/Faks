CREATE TABLE users (
    uuid UUID PRIMARY KEY,
    phone VARCHAR(15) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    room INT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    disabled BOOLEAN NOT NULL DEFAULT FALSE,
    confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(10) NOT NULL CHECK (role IN ('admin', 'user')),  -- Fixed missing comma
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE auth (
    id SERIAL PRIMARY KEY,
    user_uuid UUID NOT NULL REFERENCES users(uuid) ON DELETE CASCADE,
    password_hash TEXT NOT NULL,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL REFERENCES users(uuid) ON DELETE CASCADE,
    date DATE NOT NULL,
    termin INT NOT NULL,
    washer INT NOT NULL,
    CONSTRAINT reservations_unique UNIQUE (uuid, date, termin, washer)  -- Replaced PRIMARY KEY with UNIQUE
);

CREATE OR REPLACE FUNCTION get_user_reservations(user_uuid UUID)
RETURNS TABLE (uuid UUID, date DATE, termin INT, washer INT) AS $$
BEGIN
    RETURN QUERY
    SELECT
        r.uuid,
        r.date,
        r.termin,
        r.washer
    FROM
        reservations r
    WHERE
        r.uuid = user_uuid;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_reservations_by_month(month DATE)
RETURNS TABLE (uuid UUID, date DATE, termin INT, washer INT) AS $$
BEGIN
    RETURN QUERY
    SELECT
        r.uuid,
        r.date,
        r.termin,
        r.washer
    FROM
        reservations r
    WHERE
        r.date >= DATE_TRUNC('month', month)
        AND r.date < DATE_TRUNC('month', month) + INTERVAL '1 month';
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION search_users(search_text TEXT)
RETURNS TABLE (uuid UUID, phone VARCHAR, name VARCHAR, surname VARCHAR, email VARCHAR, role VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.uuid,
        u.phone,
        u.name,
        u.surname,
        u.email,
        u.role
    FROM
        users u
    WHERE
        LOWER(u.name) LIKE LOWER('%' || search_text || '%')
        OR LOWER(u.surname) LIKE LOWER('%' || search_text || '%')
        OR LOWER(u.phone) LIKE LOWER('%' || search_text || '%')
        OR LOWER(u.email) LIKE LOWER('%' || search_text || '%');
END;
$$ LANGUAGE plpgsql;
