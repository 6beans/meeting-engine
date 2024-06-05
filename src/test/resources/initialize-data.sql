INSERT INTO  users (issuer, subject, user_name, email)
VALUES
    ('issuer1', 'subject1', '@username1', 'email1@example.com'),
    ('issuer2', 'subject2', '@username2', 'email2@example.com'),
    ('issuer3', 'subject3', '@username3', 'email3@example.com');

INSERT INTO tag (category, title)
VALUES
    ('Category1', 'Tag1'),
    ('Category2', 'Tag2');

INSERT INTO tag_users (tags_id, users_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1);

INSERT INTO  events (is_active, title)
VALUES
    ('true', 'Basketball'),
    ('false', 'Football'),
    ('true', 'Voleyball');

INSERT INTO  tag_events (tags_id, events_id)
VALUES
    (1, 1),
    (2, 1),
    (1, 2),
    (2, 3);