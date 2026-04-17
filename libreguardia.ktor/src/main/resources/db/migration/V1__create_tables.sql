-- ========================
-- APP SETTINGS TABLE
-- ========================

CREATE TABLE app_settings (
                              id UUID PRIMARY KEY,
                              name VARCHAR(50) NOT NULL UNIQUE,
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
                            name VARCHAR(50) NOT NULL UNIQUE,
                            is_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE zone (
                      id UUID PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      is_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE academic_year (
                               id UUID PRIMARY KEY,
                               name VARCHAR(50) NOT NULL UNIQUE,
                               start_date DATE NOT NULL,
                               end_date DATE NOT NULL
);

CREATE TABLE professional_family (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE,
                                     is_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE building (
                          id UUID PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE,
                          is_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE schedule_activity (
                                   id UUID PRIMARY KEY,
                                   name VARCHAR(50) NOT NULL UNIQUE,
                                   generates_service BOOLEAN NOT NULL,
                                   is_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- ========================
-- DEPENDENT TABLES
-- ========================

CREATE TABLE place (
                       id UUID PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       floor VARCHAR(50) DEFAULT NULL,
                       is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
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
                          password VARCHAR(60) NOT NULL,
                          is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          role VARCHAR(60) NOT NULL
);

CREATE TABLE course (
                        id UUID PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                        professional_family_id UUID NOT NULL REFERENCES professional_family(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE group_tbl (
                           id UUID PRIMARY KEY,
                           name VARCHAR(50) NOT NULL UNIQUE,
                           points_multiplier DECIMAL(2,1) NOT NULL DEFAULT 1,
                           is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                           course_id UUID NOT NULL REFERENCES course(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE schedule (
                          id UUID PRIMARY KEY,
                          week_day VARCHAR(9) NOT NULL,
                          start_time TIME NOT NULL,
                          end_time TIME NOT NULL,
                          group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          schedule_activity_id UUID NOT NULL REFERENCES schedule_activity(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          place_id UUID NOT NULL REFERENCES place(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                          user_id UUID NOT NULL REFERENCES user_tbl(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                          CONSTRAINT uq_schedule UNIQUE (week_day, start_time, end_time, user_id)
);

CREATE TABLE schedule_template_slot (
                                        id UUID PRIMARY KEY,
                                        week_day VARCHAR(9) NOT NULL,
                                        start_time TIME NOT NULL,
                                        end_time TIME NOT NULL,
                                        schedule_template_id UUID NOT NULL REFERENCES schedule_template(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                                        group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        schedule_activity_id UUID REFERENCES schedule_activity(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        place_id UUID REFERENCES place(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT uq_schedule_template_slot UNIQUE (week_day, start_time, end_time, schedule_template_id)
);

CREATE TABLE absence (
                         id UUID PRIMARY KEY,
                         date DATE NOT NULL,
                         start_time TIME NOT NULL,
                         end_time TIME NOT NULL,
                         group_id UUID REFERENCES group_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                         schedule_activity_id UUID NOT NULL REFERENCES schedule_activity(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                         place_id UUID NOT NULL REFERENCES place(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                         user_id UUID REFERENCES user_tbl(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                         CONSTRAINT uq_absence UNIQUE (date, start_time, end_time, user_id)
);

CREATE TABLE service_tbl (
                             id UUID PRIMARY KEY,
                             points_obtained DECIMAL(8,1) NOT NULL,
                             absence_id UUID NOT NULL UNIQUE REFERENCES absence(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                             cover_user_id UUID DEFAULT NULL REFERENCES user_tbl(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                             assigned_user_id UUID REFERENCES user_tbl(id) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE session (
                         id UUID PRIMARY KEY,
                         expires_at TIMESTAMP NOT NULL,
                         user_id UUID NOT NULL REFERENCES user_tbl(id) ON DELETE CASCADE ON UPDATE RESTRICT
);