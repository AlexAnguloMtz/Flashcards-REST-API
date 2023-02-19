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
    app_user_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(app_user_id) REFERENCES app_user(id),
    FOREIGN KEY(category_id) REFERENCES category(id),
    PRIMARY KEY(id)
);