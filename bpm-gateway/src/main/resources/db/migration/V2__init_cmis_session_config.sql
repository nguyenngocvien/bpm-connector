CREATE TABLE cmis_session_config (
    id              SERIAL PRIMARY KEY, -- Oracle thì dùng sequence
    name            VARCHAR(100) UNIQUE NOT NULL,      -- Tên cấu hình (DEV/PRO/...)
    atompub_url     VARCHAR(500) NOT NULL,             -- CMIS endpoint URL
    repository_id   VARCHAR(100) NOT NULL,             -- Repository ID
    username        VARCHAR(100) NOT NULL,             -- Tài khoản
    password        VARCHAR(200) NOT NULL,             -- Mật khẩu (có thể mã hóa)
    binding_type    VARCHAR(50) DEFAULT 'atompub',     -- Kiểu binding (atompub/webservices/browser)
    active          BOOLEAN DEFAULT TRUE,              -- Cấu hình có được dùng không
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
