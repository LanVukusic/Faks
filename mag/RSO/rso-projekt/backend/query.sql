-- name: DeleteReservation :exec
DELETE FROM reservations
WHERE id = $1;

-- name: ConfirmUser :exec
UPDATE users
SET confirmed = TRUE
WHERE uuid = $1;

-- name: BanUser :exec
UPDATE users
SET disabled = TRUE
WHERE uuid = $1;

-- name: EditUser :exec
UPDATE users
SET phone = $2,
    name = $3,
    surname = $4,
    room = $5,
    email = $6,
    role = $7
WHERE uuid = $1;

-- name: GetUser :one
SELECT uuid, phone, name, surname, room, email, disabled, confirmed, role, created_at, updated_at
FROM users
WHERE uuid = $1;

-- name: GetUsers :many
SELECT uuid, phone, name, surname, room, email, disabled, confirmed, role, created_at, updated_at
FROM users
ORDER BY uuid;

-- name: AddReservation :one
INSERT INTO reservations (
    uuid, 
    date, 
    termin, 
    washer
) VALUES (
    $1, $2, $3, $4
)
RETURNING id, uuid, date, termin, washer;

-- name: GetUserReservations :many
SELECT
    r.uuid,
    r.id,
    r.date,
    r.termin,
    r.washer
FROM
    reservations r
WHERE
    r.uuid = $1;

-- name: GetReservationsByMonth :many
SELECT
    r.id,
    r.uuid,
    r.date,
    r.termin,
    r.washer
FROM
    reservations r
WHERE
    r.date >= DATE_TRUNC('month', $1::date)
    AND r.date < DATE_TRUNC('month', $1::date) + INTERVAL '1 month';

-- name: SearchUsers :many
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
    LOWER(u.name) LIKE LOWER('%' || $1 || '%')
    OR LOWER(u.surname) LIKE LOWER('%' || $1 || '%')
    OR LOWER(u.phone) LIKE LOWER('%' || $1 || '%')
    OR LOWER(u.email) LIKE LOWER('%' || $1 || '%');
