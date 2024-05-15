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

INSERT INTO tb_m_user_roles (id, role_name, role_code, created_by, created_at, is_active, vrsion) VALUES
	('b4f6c0bb-ef56-4b5c-b804-9d0d21186394', 'Super Admin', 'SA001', '1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'PS', 'PS001', '1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'Client', 'CLNT1', '1', NOW(), TRUE, 0);

INSERT INTO tb_m_companies (id, company_name, logo_id, created_by, created_at, is_active, vrsion) VALUES
	('cc8c66d3-96ab-4ecc-bbe9-b984e8562190', 'Lawencon', null, '1', NOW(), TRUE, 0);

commit;
INSERT INTO tb_m_users (id, email, pwd, role_id, company_id, created_by, created_at, is_active, vrsion) VALUES 
	(uuid_generate_v4(), 'example@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', 'b4f6c0bb-ef56-4b5c-b804-9d0d21186394', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0);


INSERT INTO tb_m_users (id, email, pwd, role_id, company_id, created_by, created_at, is_active, vrsion) VALUES 
	(uuid_generate_v4(), 'a@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'b@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'c@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '1e5b3f28-67c4-4615-b0e0-7e7c90f61608', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'd@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'e@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0),
	(uuid_generate_v4(), 'f@mail.com', '$2y$10$AMf5FaHHEF5cJFBNEDHl9.4b/QjWSfPULHtJQgqyL2NYsfgkVAyIq', '8487cf5c-044d-44e9-8e02-e51fd8c8d127', 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190','1', NOW(), TRUE, 0);


SELECT * FROM tb_m_users tmu ;
SELECT * FROM tb_m_user_roles tmur2 ;
SELECT * FROM tb_m_companies tmc ;
SELECT * FROM tb_m_user_roles tmur WHERE id = 'b4f6c0bb-ef56-4b5c-b804-9d0d21186394';
SELECT * FROM tb_m_companies tmc WHERE id = 'cc8c66d3-96ab-4ecc-bbe9-b984e8562190';
TRUNCATE TABLE tb_m_users RESTART IDENTITY CASCADE;

select user0_.id as id1_3_, user0_.created_at as created_2_3_, user0_.created_by as created_3_3_, user0_.is_active as is_activ4_3_, user0_.updated_at as updated_5_3_, user0_.updated_by as updated_6_3_, user0_.vrsion as vrsion7_3_, user0_.company_id as company11_3_, user0_.email as email8_3_, user0_.file_id as file_id12_3_, user0_.full_name as full_nam9_3_, user0_.pwd as pwd10_3_, user0_.role_id as role_id13_3_ from tb_m_users user0_ where user0_.email='example@mail.com';
select company0_.id as id1_0_0_, company0_.created_at as created_2_0_0_, company0_.created_by as created_3_0_0_, company0_.is_active as is_activ4_0_0_, company0_.updated_at as updated_5_0_0_, company0_.updated_by as updated_6_0_0_, company0_.vrsion as vrsion7_0_0_, company0_.company_name as company_8_0_0_, company0_.logo_id as logo_id9_0_0_, file1_.id as id1_1_1_, file1_.created_at as created_2_1_1_, file1_.created_by as created_3_1_1_, file1_.is_active as is_activ4_1_1_, file1_.updated_at as updated_5_1_1_, file1_.updated_by as updated_6_1_1_, file1_.vrsion as vrsion7_1_1_, file1_.stored_path as stored_p8_1_1_ from tb_m_companies company0_ inner join tb_m_files file1_ on company0_.logo_id=file1_.id where company0_.id='cc8c66d3-96ab-4ecc-bbe9-b984e8562190';
