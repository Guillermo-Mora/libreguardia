-- Passwords for al users: 123456789

insert into user_tbl VALUES (
                                gen_random_uuid(),
                                'Admin',
                                'Martínez Hernández',
                                'admin@test.com',
                                '000000000',
                                '$2a$10$6wDl41ybagr.cIyTe87OSexxZlqgkhbFU.8yecAoyi4lIcFA6sH02',
                                true,
                                false,
                                'ADMIN'
                            );

insert into user_tbl VALUES (
                                gen_random_uuid(),
                                'User',
                                'Martínez Hernández',
                                'user@test.com',
                                '000000000',
                                '$2a$10$6wDl41ybagr.cIyTe87OSexxZlqgkhbFU.8yecAoyi4lIcFA6sH02',
                                true,
                                false,
                                'USER'
                            );

insert into user_tbl VALUES (
                                gen_random_uuid(),
                                'Visualizer',
                                'Martínez Hernández',
                                'visualizer@test.com',
                                '000000000',
                                '$2a$10$6wDl41ybagr.cIyTe87OSexxZlqgkhbFU.8yecAoyi4lIcFA6sH02',
                                true,
                                false,
                                'VISUALIZER'
                            );

insert into academic_year VALUES (
                                     gen_random_uuid(),
                                     '25-26',
                                     '2025-09-01',
                                     '2026-06-30'
                                 );

insert into professional_family VALUES (
                                           gen_random_uuid(),
                                           'Informática'
                                       );

insert into course VALUES (
                              gen_random_uuid(),
                              '1DAM',
                              (SELECT id FROM professional_family WHERE name = 'Informática')
                          );

insert into group_tbl VALUES (
                                 gen_random_uuid(),
                                 'A',
                                 1,
                                 (SELECT id FROM course WHERE name = '1DAM'),
                                 (SELECT id FROM academic_year WHERE name = '25-26')
                             );

insert into schedule_activity VALUES (
                                         gen_random_uuid(),
                                         'Teach',
                                         true
                                     );

insert into schedule_activity VALUES (
                                         gen_random_uuid(),
                                         'Break',
                                         false
                                     );

insert into schedule_activity VALUES (
                                         gen_random_uuid(),
                                         'Service',
                                         true
                                     );

insert into building VALUES (
                                gen_random_uuid(),
                                'Central'
                            );

insert into zone VALUES (
                            gen_random_uuid(),
                            'A-01'
                        );

insert into place_type VALUES (
                                  gen_random_uuid(),
                                  'Classroom'
                              );

insert into place_type VALUES (
                                  gen_random_uuid(),
                                  'Hallway'
                              );

insert into place_type VALUES (
                                  gen_random_uuid(),
                                  'Schoolyard'
                              );

insert into place VALUES (
                             gen_random_uuid(),
                             'E-01',
                             '2',
                             (SELECT id FROM building WHERE name = 'Central'),
                             (SELECT id FROM zone WHERE name = 'A-01'),
                             (SELECT id FROM place_type WHERE name = 'Classroom')
                         );

insert into place VALUES (
                             gen_random_uuid(),
                             'Main',
                             null,
                             null,
                             (SELECT id FROM zone WHERE name = 'A-01'),
                             (SELECT id FROM place_type WHERE name = 'Schoolyard')
                         );

insert into place VALUES (
                             gen_random_uuid(),
                             'A-12',
                             '1',
                             (SELECT id FROM building WHERE name = 'Central'),
                             (SELECT id FROM zone WHERE name = 'A-01'),
                             (SELECT id FROM place_type WHERE name = 'Hallway')
                         );

-- MONDAY
insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '08:00:00',
                                '08:55:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '08:55:00',
                                '09:50:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '09:50:00',
                                '10:45:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '10:45:00',
                                '11:15:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Break'),
                                (SELECT id FROM place WHERE name = 'Main'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '11:15:00',
                                '12:10:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'MONDAY',
                                '12:10:00',
                                '13:05:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );
-- TUESDAY
insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '08:00:00',
                                '08:55:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '08:55:00',
                                '09:50:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '09:50:00',
                                '10:45:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '10:45:00',
                                '11:15:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Break'),
                                (SELECT id FROM place WHERE name = 'Main'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '11:15:00',
                                '12:10:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'TUESDAY',
                                '12:10:00',
                                '13:05:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );
-- WEDNESDAY
insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '08:00:00',
                                '08:55:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '08:55:00',
                                '09:50:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '09:50:00',
                                '10:45:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '10:45:00',
                                '11:15:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Break'),
                                (SELECT id FROM place WHERE name = 'Main'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '11:15:00',
                                '12:10:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'WEDNESDAY',
                                '12:10:00',
                                '13:05:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );
-- THURSDAY
insert into schedule VALUES (
                                gen_random_uuid(),
                                'THURSDAY',
                                '08:00:00',
                                '08:55:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'THURSDAY',
                                '08:55:00',
                                '09:50:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'THURSDAY',
                                '09:50:00',
                                '10:45:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'THURSDAY',
                                '10:45:00',
                                '11:15:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Break'),
                                (SELECT id FROM place WHERE name = 'Main'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'THURSDAY',
                                '11:15:00',
                                '12:10:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );
-- FRIDAY
insert into schedule VALUES (
                                gen_random_uuid(),
                                'FRIDAY',
                                '08:55:00',
                                '09:50:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'FRIDAY',
                                '09:50:00',
                                '10:45:00',
                                (SELECT id FROM group_tbl WHERE code = 'A'),
                                (SELECT id FROM schedule_activity WHERE name = 'Teach'),
                                (SELECT id FROM place WHERE name = 'E-01'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'FRIDAY',
                                '10:45:00',
                                '11:15:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Break'),
                                (SELECT id FROM place WHERE name = 'Main'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );

insert into schedule VALUES (
                                gen_random_uuid(),
                                'FRIDAY',
                                '11:15:00',
                                '12:10:00',
                                null,
                                (SELECT id FROM schedule_activity WHERE name = 'Service'),
                                (SELECT id FROM place WHERE name = 'A-12'),
                                (SELECT id FROM user_tbl WHERE email = 'admin@test.com')
                            );