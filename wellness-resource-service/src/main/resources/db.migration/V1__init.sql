CREATE TABLE IF NOT EXISTS resources (
                                         resource_id BIGSERIAL PRIMARY KEY,
                                         title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    category VARCHAR(64),
    url VARCHAR(1024)
    );
