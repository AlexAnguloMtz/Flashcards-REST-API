DROP TABLE IF EXISTS flashcard;
DROP TABLE IF EXISTS study_session;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS app_role;
DROP TABLE IF EXISTS category;

CREATE TABLE app_role(
    id INT NOT NULL,
    name VARCHAR(20) UNIQUE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE app_user(
    id VARCHAR(36) NOT NULL,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(150) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY(role_id) REFERENCES app_role(id),
    PRIMARY KEY(id)
);

CREATE TABLE category(
    id VARCHAR(36) NOT NULL,
    name VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE study_session(
    id VARCHAR(36) NOT NULL,
    name VARCHAR(30) UNIQUE NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES app_user(id),
    FOREIGN KEY(category_id) REFERENCES category(id),
    PRIMARY KEY(id)
);

CREATE TABLE flashcard(
    id VARCHAR(36) NOT NULL,
    question VARCHAR(30) UNIQUE NOT NULL,
    answer VARCHAR(36) NOT NULL,
    study_session_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(study_session_id) REFERENCES study_session(id),
    PRIMARY KEY(id)
);