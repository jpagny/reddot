INSERT INTO messages (content, thread_id, user_id, created_at, updated_at) VALUES ('content_1', 1, 'bfcff13a-51c9-4e95-b362-639984413fb4', STR_TO_DATE('2023-05-01 10:30:00', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2023-05-01 10:30:00', '%Y-%m-%d %H:%i:%s'));
INSERT INTO messages (content, thread_id, user_id, created_at, updated_at) VALUES ('content_2', 1, 'bfcff13a-51c9-4e95-b362-639984413fb4', STR_TO_DATE('2023-05-01 10:30:01', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2023-05-01 10:30:01', '%Y-%m-%d %H:%i:%s'));


INSERT INTO messages (content, thread_id, user_id, created_at, updated_at) VALUES ('content_3', 1, 'bfcff13a-51c9-4e95-b362-639984413fb4', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'));
INSERT INTO messages (content, thread_id, user_id, created_at, updated_at) VALUES ('content_4', 1, 'bfcff13a-51c9-4e95-b362-639984413fb4', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'));
