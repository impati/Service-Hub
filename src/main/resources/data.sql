insert into client (nickname,password,username,email,roles,created_by,created_at,updated_at) values ('impati','123','impati','yongs170@naver.com','ADMIN','impati',now(),now());
insert into client (nickname,password,username,email,roles,created_by,created_at,updated_at) values ('wnsduds1','123','wnsduds1','wnsduds1@sju.ac.kr','USER','wnsduds1',now(),now());

insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('notion-logo.png','https://notion.so/','노션','블로그입니다','notion이다 ','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('github-logo.png','https://github.com/','깃허브','코드저장소입니다','깃허브다','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('naver-logo.png','https://naver.com/','네이버','검색플랫폼입니다','네이버다','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('jobplanet-logo.png','https://www.jobplanet.co.kr/','잡플래닛','채용서비스입니다','잡플래닛','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('programmers-logo.png','https://programmers.co.kr/','프로그래머스','채용서비스 및 교육 서비스입니다.','프로그래머스이다','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('youtube-logo.png','https://www.youtube.com/','유튜브','언터테인서비스입니다.','유튜브이다.','impati',now(),now());
insert into services (logo_store_name, service_url,service_name,title,content,created_by,created_at,updated_at) values ('jobkorea-logo.png','https://www.jobkorea.co.kr/','잡코리아','채용서비스입니다.','잡코리아.','impati',now(),now());

insert into category(name,created_by,created_at,updated_at) values('IT','impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('BLOG',1,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('REPO',1,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('entertainment',null,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('job',null,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('education',null,'impati',now(),now());
insert into category(name,parent_id,created_by,created_at,updated_at) values('search-platform',1,'impati',now(),now());

insert into service_comment(content,service_id,client_id,created_by,created_at,updated_at) values('안녕 노션',1,1,'impati',now(),now());
insert into service_comment(content,service_id,client_id,created_by,created_at,updated_at) values('안녕 깃허브',2,2,'wnsduds1',now(),now());

insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(1,1,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(1,2,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(2,1,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(2,3,'impati',now(),now());

insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(3,7,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(4,5,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(5,6,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(6,4,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(6,7,'impati',now(),now());
insert into service_category(service_id,category_id,created_by,created_at,updated_at) values(7,5,'impati',now(),now());

insert into client_service(service_id,client_id,click_count,created_by,created_at,updated_at) values(1,1,5,'impati',now(),now());
insert into client_service(service_id,client_id,click_count,created_by,created_at,updated_at) values(2,1,0,'impati',now(),now());
insert into client_service(service_id,client_id,click_count,created_by,created_at,updated_at) values(2,2,2,'wnsduds1',now(),now());