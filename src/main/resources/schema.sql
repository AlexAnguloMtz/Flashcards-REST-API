DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS app_role;

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
