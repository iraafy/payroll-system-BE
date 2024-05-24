CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE tb_m_email_templates (
	id VARCHAR(36) PRIMARY KEY,
	email_code VARCHAR(15) NOT NULL,
	email_subject TEXT NOT NULL,
	email_body TEXT NOT NULL,
	email_content TEXT NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_m_files (
	id VARCHAR(36) PRIMARY KEY,
	file_content TEXT,
	file_ext VARCHAR(10),
	file_name TEXT,
	stored_path TEXT,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_m_companies (
	id VARCHAR(36) PRIMARY KEY,
	company_name VARCHAR(50) NOT NULL,
	logo_id VARCHAR(36),
	default_payment_day SMALLINT NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT logo_fk FOREIGN KEY(logo_id) REFERENCES tb_m_files(id)
);

CREATE TABLE tb_m_user_roles (
	id VARCHAR(36) PRIMARY KEY,
	role_name VARCHAR(25),
	role_code CHAR(5) NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT role_code_bk UNIQUE(role_code)
);

CREATE TABLE tb_m_users (
	id VARCHAR(36) PRIMARY KEY,
	role_id VARCHAR(36) NOT NULL,
	company_id VARCHAR(36) NOT NULL,
	file_id VARCHAR(36),
	full_name VARCHAR(100),
	email VARCHAR(50) NOT NULL,
	pwd TEXT NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT email_bk UNIQUE(email),
	CONSTRAINT role_fk FOREIGN KEY(role_id) REFERENCES tb_m_user_roles(id),
	CONSTRAINT company_fk FOREIGN KEY(company_id) REFERENCES tb_m_companies(id),
	CONSTRAINT file_fk FOREIGN KEY(file_id) REFERENCES tb_m_files(id)
);

CREATE TABLE tb_m_notifications(
	id VARCHAR(36) PRIMARY KEY,
	notification_content TEXT NOT NULL,
	context_url TEXT,
	context_id VARCHAR(36),
	user_id VARCHAR(36) NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT user_fk FOREIGN KEY(user_id) REFERENCES tb_m_users(id)
);

CREATE TABLE tb_r_client_assignments (
	id VARCHAR(36) PRIMARY KEY,
	ps_id VARCHAR(36) NOT NULL,
	client_id VARCHAR(36) NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT ps_fk FOREIGN KEY(ps_id) REFERENCES tb_m_users(id),
	CONSTRAINT client_fk FOREIGN KEY(client_id) REFERENCES tb_m_users(id),
	CONSTRAINT client_bk UNIQUE(client_id)
);

CREATE TABLE tb_r_chats (
	id VARCHAR(36) PRIMARY KEY,
	message TEXT NOT NULL,
	recipient_id VARCHAR(36) NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT recipient_fk FOREIGN KEY(recipient_id) REFERENCES tb_m_users(id)
);

CREATE TABLE tb_r_payrolls (
	id VARCHAR(36) PRIMARY KEY,
	client_id VARCHAR(36) NOT NULL,
	title TEXT,
	scheduled_date TIMESTAMP NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT client_fk FOREIGN KEY(client_id) REFERENCES tb_m_users(id)
);

CREATE TABLE tb_r_payroll_details (
	id VARCHAR(36) PRIMARY KEY,
	payroll_id VARCHAR(36) NOT NULL,
	description TEXT,
	file_id VARCHAR(36),
	max_upload_date TIMESTAMP NOT NULL,
	ps_acknowledge BOOLEAN,
	client_acknowledge BOOLEAN,
	for_client Boolean NOT NULL,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT payroll_fk FOREIGN KEY(payroll_id) REFERENCES tb_r_payrolls(id),
	CONSTRAINT file_fk FOREIGN KEY(file_id) REFERENCES tb_m_files(id)
);

CREATE TABLE tb_r_reschedules (
	id VARCHAR(36) PRIMARY KEY,
	new_scheduled_date TIMESTAMP NOT NULL,
	payroll_id VARCHAR(36) NOT NULL,
	is_approve BOOLEAN,
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT payroll_fk FOREIGN KEY(payroll_id) REFERENCES tb_r_payrolls(id)
);

INSERT INTO tb_m_user_roles (id, role_name, role_code, created_by, created_at, is_active, vrsion) VALUES
	('b4f6c0bb-ef56-4b5c-b804-9d0d21186394', 'Super Admin', 'SA001', '1', NOW(), TRUE, 0),
	('1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'PS', 'PS001', '1', NOW(), TRUE, 0),
	('8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'Client', 'CLNT1', '1', NOW(), TRUE, 0);

INSERT INTO tb_m_companies (id, company_name, logo_id, default_payment_day, created_by, created_at, is_active, vrsion) VALUES
	('cc8c66d3-96ab-4ecc-bbe9-b984e8562190', 'Lawencon', null, 31, '1', NOW(), TRUE, 0);

INSERT INTO tb_m_users (id, full_name, email, pwd, role_id, company_id, created_by, created_at, is_active, vrsion) VALUES 
    (uuid_generate_v4(), 'Asmodeus', 'a@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
    (uuid_generate_v4(), 'Beelzebub', 'b@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
    (uuid_generate_v4(), 'Behamoth', 'c@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
    (uuid_generate_v4(), 'Lucifer', 'd@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
    (uuid_generate_v4(), 'Satan', 'e@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
    (uuid_generate_v4(), 'Leviathan', 'f@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0);

INSERT INTO tb_m_users (id, full_name, email, pwd, role_id, company_id, created_by, created_at, is_active, vrsion) VALUES 
	(uuid_generate_v4(), 'SUPEEEER Admin', 'example@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', 'b4f6c0bb-ef56-4b5c-b804-9d0d21186394', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0);


DROP TABLE tb_r_reschedules, tb_r_payroll_details, tb_r_payrolls, tb_r_chats, tb_r_client_assignments, tb_m_users, tb_m_user_roles, tb_m_companies, tb_m_files, tb_m_notifications, tb_m_email_templates;

---------------------------TRUNCATE------------------------------
--TRUNCATE tb_m_companies RESTART IDENTITY CASCADE;
--TRUNCATE tb_m_email_templates RESTART IDENTITY CASCADE;
--TRUNCATE tb_m_files RESTART IDENTITY CASCADE;
--TRUNCATE tb_m_notifications RESTART IDENTITY CASCADE;
--TRUNCATE tb_m_user_roles RESTART IDENTITY CASCADE;
--TRUNCATE tb_m_users RESTART IDENTITY CASCADE;
--TRUNCATE tb_r_chats RESTART IDENTITY CASCADE;
--TRUNCATE tb_r_client_assignments RESTART IDENTITY CASCADE;
--TRUNCATE tb_r_payroll_details RESTART IDENTITY CASCADE;
--TRUNCATE tb_r_payrolls RESTART IDENTITY CASCADE;
--TRUNCATE tb_r_reschedules RESTART IDENTITY CASCADE;
---------------------------TRUNCATE------------------------------

--SELECT * FROM tb_r_client_assignments trca ;
--SELECT * FROM tb_m_users tmu ;
--SELECT * FROM tb_r_payroll_details trpd ;
SELECT * FROM tb_m_notifications tmn ;

--SELECT * FROM tb_r_client_assignments trca RIGHT JOIN tb_m_users tmu ON trca.client_id = tmu.id WHERE tmu.role_id = '8487cf5c-044d-44e9-8e02-e51fd8c8d127';