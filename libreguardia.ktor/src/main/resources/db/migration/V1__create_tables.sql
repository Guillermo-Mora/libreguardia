-- ========================
-- INDEPENDENT TABLES
-- ========================

CREATE TABLE schedule_template (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE place_type (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE zone (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE academic_year (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL
);

CREATE TABLE professional_family (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE building (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_role (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE schedule_activity (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE,
	generates_service BOOLEAN NOT NULL
);

CREATE TABLE time_range (
	id UUID PRIMARY KEY,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	CONSTRAINT uq_time_range UNIQUE (start_time, end_time)
);

-- ========================
-- DEPENDENT TABLES
-- ========================

CREATE TABLE place (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	floor VARCHAR(50) DEFAULT NULL,
	building_id UUID DEFAULT NULL REFERENCES building(id) ON DELETE RESTRICT,
	zone_id UUID NOT NULL REFERENCES zone(id) ON DELETE RESTRICT,
	place_type_id UUID NOT NULL REFERENCES place_type(id) ON DELETE RESTRICT,
	CONSTRAINT uq_place UNIQUE (name, building_id, zone_id)
);

CREATE TABLE user_tbl (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	surname VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL UNIQUE,
	phone_number VARCHAR(20) NOT NULL,
	is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
	password VARCHAR(60) NOT NULL,
	user_role_id UUID NOT NULL REFERENCES user_role(id) ON DELETE RESTRICT
);

CREATE TABLE course (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	professional_family_id UUID NOT NULL REFERENCES professional_family(id) ON DELETE RESTRICT,
	academic_year_id UUID NOT NULL REFERENCES academic_year(id) ON DELETE RESTRICT,
	CONSTRAINT uq_course UNIQUE (name, academic_year_id)
);

CREATE TABLE group_tbl (
	id UUID PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	course_id UUID NOT NULL REFERENCES course(id) ON DELETE RESTRICT,
	CONSTRAINT uq_group UNIQUE (name, course_id)
);

CREATE TABLE schedule (
	id UUID PRIMARY KEY,
	is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
	week_day VARCHAR(9) NOT NULL,
	group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT,
	schedule_activity_id UUID NOT NULL REFERENCES schedule_activity(id) ON DELETE RESTRICT,
	place_id UUID NOT NULL REFERENCES place(id) ON DELETE RESTRICT,
	time_range_id UUID NOT NULL REFERENCES time_range(id) ON DELETE RESTRICT,
	user_id UUID NOT NULL REFERENCES user_tbl(id) ON DELETE RESTRICT,
	academic_year_id UUID NOT NULL REFERENCES academic_year(id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule UNIQUE (week_day, time_range_id, user_id, academic_year_id)
);

CREATE TABLE schedule_template_slot (
	id UUID PRIMARY KEY,
	week_day VARCHAR(9) NOT NULL,
	schedule_template_id UUID NOT NULL REFERENCES schedule_template(id) ON DELETE CASCADE,
	group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT,
	schedule_activity_id UUID REFERENCES schedule_activity(id) ON DELETE RESTRICT,
	place_id UUID REFERENCES place(id) ON DELETE RESTRICT,
	time_range_id UUID NOT NULL REFERENCES time_range(id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule_template_slot UNIQUE (schedule_template_id, week_day, time_range_id)
);

CREATE TABLE absence (
	id UUID PRIMARY KEY,
	date DATE NOT NULL,
	schedule_id UUID NOT NULL REFERENCES schedule(id) ON DELETE RESTRICT,
	CONSTRAINT uq_absence UNIQUE (date, schedule_id)
);

CREATE TABLE service_tbl (
	id UUID PRIMARY KEY,
	is_signed BOOLEAN NOT NULL DEFAULT FALSE,
	absence_id UUID NOT NULL UNIQUE REFERENCES absence(id) ON DELETE CASCADE,
	user_id UUID DEFAULT NULL REFERENCES user_tbl(id) ON DELETE RESTRICT
);