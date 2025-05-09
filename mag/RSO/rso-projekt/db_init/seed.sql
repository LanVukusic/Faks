-- Insert 10 users with different roles
INSERT INTO users (uuid, phone, name, surname, room, email, role)
VALUES
(uuid_generate_v4(), '1234567890', 'John', 'Doe', 101, 'john.doe1@example.com', 'user'),
(uuid_generate_v4(), '1234567891', 'Jane', 'Smith', 102, 'jane.smith@example.com', 'admin'),
(uuid_generate_v4(), '1234567892', 'Alice', 'Johnson', 103, 'alice.johnson@example.com', 'user'),
(uuid_generate_v4(), '1234567893', 'Bob', 'Brown', 104, 'bob.brown@example.com', 'user'),
(uuid_generate_v4(), '1234567894', 'Charlie', 'Davis', 105, 'charlie.davis@example.com', 'admin'),
(uuid_generate_v4(), '1234567895', 'Diana', 'Clark', 106, 'diana.clark@example.com', 'user'),
(uuid_generate_v4(), '1234567896', 'Eve', 'Miller', 107, 'eve.miller@example.com', 'admin'),
(uuid_generate_v4(), '1234567897', 'Frank', 'Wilson', 108, 'frank.wilson@example.com', 'user'),
(uuid_generate_v4(), '1234567898', 'Grace', 'Taylor', 109, 'grace.taylor@example.com', 'user'),
(uuid_generate_v4(), '1234567899', 'Hank', 'Anderson', 110, 'hank.anderson@example.com', 'admin');

-- Insert random reservations for some of the users
INSERT INTO reservations (uuid, date, termin, washer)
SELECT
    uuid,
    CURRENT_DATE + (random() * 30)::int, -- Random date within the next 30 days
    (random() * 4)::int + 1,            -- Random termin between 1 and 5
    (random() * 3)::int + 1             -- Random washer between 1 and 4
FROM users
WHERE role = 'user'
LIMIT 15; -- Add 15 random reservations
