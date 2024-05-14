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
	CONSTRAINT company_fk FOREIGN KEY(company_id) REFERENCES tb_m_companies(id)
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
	created_by VARCHAR(36) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(36),
	updated_at TIMESTAMP,
	vrsion INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT payroll_fk FOREIGN KEY(payroll_id) REFERENCES tb_r_payrolls(id)
);

CREATE TABLE tb_r_schedules (
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
