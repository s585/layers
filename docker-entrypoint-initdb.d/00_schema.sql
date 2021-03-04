-- DDL (Data Definition Language)
-- DML (Data Manipulation Lanquage) -> Insert, Update, Delete
-- DRL/DQL (Data Query Language) -> Select
-- TCL (Transaction Control Language) -> Begin/Rollback/Commit
-- DCL (Data Control Language) -> GRANT/REVOKE

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    secret TEXT NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}',
    removed BOOLEAN NOT NULL DEFAULT false,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)