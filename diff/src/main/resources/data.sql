DROP TABLE diff_document;

CREATE TABLE diff_document(
    id int  PRIMARY KEY NOT NULL,
    left clob,
    right clob
);

INSERT INTO diff_document(id, left, right) VALUES (1, 'A', 'B');