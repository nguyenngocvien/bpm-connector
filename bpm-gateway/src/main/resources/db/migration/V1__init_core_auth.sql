CREATE TABLE core_service_auth (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,            -- tên định danh cho auth
    auth_type VARCHAR(20) NOT NULL,               -- BASIC, BEARER, API_KEY, OAUTH2, NONE

    username VARCHAR(100),                        -- BASIC
    password VARCHAR(100),                        -- BASIC

    token TEXT,                                   -- BEARER
    api_key_header VARCHAR(100),                  -- API_KEY
    api_key_value TEXT,                           -- API_KEY

    oauth2_client_id VARCHAR(100),                -- OAUTH2 (optional future-proof)
    oauth2_client_secret VARCHAR(100),
    oauth2_token_url TEXT,
    
    role VARCHAR(50),
    scope VARCHAR(255),

    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE auth_type_enum AS ENUM ('BASIC', 'BEARER', 'API_KEY', 'OAUTH2', 'NONE');

ALTER TABLE core_service_auth
  ALTER COLUMN auth_type TYPE auth_type_enum USING auth_type::auth_type_enum;