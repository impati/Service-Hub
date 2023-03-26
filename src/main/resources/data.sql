insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('notion-logo.png', 'https://notion.so/', '노션', '블로그입니다', 'notion이다 ', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('github-logo.png', 'https://github.com/', '깃허브', '코드저장소입니다', '깃허브다', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('naver-logo.png', 'https://naver.com/', '네이버', '검색플랫폼입니다', '네이버다', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('jobplanet-logo.png', 'https://www.jobplanet.co.kr/', '잡플래닛', '채용서비스입니다', '잡플래닛', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('programmers-logo.png', 'https://programmers.co.kr/', '프로그래머스', '채용서비스 및 교육 서비스입니다.', '프로그래머스이다', 'impati',
        now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('youtube-logo.png', 'https://www.youtube.com/', '유튜브', '언터테인서비스입니다.', '유튜브이다.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('jobkorea-logo.png', 'https://www.jobkorea.co.kr/', '잡코리아', '채용서비스입니다.', '잡코리아.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('9e938eaa-090d-49de-ab17-30228c6c2359.png', 'https://papago.naver.com/', '파파고', '무료 번역 서비스.',
        '국내 최고 무료 번역 서비스.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('9bb5d2e6-19f6-4034-9928-885aaae2b5d3.png', 'https://getbootstrap.com/', '부트스트랩', 'css 프레임워크.', 'css 프레임워크.',
        'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('1b711634-70a8-4907-8c25-f3343193b9d5.png', 'https://cloud.google.com/', 'Google Cloud',
        'Cloud Computing Services  |  Google Cloud', '구글 클라우드 컴퓨팅 서비스.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('e93b4c82-b4ad-414b-bd96-069561e9bb77.png', 'https://aws.amazon.com/', 'Amazon Web Services',
        'Cloud Computing Services - Amazon Web Services (AWS)', '아마존 클라우드 컴퓨팅 서비스.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('f2861af9-a09b-47dd-b229-6b8f931c7243.jpeg', 'https://azure.microsoft.com/', 'Microsoft Azure',
        '클라우드 컴퓨팅 서비스 | Microsoft Azure', 'Microsoft 클라우드 컴퓨팅 서비스 Azure.', 'impati', now(), now());
insert into services (logo_store_name, service_url, service_name, title, content, created_by, created_at, updated_at)
values ('6718c6a9-e26a-44c2-9656-ba874010b897.jpg', 'https://www.inflearn.com/', '인프런',
        '인프런 - 미래의 동료들과 함께 성장하는 곳 | IT 정보 플랫폼', 'IT 정보 플랫폼', 'impati', now(), now());


insert into category(name, created_by, created_at, updated_at)
values ('IT', 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('BLOG', 1, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('REPO', 1, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('entertainment', null, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('job', null, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('education', null, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('search-platform', 1, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('translation', null, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('framework', 1, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('cloud', 1, 'impati', now(), now());
insert into category(name, parent_id, created_by, created_at, updated_at)
values ('CUSTOM', null, 'impati', now(), now());


insert into service_comment(content, service_id, client_id, created_by, created_at, updated_at)
values ('안녕 노션', 1, 1, 'impati', now(), now());
insert into service_comment(content, service_id, client_id, created_by, created_at, updated_at)
values ('안녕 깃허브', 2, 2, 'wnsduds1', now(), now());

insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (1, 1, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (1, 2, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (2, 1, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (2, 3, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (3, 7, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (4, 5, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (5, 6, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (6, 4, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (6, 7, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (7, 5, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (8, 8, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (9, 9, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (10, 10, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (11, 10, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (12, 10, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (13, 6, 'impati', now(), now());
insert into service_category(service_id, category_id, created_by, created_at, updated_at)
values (13, 1, 'impati', now(), now());
