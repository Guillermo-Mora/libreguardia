-- ========================
-- ENUMS
-- ========================

CREATE TYPE enum_week_day AS ENUM (
'MONDAY',
'TUESDAY',
'WEDNESDAY',
'THURSDAY',
'FRIDAY',
'SATURDAY',
'SUNDAY'
);

-- ========================
-- INDEPENDENT TABLES
-- ========================

CREATE TABLE tbl_schedule_template (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_place_type (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_zone (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_academic_year (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL
);

CREATE TABLE tbl_professional_family (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_building (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_user_role (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_schedule_activity (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL UNIQUE,
	generates_service BOOLEAN NOT NULL
);

CREATE TABLE tbl_time_range (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	CONSTRAINT uq_time_range UNIQUE (start_time, end_time)
);

-- ========================
-- DEPENDENT TABLES
-- ========================

CREATE TABLE tbl_place (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL,
	floor VARCHAR(50) DEFAULT NULL,
	building_id UUID DEFAULT NULL REFERENCES tbl_building(id) ON DELETE RESTRICT,
	zone_id UUID NOT NULL REFERENCES tbl_zone(id) ON DELETE RESTRICT,
	place_type_id UUID NOT NULL REFERENCES tbl_place_type(id) ON DELETE RESTRICT,
	CONSTRAINT uq_place UNIQUE (name, building_id, zone_id)
);

CREATE TABLE tbl_user (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL,
	surname VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL UNIQUE,
	phone_number VARCHAR(20) NOT NULL,
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	password CHAR(60) NOT NULL,
	user_role_id UUID NOT NULL REFERENCES tbl_user_role(id) ON DELETE RESTRICT
);

CREATE TABLE tbl_course (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL,
	professional_family_id UUID NOT NULL REFERENCES tbl_professional_family(id) ON DELETE RESTRICT,
	academic_year_id UUID NOT NULL REFERENCES tbl_academic_year(id) ON DELETE RESTRICT,
	CONSTRAINT uq_course UNIQUE (name, academic_year_id)
);

CREATE TABLE tbl_group (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) NOT NULL,
	course_id UUID NOT NULL REFERENCES tbl_course(id) ON DELETE RESTRICT,
	CONSTRAINT uq_group UNIQUE (name, course_id)
);

CREATE TABLE tbl_schedule (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	week_day enum_week_day NOT NULL,
	group_id UUID REFERENCES tbl_group(id) ON DELETE RESTRICT,
	schedule_activity_id UUID NOT NULL REFERENCES tbl_schedule_activity(id) ON DELETE RESTRICT,
	place_id UUID NOT NULL REFERENCES tbl_place(id) ON DELETE RESTRICT,
	time_range_id UUID NOT NULL REFERENCES tbl_time_range(id) ON DELETE RESTRICT,
	user_id UUID NOT NULL REFERENCES tbl_user(id) ON DELETE RESTRICT,
	academic_year_id UUID NOT NULL REFERENCES tbl_academic_year(id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule UNIQUE (week_day, time_range_id, user_id, academic_year_id)
);

CREATE TABLE tbl_schedule_template_slot (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	week_day enum_week_day NOT NULL,
	schedule_template_id UUID NOT NULL REFERENCES tbl_schedule_template(id) ON DELETE RESTRICT,
	group_id UUID REFERENCES tbl_group(id) ON DELETE RESTRICT,
	schedule_activity_id UUID REFERENCES tbl_schedule_activity(id) ON DELETE RESTRICT,
	place_id UUID REFERENCES tbl_place(id) ON DELETE RESTRICT,
	time_range_id UUID NOT NULL REFERENCES tbl_time_range(id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule_template_slot UNIQUE (schedule_template_id, week_day, time_range_id)
);

CREATE TABLE tbl_absence (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	date DATE NOT NULL,
	schedule_id UUID NOT NULL REFERENCES tbl_schedule(id) ON DELETE RESTRICT,
	CONSTRAINT uq_absence UNIQUE (date, schedule_id)
);

CREATE TABLE tbl_service (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	is_signed BOOLEAN NOT NULL DEFAULT FALSE,
	absence_id UUID NOT NULL UNIQUE REFERENCES tbl_absence(id) ON DELETE CASCADE,
	user_id UUID DEFAULT NULL REFERENCES tbl_user(id) ON DELETE RESTRICT
);