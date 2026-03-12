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
	schedule_template_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	schedule_template_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_place_type (
	place_type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	place_type_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_zone (
	zone_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	zone_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_academic_year (
	academic_year_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	academic_year_name VARCHAR(50) NOT NULL UNIQUE,
	academic_year_start DATE NOT NULL,
	academic_year_end DATE NOT NULL
);

CREATE TABLE tbl_professional_family (
professional_family_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
professional_family_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_building (
	building_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	building_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_user_role (
	user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	user_role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tbl_schedule_activity (
	schedule_activity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	schedule_activity_name VARCHAR(50) NOT NULL UNIQUE,
	schedule_activity_generates_service BOOLEAN NOT NULL
);

CREATE TABLE tbl_time_range (
	time_range_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	time_range_is_active BOOLEAN NOT NULL DEFAULT TRUE,
	time_range_start TIME NOT NULL,
	time_range_end TIME NOT NULL,
	CONSTRAINT uq_time_range UNIQUE (time_range_start, time_range_end)
);

-- ========================
-- DEPENDENT TABLES
-- ========================

CREATE TABLE tbl_place (
	place_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	place_name VARCHAR(50) NOT NULL,
	place_floor VARCHAR(50) DEFAULT NULL,
	building_fk UUID DEFAULT NULL REFERENCES tbl_building(building_id) ON DELETE RESTRICT,
	zone_fk UUID NOT NULL REFERENCES tbl_zone(zone_id) ON DELETE RESTRICT,
	place_type_fk UUID NOT NULL REFERENCES tbl_place_type(place_type_id) ON DELETE RESTRICT,
	CONSTRAINT uq_place UNIQUE (place_name, building_fk, zone_fk)
);

CREATE TABLE tbl_user (
	user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	user_name VARCHAR(50) NOT NULL,
	user_surname VARCHAR(50) NOT NULL,
	user_email VARCHAR(50) NOT NULL UNIQUE,
	user_phone_number VARCHAR(20) NOT NULL,
	user_is_active BOOLEAN NOT NULL DEFAULT TRUE,
	user_password CHAR(60) NOT NULL,
	user_role_fk UUID NOT NULL REFERENCES tbl_user_role(user_role_id) ON DELETE RESTRICT
);

CREATE TABLE tbl_course (
	course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	course_name VARCHAR(50) NOT NULL,
	professional_family_fk UUID NOT NULL REFERENCES tbl_professional_family(professional_family_id) ON DELETE RESTRICT,
	academic_year_fk UUID NOT NULL REFERENCES tbl_academic_year(academic_year_id) ON DELETE RESTRICT,
	CONSTRAINT uq_course UNIQUE (course_name, academic_year_fk)
);

CREATE TABLE tbl_group (
	group_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	group_name VARCHAR(50) NOT NULL,
	course_fk UUID NOT NULL REFERENCES tbl_course(course_id) ON DELETE RESTRICT,
	CONSTRAINT uq_group UNIQUE (group_name, course_fk)
);

CREATE TABLE tbl_schedule (
	schedule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	schedule_is_active BOOLEAN NOT NULL DEFAULT TRUE,
	schedule_week_day enum_week_day NOT NULL,
	group_fk UUID REFERENCES tbl_group(group_id) ON DELETE RESTRICT,
	schedule_activity_fk UUID NOT NULL REFERENCES tbl_schedule_activity(schedule_activity_id) ON DELETE RESTRICT,
	place_fk UUID NOT NULL REFERENCES tbl_place(place_id) ON DELETE RESTRICT,
	time_range_fk UUID NOT NULL REFERENCES tbl_time_range(time_range_id) ON DELETE RESTRICT,
	user_fk UUID NOT NULL REFERENCES tbl_user(user_id) ON DELETE RESTRICT,
	academic_year_fk UUID NOT NULL REFERENCES tbl_academic_year(academic_year_id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule UNIQUE (schedule_week_day, time_range_fk, user_fk, academic_year_fk)
);

CREATE TABLE tbl_schedule_template_slot (
	schedule_template_slot_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	schedule_template_slot_week_day enum_week_day NOT NULL,
	schedule_template_fk UUID NOT NULL REFERENCES tbl_schedule_template(schedule_template_id) ON DELETE RESTRICT,
	group_fk UUID REFERENCES tbl_group(group_id) ON DELETE RESTRICT,
	schedule_activity_fk UUID REFERENCES tbl_schedule_activity(schedule_activity_id) ON DELETE RESTRICT,
	place_fk UUID REFERENCES tbl_place(place_id) ON DELETE RESTRICT,
	time_range_fk UUID NOT NULL REFERENCES tbl_time_range(time_range_id) ON DELETE RESTRICT,
	CONSTRAINT uq_schedule_template_slot UNIQUE (schedule_template_fk, schedule_template_slot_week_day, time_range_fk)
);

CREATE TABLE tbl_absence (
	absence_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	absence_date DATE NOT NULL,
	schedule_fk UUID NOT NULL REFERENCES tbl_schedule(schedule_id) ON DELETE RESTRICT,
	CONSTRAINT uq_absence UNIQUE (absence_date, schedule_fk)
);

CREATE TABLE tbl_service (
	service_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	service_is_signed BOOLEAN NOT NULL DEFAULT FALSE,
	absence_fk UUID NOT NULL UNIQUE REFERENCES tbl_absence(absence_id) ON DELETE CASCADE,
	user_fk UUID DEFAULT NULL REFERENCES tbl_user(user_id) ON DELETE RESTRICT
);