-- ========================
-- APP SETTINGS TABLE
-- ========================

CREATE TABLE app_settings (
                              name VARCHAR(50) PRIMARY KEY,
                              status VARCHAR(50) NOT NULL
);

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
                       name VARCHAR(50) NOT NULL UNIQUE,
                       floor VARCHAR(50) DEFAULT NULL,
                       building_id UUID DEFAULT NULL REFERENCES building(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                       zone_id UUID NOT NULL REFERENCES zone(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                       place_type_id UUID NOT NULL REFERENCES place_type(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE user_tbl (
                          id UUID PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          surname VARCHAR(50) NOT NULL,
                          email VARCHAR(50) NOT NULL UNIQUE,
                          phone_number VARCHAR(20) NOT NULL,
                          is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                          password VARCHAR(60) NOT NULL,
                          user_role_id UUID NOT NULL REFERENCES user_role(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE course (
                        id UUID PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        professional_family_id UUID NOT NULL REFERENCES professional_family(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE group_tbl (
                           id UUID PRIMARY KEY,
                           name VARCHAR(50) NOT NULL UNIQUE,
                           points_multiplier DECIMAL(3,1) NOT NULL DEFAULT 1.0,
                           course_id UUID NOT NULL REFERENCES course(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE schedule (
                          id UUID PRIMARY KEY,
                          week_day VARCHAR(9) NOT NULL,
                          group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          schedule_activity_id UUID NOT NULL REFERENCES schedule_activity(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          place_id UUID NOT NULL REFERENCES place(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          time_range_id UUID NOT NULL REFERENCES time_range(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          user_id UUID NOT NULL REFERENCES user_tbl(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                          CONSTRAINT uq_schedule UNIQUE (week_day, time_range_id, user_id)
);

CREATE TABLE schedule_template_slot (
                                        id UUID PRIMARY KEY,
                                        week_day VARCHAR(9) NOT NULL,
                                        schedule_template_id UUID NOT NULL REFERENCES schedule_template(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                                        group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        schedule_activity_id UUID REFERENCES schedule_activity(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        place_id UUID REFERENCES place(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        time_range_id UUID NOT NULL REFERENCES time_range(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT uq_schedule_template_slot UNIQUE (schedule_template_id, week_day, time_range_id)
);

CREATE TABLE absence (
                         id UUID PRIMARY KEY,
                         date DATE NOT NULL,
                         schedule_id UUID NOT NULL REFERENCES schedule(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                         CONSTRAINT uq_absence UNIQUE (date, schedule_id)
);

CREATE TABLE absence_history (
                                 id UUID PRIMARY KEY,
                                 date DATE NOT NULL,
                                 user_name VARCHAR(50) NOT NULL,
                                 user_surname VARCHAR(50) NOT NULL,
                                 user_email VARCHAR(50) NOT NULL,
                                 place_name VARCHAR(50) NOT NULL,
                                 schedule_activity_name VARCHAR(50) NOT NULL,
                                 group_name VARCHAR(50) DEFAULT NULL,
                                 time_range_start_time TIME NOT NULL,
                                 time_range_end_time TIME NOT NULL,
                                 academic_year_id UUID NOT NULL REFERENCES academic_year(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 absence_id UUID REFERENCES absence(id) ON DELETE SET NULL ON UPDATE RESTRICT,
                                 user_id UUID REFERENCES user_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE service_tbl (
                             id UUID PRIMARY KEY,
                             points_obtained DECIMAL(8,1) NOT NULL DEFAULT 1,
                             absence_id UUID NOT NULL UNIQUE REFERENCES absence(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                             cover_user_id UUID DEFAULT NULL REFERENCES user_tbl(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                             assigned_user_id UUID REFERENCES user_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE service_history (
                                 id UUID PRIMARY KEY,
                                 points_obtained DECIMAL(8,1) NOT NULL,
                                 cover_user_name VARCHAR(50) NOT NULL,
                                 cover_user_surname VARCHAR(50) NOT NULL,
                                 cover_user_email VARCHAR(50) NOT NULL,
                                 assigned_user_name VARCHAR(50),
                                 assigned_user_surname VARCHAR(50),
                                 assigned_user_email VARCHAR(50),
                                 absence_history_id UUID NOT NULL REFERENCES absence_history(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 service_id UUID REFERENCES service_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT,
                                 cover_user_id UUID REFERENCES user_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT,
                                 assigned_user_id UUID REFERENCES user_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT
);