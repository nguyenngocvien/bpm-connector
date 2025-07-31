CREATE TABLE IF NOT EXISTS core_service_mail (
    config_id BIGINT PRIMARY KEY REFERENCES core_service_config(id) ON DELETE CASCADE,
    
    smtp_server TEXT NOT NULL,
    smtp_port INTEGER NOT NULL,
    username VARCHAR(100),
    password VARCHAR(100),
    use_tls BOOLEAN DEFAULT TRUE,
    
    mail_from VARCHAR(100) NOT NULL,

    mail_to TEXT,
    mail_cc TEXT,
    mail_bcc TEXT,

    subject_template TEXT,
    body_template TEXT,
    
    headers TEXT,

    retry_count INTEGER DEFAULT 0,
    retry_backoff_ms INTEGER DEFAULT 1000,
    timeout_ms INTEGER DEFAULT 5000,

    attachments_enabled BOOLEAN DEFAULT FALSE,

    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
