insert into client (nickname,password,username,email,roles,created_by,created_at,updated_at) values ('impati','123','impati','yongs170@naver.com','ADMIN','impati',now(),now());
insert into client (nickname,password,username,email,roles,created_by,created_at,updated_at) values ('wnsduds1','123','wnsduds1','wnsduds1@sju.ac.kr','USER','wnsduds1',now(),now());

insert into services (logo_store_name, service_url,content,created_by,created_at,updated_at) values ('notion-logo.png','https://notion.so/','notion이다 ','impati',now(),now());
insert into services (logo_store_name, service_url,content,created_by,created_at,updated_at) values ('github-logo.png','https://github.com/','깃허브다','impati',now(),now());

insert into category(name,created_by,created_at,updated_at) values('IT','impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('BLOG',1,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('REPO',1,'impati',now(),now());

insert into service_comment(content,service_id,client_id,created_by,created_at,updated_at) values('안녕 노션',1,1,'impati',now(),now());
insert into service_comment(content,service_id,client_id,created_by,created_at,updated_at) values('안녕 깃허브',2,2,'wnsduds1',now(),now());

insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(1,1,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(1,2,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(2,1,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(2,3,'impati',now(),now());

insert into client_service(service_id,client_id,created_by,created_at,updated_at) values(1,1,'impati',now(),now());
insert into client_service(service_id,client_id,created_by,created_at,updated_at) values(1,2,'impati',now(),now());
insert into client_service(service_id,client_id,created_by,created_at,updated_at) values(2,1,'wnsduds1',now(),now());