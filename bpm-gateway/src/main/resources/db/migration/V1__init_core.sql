-- ================= CORE METADATA =================

CREATE TABLE cfg_servers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL, -- REST, MAIL, DB...
    ip VARCHAR(50) NOT NULL,
    port INT NOT NULL,
    https BOOLEAN DEFAULT FALSE
);

CREATE TABLE cfg_auths (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,   -- định danh credential
    auth_type VARCHAR(20),

    username VARCHAR(100),               -- BASIC
    password VARCHAR(100),               -- BASIC

    token TEXT,                          -- BEARER
    api_key_header VARCHAR(100),         -- API_KEY
    api_key_value TEXT,                  -- API_KEY

    oauth2_client_id VARCHAR(100),       -- OAUTH2
    oauth2_client_secret VARCHAR(100),
    oauth2_token_url TEXT,

    role VARCHAR(50),
    scope VARCHAR(255),

    status VARCHAR(20) DEFAULT 'ACTIVE', -- thay cho active boolean
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cfg_datasources (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,   -- định danh datasource
    description TEXT,

    url VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,      -- nên mã hóa hoặc dùng vault
    driver_class_name VARCHAR(100) NOT NULL,

    -- Pool settings
    max_pool_size INTEGER DEFAULT 10,
    min_idle INTEGER DEFAULT 2,
    connection_timeout_ms INTEGER DEFAULT 30000,
    idle_timeout_ms INTEGER DEFAULT 600000,
    max_lifetime_ms INTEGER DEFAULT 1800000,

    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE service_type_enum AS ENUM ('REST', 'SOAP', 'DB', 'MAIL', 'FILE');

CREATE TABLE cfg_services (
    id BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(50) NOT NULL UNIQUE,
    service_name VARCHAR(100) NOT NULL,
    service_description TEXT,
    service_type service_type_enum NOT NULL,

    log_enabled BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================= DOCUMENT DOMAIN =================

CREATE TABLE cfg_doc_tmplts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,   -- filename: invoice.docx
    type VARCHAR(20) NOT NULL,    -- DOCX, XLSX, PDF...
    content BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cfg_doc_srvcs (
    id SERIAL PRIMARY KEY,
    template_id BIGINT NOT NULL REFERENCES cfg_doc_tmplts(id),
    server_id BIGINT NOT NULL REFERENCES cfg_servers(id),
    auth_id BIGINT REFERENCES cfg_auths(id),

    status VARCHAR(20) DEFAULT 'ACTIVE',
    version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================= SERVICE CONFIGS =================

CREATE TABLE cfg_svc_db (
    id BIGINT PRIMARY KEY REFERENCES cfg_services(id) ON DELETE CASCADE,

    datasource_id BIGINT NOT NULL REFERENCES cfg_datasources(id),
    sql_statement TEXT NOT NULL,
    sql_type VARCHAR(10) NOT NULL,   -- QUERY / UPDATE / PROC

    input_params TEXT,
    output_mapping TEXT,

    timeout_ms INTEGER DEFAULT 3000,
    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,

    transactional BOOLEAN DEFAULT FALSE,
    fetch_size INTEGER DEFAULT 100,

    result_type VARCHAR(20) DEFAULT 'LIST', -- LIST, SINGLE, NONE
    status VARCHAR(20) DEFAULT 'ENABLED',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cfg_svc_rest (
    id BIGINT PRIMARY KEY REFERENCES cfg_services(id) ON DELETE CASCADE,

    server_id BIGINT REFERENCES cfg_servers(id) ON DELETE SET NULL,
    auth_id BIGINT REFERENCES cfg_auths(id) ON DELETE SET NULL,

    path TEXT NOT NULL,
    http_method VARCHAR(10) NOT NULL DEFAULT 'GET',
    content_type VARCHAR(50) DEFAULT 'application/json',

    timeout_ms INTEGER DEFAULT 3000,
    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,

    request_mapping_script TEXT,
    response_mapping_script TEXT,

    headers JSONB DEFAULT '[]',
    query_params JSONB DEFAULT '[]',
    path_params JSONB DEFAULT '[]',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cfg_svc_mail (
    id BIGINT PRIMARY KEY REFERENCES cfg_services(id) ON DELETE CASCADE,

    server_id BIGINT NOT NULL REFERENCES cfg_servers(id),
    auth_id BIGINT REFERENCES cfg_auths(id),

    default_mail_from VARCHAR(100) NOT NULL,
    default_mail_to TEXT,
    default_mail_cc TEXT,
    default_mail_bcc TEXT,

    subject_template TEXT,
    body_template TEXT,
    headers TEXT,

    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,
    timeout_ms INTEGER DEFAULT 5000,

    attachments_enabled BOOLEAN DEFAULT FALSE,

    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================= LOGGING =================

CREATE TABLE svc_logs (
    id BIGSERIAL PRIMARY KEY,
    service_code VARCHAR(50) NOT NULL,
    request_data TEXT,
    mapped_request TEXT,
    response_data TEXT,
    status_code INT,
    duration_ms INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
