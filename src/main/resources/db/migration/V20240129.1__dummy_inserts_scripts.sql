INSERT INTO roles (id,date_created ,last_updated, name) VALUES (1,NOW(),NOW(), 'ADMIN');
INSERT INTO roles (id,date_created ,last_updated, name) VALUES (2,NOW(),NOW(), 'USER');

INSERT INTO users (id,date_created ,last_updated, first_name ,last_name , email, password, role_id, status) VALUES (99991,NOW(),NOW(), 'Mustafa', 'Bulut', 'mustafa_admin@gmail.com','$2a$10$ALSGxukF1DlrwRewXfibfO6Q7i.qPa0dM9.bF.bI7vGvl35JvHRaa',1,'ACTIVE');
INSERT INTO users (id,date_created ,last_updated, first_name ,last_name , email, password, role_id, status) VALUES (99992,NOW(),NOW(), 'Mustafa', 'Bulut', 'mustafa_user@gmail.com','$2a$10$ALSGxukF1DlrwRewXfibfO6Q7i.qPa0dM9.bF.bI7vGvl35JvHRaa',2,'ACTIVE');

INSERT INTO products (id,date_created ,last_updated, description,price, name) VALUES (99991,NOW(),NOW(), 'Mobilya', 150, 'Masa');
INSERT INTO products (id,date_created ,last_updated, description,price, name) VALUES (99992,NOW(),NOW(), 'Elektronik', 39, 'USB Adaptör');
INSERT INTO products (id,date_created ,last_updated, description,price, name) VALUES (99993,NOW(),NOW(), 'Kırtasiye', 11, 'Silgi');
INSERT INTO products (id,date_created ,last_updated, description,price, name) VALUES (99994,NOW(),NOW(), 'Kırtasiye', 20, 'Tükenmez Kalem');



