INSERT INTO core_datasource (
    name, description, url, username, password, driver_class_name,
    max_pool_size, min_idle, connection_timeout_ms, idle_timeout_ms, max_lifetime_ms,
    active, created_at, updated_at
) VALUES 
('pg_main', 'Primary PostgreSQL DB', 'jdbc:postgresql://localhost:5432/maindb', 'postgres', 'password123', 'org.postgresql.Driver', 10, 2, 30000, 600000, 1800000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('pg_reporting', 'Reporting DB', 'jdbc:postgresql://localhost:5432/reporting', 'report_user', 'reportpass', 'org.postgresql.Driver', 8, 2, 30000, 600000, 1800000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('mysql_legacy', 'Legacy MySQL DB', 'jdbc:mysql://localhost:3306/legacydb', 'legacy', 'legacy123', 'com.mysql.cj.jdbc.Driver', 5, 1, 20000, 300000, 1200000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('oracle_bi', 'Oracle for BI', 'jdbc:oracle:thin:@localhost:1521:orcl', 'bi_user', 'bi_pass', 'oracle.jdbc.OracleDriver', 12, 3, 25000, 500000, 1500000, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('h2_test', 'In-memory test DB', 'jdbc:h2:mem:testdb', 'sa', '', 'org.h2.Driver', 2, 1, 10000, 300000, 600000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO core_service_config (id, service_code, service_name, service_description, service_type, log_enabled, active, version)
VALUES
(1, 'svc_rest_1', 'REST Service 1', 'Call external API', 'REST', true, true, 1),
(2, 'svc_db_1', 'DB Service 1', 'Query customer data', 'DB', true, true, 1),
(3, 'svc_mail_1', 'Mail Service 1', 'Send notification emails', 'MAIL', false, true, 1),
(4, 'svc_file_1', 'File Service 1', 'Export data to CSV', 'FILE', true, true, 1),
(5, 'svc_soap_1', 'SOAP Service 1', 'Legacy SOAP integration', 'SOAP', false, true, 1);

INSERT INTO core_service_db (id, ds_id, sql_statement, sql_type, input_params, output_mapping, timeout_ms, retry_count, retry_backoff_ms, transactional, fetch_size, result_type, enabled)
VALUES
(2, 1, 'SELECT * FROM customers WHERE status = ?', 'QUERY', 'status', 'id:customerId,name:customerName', 3000, 1, 1000, true, 100, 'LIST', true);

INSERT INTO core_service_rest (id, target_url, http_method, content_type, timeout_ms, retry_count, retry_backoff_ms, payload_template, response_mapping, auth_id)
VALUES
(1, 'https://api.example.com/v1/data', 'GET', 'application/json', 5000, 2, 1500, null, '{"data": "result"}', null);

INSERT INTO core_service_mail (
    config_id, smtp_server, smtp_port, username, password, use_tls,
    mail_from, mail_to, mail_cc, mail_bcc, subject_template, body_template, headers,
    retry_count, retry_backoff_ms, timeout_ms, attachments_enabled, active
)
VALUES
(3, 'smtp.example.com', 587, 'noreply@example.com', 'securepass', true,
'noreply@example.com', 'user1@example.com', 'cc@example.com', 'bcc@example.com',
'Welcome, {{name}}', 'Hello {{name}}, your account is ready.', '{"X-Priority":"1"}',
1, 1000, 5000, false, true);

INSERT INTO core_service_file (
    config_id, file_path, file_action, file_format, file_template, file_name_pattern, 
    file_encoding, extra_config, retry_count, retry_backoff_ms, timeout_ms, active
)
VALUES
(4, '/data/exports/', 'WRITE', 'CSV', '{{id}},{{name}},{{email}}', 'export_*.csv',
'UTF-8', '{"delimiter":","}', 1, 1000, 7000, true);
