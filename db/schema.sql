-- 초기 테이블 생성

-- TopCategory 엔터티 생성
CREATE TABLE IF NOT EXISTS TopCategory(
	top_category_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	top_category_name VARCHAR(20) UNIQUE -- 상위 카테고리명
);

-- SubCategory 엔터티 생성
CREATE TABLE IF NOT EXISTS SubCategory(
	sub_category_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	sub_category_name VARCHAR(20) UNIQUE, -- 하위 카테고리명
	top_category_id INT, -- 외래키
	CONSTRAINT fk_SubCategory_TopCategory FOREIGN KEY(top_category_id)
	REFERENCES TopCategory(top_category_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- Brand 엔터티 생성
CREATE TABLE IF NOT EXISTS Brand(
	brand_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	brand_name VARCHAR(50), -- 브랜드명
	sub_category_id INT, -- 외래키
	CONSTRAINT fk_Brand_SubCategory FOREIGN KEY(sub_category_id)
	REFERENCES SubCategory(sub_category_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- Location 엔터티 생성
CREATE TABLE IF NOT EXISTS Location(
	location_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	location_name VARCHAR(20) UNIQUE, -- 위치명
	brand_id INT, -- 외래키
	CONSTRAINT fk_Location_Brand FOREIGN KEY(brand_id)
	REFERENCES Brand(brand_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- Product 엔터티 생성
CREATE TABLE IF NOT EXISTS Product(
	product_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	product_name VARCHAR(60), -- 상품명
	product_price INT, -- 상품가격
	product_description TEXT, -- 상품설명
	location_id INT, -- 외래키
	CONSTRAINT fk_Product_Location FOREIGN KEY(location_id)
	REFERENCES Location(location_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- ProductImage 엔터티 생성
CREATE TABLE IF NOT EXISTS ProductImage(
	product_image_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	image_url TEXT, -- 이미지 경로
	product_id INT, -- 외래키
	CONSTRAINT fk_ProductImage_Product FOREIGN KEY(product_id)
	REFERENCES Product(product_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- ProductDetail 엔터티 생성
CREATE TABLE IF NOT EXISTS ProductDetail(
	product_detail_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	product_size_name VARCHAR(20), -- 사이즈명
	product_quantity INT, -- 상품수량
	product_id INT, -- 외래키
	CONSTRAINT fk_ProductDetail_Product FOREIGN KEY(product_id)
	REFERENCES Product(product_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- InventoryLog 엔터티 생성
CREATE TABLE IF NOT EXISTS InventoryLog(
	inventory_log_id INT PRIMARY KEY AUTO_INCREMENT, -- 기본키
	change_type ENUM('IN', 'OUT'), -- 입출고 구분
	quantity INT, -- 사이즈별 상품수량
	changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 입출고날짜
	product_detail_id INT, -- 외래키
	CONSTRAINT fk_InventoryLog_ProductDetail FOREIGN KEY(product_detail_id)
	REFERENCES ProductDetail(product_detail_id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

-- 상위카테고리 INSERT
INSERT INTO TopCategory(top_category_name) VALUES('상의');
INSERT INTO TopCategory(top_category_name) VALUES('하의');
INSERT INTO TopCategory(top_category_name) VALUES('아우터');
INSERT INTO TopCategory(top_category_name) VALUES('신발');
INSERT INTO TopCategory(top_category_name) VALUES('가방');
INSERT INTO TopCategory(top_category_name) VALUES('액세서리');
INSERT INTO TopCategory(top_category_name) VALUES('모자');
INSERT INTO TopCategory(top_category_name) VALUES('이너웨어');
INSERT INTO TopCategory(top_category_name) VALUES('원피스');
INSERT INTO TopCategory(top_category_name) VALUES('운동복');

-- 하위카테고리 INSERT
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('티셔츠',1);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('셔츠',1);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('니트',1);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('청바지',2);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('슬랙스',2);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('스웻팬츠',2);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('패딩',3);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('바람막이',3);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('청자켓',3);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('운동화',4);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('더피슈즈',4);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('부츠',4);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('백팩',5);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('토트백',5);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('숄더백',5);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('목걸이',6);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('팔찌',6);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('반지',6);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('볼캡',7);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('비니',7);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('스냅백',7);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('팬티',8);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('런닝',8);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('양말',8);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('미니원피스',9);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('점프수트',9);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('맥시원피스',9);

INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('레깅스',10);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('트레이닝복 하의',10);
INSERT INTO SubCategory(sub_category_name, top_category_id) VALUES('트레이닝복 상의',10);

-- Brand INSERT
INSERT INTO Brand(brand_name, sub_category_id) VALUES('스투시',1);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('폴로',2);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('스투시',3);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('리바이스',4);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('무신사스탠다드',5);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('무신사스탠다드',6);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('노스페이스',7);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('아크테릭스',8);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('스투시',9);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('뉴발란스',10);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('닥터마틴',11);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('그라더스',12);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('노스페이스',13);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('프라이탁',14);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('르메르',15);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('티파니',16);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('티파니',17);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('크롬하츠',18);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('슈프림',19);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('아크테릭스',20);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('엠엘비',21);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('캘빈클라인',22);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('유니클로',23);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('유니클로',24);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('버버리',25);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('르메르',26);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('르메르',27);

INSERT INTO Brand(brand_name, sub_category_id) VALUES('나이키',28);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('아디다스',29);
INSERT INTO Brand(brand_name, sub_category_id) VALUES('아디다스',30);

-- Location INSERT
INSERT INTO Location(location_name, brand_id)
SELECT
	CONCAT(sc.top_category_id, b.sub_category_id) AS location_name,
	b.brand_id
FROM Brand b
JOIN SubCategory sc ON b.sub_category_id = sc.sub_category_id;

-- Product INSERT
-- 상의 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('스투시 티셔츠', 39000, '스투시의 기본 반팔 티셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '11' LIMIT 1)),

('폴로 옥스포드 셔츠', 79000, '클래식한 폴로 셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '12' LIMIT 1)),

('스투시 니트', 65000, '겨울용 두꺼운 니트웨어입니다.',
 (SELECT location_id FROM Location WHERE location_name = '13' LIMIT 1)),
 
('스투시 그래픽 티셔츠', 45000, '프린팅이 들어간 스투시 티셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '11' LIMIT 1)),

('폴로 스트라이프 셔츠', 85000, '스트라이프 패턴의 셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '12' LIMIT 1)),

('스투시 반집업 니트', 99000, '반집업 스타일의 스투시 니트입니다.',
 (SELECT location_id FROM Location WHERE location_name = '13' LIMIT 1)),
 
('스투시 그래픽 후드', 59000, '편안한 그래픽 후드티입니다.',
 (SELECT location_id FROM Location WHERE location_name = '11' LIMIT 1)),
 
('폴로 클래식 티셔츠', 45000, '폴로 브랜드 기본 반팔 티셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '12' LIMIT 1));
 
 
-- 하의 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('리바이스 청바지', 89000, '리바이스의 정통 데님 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),

('무신사 슬랙스', 45000, '편안한 무신사 슬랙스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '25' LIMIT 1)),

('무신사 스웻팬츠', 43000, '데일리용 조거 스웻팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1)),
 
('리바이스 와이드진', 98000, '넉넉한 핏의 와이드 진입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),

('무신사스탠다드 슬랙스 블랙', 49000, '기본 블랙 슬랙스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '25' LIMIT 1)),

('무신사스탠다드 스웻 조거팬츠', 43000, '신축성 좋은 조거 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1)),

('리바이스 스트레이트진', 97000, '기본 스트레이트 핏의 청바지입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),
 
('무신사 스탠다드 데님', 49000, '베이직 데님 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),
 
('스투시 카고 팬츠', 53000, '트렌디한 카고 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1)),
 
('폴로 치노 팬츠', 55000, '폴로 브랜드 치노 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '25' LIMIT 1)),
 
('리바이스 슬림 팬츠', 60000, '리바이스 슬림 핏 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),
 
('무신사 스탠다드 조거팬츠', 47000, '편안한 조거팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1)),
 
('스투시 트레이닝 팬츠', 42000, '캐주얼 트레이닝 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1));
 
-- 아우터 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('노스페이스 롱패딩', 179000, '노스페이스 구스다운 패딩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '37' LIMIT 1)),

('아크테릭스 윈드브레이커', 199000, '아크테릭스 방풍 자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '38' LIMIT 1)),

('스투시 청자켓', 98000, '빈티지 느낌의 청자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '39' LIMIT 1)),
 
('노스페이스 푸퍼 자켓', 199000, '노스페이스 푸퍼 스타일 자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '37' LIMIT 1)),
 
('아크테릭스 소프트쉘', 210000, '방풍 소프트쉘 자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '38' LIMIT 1)),
 
('스투시 윈드브레이커', 98000, '경량 윈드브레이커입니다.',
 (SELECT location_id FROM Location WHERE location_name = '39' LIMIT 1));
 
-- 신발 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('뉴발란스 운동화', 129000, '990v6 운동화입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),

('닥터마틴 더비슈즈', 189000, '클래식한 블랙 더비 슈즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '411' LIMIT 1)),

('그라더스 부츠', 159000, '가죽 첼시 부츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '412' LIMIT 1)),
 
('나이키 에어맥스', 130000, '나이키 인기 에어맥스 운동화입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),
 
('아디다스 울트라부스트', 150000, '아디다스 울트라부스트 운동화입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),
 
('뉴발란스 574', 90000, '클래식 뉴발란스 574 모델입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),
 
('닥터마틴 첼시 부츠', 200000, '클래식 첼시 부츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '412' LIMIT 1)),
 
('그라더스 더비슈즈', 180000, '더비 스타일 가죽 신발입니다.',
 (SELECT location_id FROM Location WHERE location_name = '411' LIMIT 1)),
 
('아디다스 슬립온', 85000, '편안한 슬립온 운동화입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),
 
('나이키 조던', 160000, '나이키 조던 시리즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1));
 
-- 가방 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('노스페이스 백팩', 79000, '여행용 백팩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '513' LIMIT 1)),

('프라이탁 토트백', 149000, '재활용 방수천 토트백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '514' LIMIT 1)),

('르메르 숄더백', 189000, '미니멀한 디자인의 숄더백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '515' LIMIT 1)),
 
('노스페이스 메신저백', 85000, '실용적인 메신저백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '513' LIMIT 1)),
 
('프라이탁 백팩 미니', 120000, '컴팩트 백팩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '513' LIMIT 1)),
 
('르메르 크로스백', 95000, '미니 크로스백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '515' LIMIT 1)),
 
('무신사 숄더백', 65000, '캐주얼 숄더백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '515' LIMIT 1));

-- 액세서리 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('티파니 목걸이', 490000, '하트 펜던트 목걸이입니다.',
 (SELECT location_id FROM Location WHERE location_name = '616' LIMIT 1)),

('티파니 팔찌', 390000, '클래식 체인 팔찌입니다.',
 (SELECT location_id FROM Location WHERE location_name = '617' LIMIT 1)),

('크롬하츠 반지', 990000, '실버 팬던트 반지입니다.',
 (SELECT location_id FROM Location WHERE location_name = '618' LIMIT 1)),
 
('티파니 반지', 450000, '티파니 실버 반지입니다.',
 (SELECT location_id FROM Location WHERE location_name = '618' LIMIT 1)),
 
('크롬하츠 팔찌', 580000, '은팔찌 디자인입니다.',
 (SELECT location_id FROM Location WHERE location_name = '617' LIMIT 1));

-- 모자 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('슈프림 볼캡', 79000, '로고 볼캡입니다.',
 (SELECT location_id FROM Location WHERE location_name = '719' LIMIT 1)),

('아크테릭스 비니', 59000, '겨울용 니트 비니입니다.',
 (SELECT location_id FROM Location WHERE location_name = '720' LIMIT 1)),

('엠엘비 스냅백', 69000, '뉴욕 양키즈 로고가 들어간 스냅백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '721' LIMIT 1)),
 
('슈프림 캠프캡', 89000, '캠프 스타일의 슈프림 볼캡입니다.',
 (SELECT location_id FROM Location WHERE location_name = '719' LIMIT 1)),

('엠엘비 비니', 39000, '겨울용 MLB 비니입니다.',
 (SELECT location_id FROM Location WHERE location_name = '720' LIMIT 1)),
 
('슈프림 버킷햇', 72000, '슈프림 버킷햇입니다.',
 (SELECT location_id FROM Location WHERE location_name = '719' LIMIT 1)),
 
('엠엘비 스냅백 블랙', 69000, '블랙 스냅백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '721' LIMIT 1)),
 
('아디다스 비니', 43000, '겨울용 비니입니다.',
 (SELECT location_id FROM Location WHERE location_name = '720' LIMIT 1));
 
-- 이너웨어 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('캘빈클라인 팬티', 29000, '3팩 남성 드로즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '822' LIMIT 1)),

('유니클로 런닝', 19900, '에어리즘 런닝웨어입니다.',
 (SELECT location_id FROM Location WHERE location_name = '823' LIMIT 1)),

('유니클로 양말', 9900, '기본 크루삭스 3팩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '824' LIMIT 1)),
 
('유니클로 드로즈', 15000, '편안한 남성 드로즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '822' LIMIT 1));

-- 원피스 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('버버리 미니원피스', 359000, '버버리 체크 미니 드레스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '925' LIMIT 1)),

('르메르 점프수트', 279000, '모노톤 여성용 점프수트입니다.',
 (SELECT location_id FROM Location WHERE location_name = '926' LIMIT 1)),

('르메르 맥시원피스', 299000, '롱 플로럴 드레스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '927' LIMIT 1));

-- 운동복 INSERT
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('나이키 레깅스', 39000, '운동용 하이웨이스트 레깅스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1028' LIMIT 1)),

('아디다스 트레이닝 하의', 59000, '클래식 삼선 트레이닝 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1029' LIMIT 1)),

('아디다스 트레이닝 상의', 69000, '지퍼 후디 스타일입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1030' LIMIT 1)),
 
('나이키 트레이닝 자켓', 99000, '가벼운 트레이닝 자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1028' LIMIT 1)),
 
('아디다스 후디', 85000, '클래식 후디입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1030' LIMIT 1)),
 
('나이키 하프팬츠', 42000, '여름용 통기성 좋은 하프팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1028' LIMIT 1)),

('아디다스 조거팬츠', 53000, '클래식 삼선 조거 스타일입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1029' LIMIT 1)),

('아디다스 스포츠 브라', 39000, '운동 시 편안한 스포츠 브라입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1030' LIMIT 1)),
 
('아디다스 트레이닝 티셔츠', 39000, '트레이닝용 티셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1030' LIMIT 1)),
 
('나이키 러닝 타이즈', 40000, '러닝용 타이즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1028' LIMIT 1));
 
-- ProductDetail INSERT
INSERT INTO ProductDetail(product_size_name, product_quantity, product_id)
VALUES 
('S', 4, 1),
('M', 8, 2),
('L', 9, 3),
('S', 5, 4),
('M', 9, 5),
('L', 10, 6),
('S', 3, 7),
('M', 6, 8),
('L', 4, 9),
('S', 3, 10),
('M', 5, 11),
('L', 2, 12),
('S', 6, 13),
('M', 7, 14),
('L', 5, 15),
('S', 8, 16),
('M', 2, 17),
('L', 5, 18),
('S', 3, 19),
('M', 4, 20),
('L', 5, 21),
('S', 10, 22),
('M', 5, 23),
('L', 4, 24),
('S', 9, 25),
('M', 7, 26),
('L', 13, 27),
('250', 6, 28),
('280', 5, 29),
('265', 4, 30),
('245', 12, 31),
('270', 6, 32),
('220', 8, 33),
('230', 3, 34),
('255', 5, 35),
('285', 5, 36),
('240', 2, 37),
('FREE', 10, 38),
('FREE', 4, 39),
('FREE', 6, 40),
('FREE', 7, 41),
('FREE', 11, 42),
('FREE', 8, 43),
('FREE', 3, 44),
('FREE', 15, 45),
('FREE', 4, 46),
('FREE', 13, 47),
('FREE', 2, 48),
('FREE', 6, 49),
('FREE', 7, 50),
('FREE', 5, 51),
('FREE', 6, 52),
('FREE', 2, 53),
('FREE', 6, 54),
('FREE', 5, 55),
('FREE', 14, 56),
('FREE', 4, 57),
('L', 6, 58),
('M', 14, 59),
('FREE', 28, 60),
('M', 8, 61),
('S', 12, 62),
('M', 20, 63),
('L', 18, 64),
('M', 6, 65),
('S', 6, 66),
('S', 10, 67),
('M', 5, 68),
('L', 8, 69),
('M', 7, 70),
('S', 4, 71),
('M', 4, 72),
('L', 5, 73),
('S', 2, 74);

-- ProductImage INSERT
INSERT INTO ProductImage(image_url, product_id)
VALUES
('/images/product1.png', 1),
('/images/product2.png', 2),
('/images/product3.png', 3),
('/images/product4.png', 4),
('/images/product5.png', 5),
('/images/product6.png', 6),
('/images/product7.png', 7),
('/images/product8.png', 8),
('/images/product9.png', 9),
('/images/product10.png', 10),
('/images/product11.png', 11),
('/images/product12.png', 12),
('/images/product13.png', 13),
('/images/product14.png', 14),
('/images/product15.png', 15),
('/images/product16.png', 16),
('/images/product17.png', 17),
('/images/product18.png', 18),
('/images/product19.png', 19),
('/images/product20.png', 20),
('/images/product21.png', 21),
('/images/product22.png', 22),
('/images/product23.png', 23),
('/images/product24.png', 24),
('/images/product25.png', 25),
('/images/product26.png', 26),
('/images/product27.png', 27),
('/images/product28.png', 28),
('/images/product29.png', 29),
('/images/product30.png', 30),
('/images/product31.png', 31),
('/images/product32.png', 32),
('/images/product33.png', 33),
('/images/product34.png', 34),
('/images/product35.png', 35),
('/images/product36.png', 36),
('/images/product37.png', 37),
('/images/product38.png', 38),
('/images/product39.png', 39),
('/images/product40.png', 40),
('/images/product41.png', 41),
('/images/product42.png', 42),
('/images/product43.png', 43),
('/images/product44.png', 44),
('/images/product45.png', 45),
('/images/product46.png', 46),
('/images/product47.png', 47),
('/images/product48.png', 48),
('/images/product49.png', 49),
('/images/product50.png', 50),
('/images/product51.png', 51),
('/images/product52.png', 52),
('/images/product53.png', 53),
('/images/product54.png', 54),
('/images/product55.png', 55),
('/images/product56.png', 56),
('/images/product57.png', 57),
('/images/product58.png', 58),
('/images/product59.png', 59),
('/images/product60.png', 60),
('/images/product61.png', 61),
('/images/product62.png', 62),
('/images/product63.png', 63),
('/images/product64.png', 64),
('/images/product65.png', 65),
('/images/product66.png', 66),
('/images/product67.png', 67),
('/images/product68.png', 68),
('/images/product69.png', 69),
('/images/product70.png', 70),
('/images/product71.png', 71),
('/images/product72.png', 72),
('/images/product73.png', 73),
('/images/product74.png', 74);

-- InventoryLog INSERT
INSERT INTO InventoryLog(change_type, quantity, product_detail_id)
VALUES
('IN', 10, 1),
('IN', 10, 2),
('IN', 10, 3),
('IN', 10, 4),
('IN', 10, 5),
('IN', 10, 6),
('IN', 10, 7),
('IN', 10, 8),
('IN', 10, 9),
('IN', 10, 10),
('IN', 10, 11),
('IN', 10, 12),
('IN', 10, 13),
('IN', 10, 14),
('IN', 10, 15),
('IN', 10, 16),
('IN', 10, 17),
('IN', 10, 18),
('IN', 10, 19),
('IN', 10, 20),
('IN', 10, 21),
('IN', 10, 22),
('IN', 10, 23),
('IN', 10, 24),
('IN', 10, 25),
('IN', 10, 26),
('IN', 10, 27),
('IN', 10, 28),
('IN', 10, 29),
('IN', 10, 30),
('IN', 10, 31),
('IN', 10, 32),
('IN', 10, 33),
('IN', 10, 34),
('IN', 10, 35),
('IN', 10, 36),
('IN', 10, 37),
('IN', 10, 38),
('IN', 10, 39),
('IN', 10, 40),
('IN', 10, 41),
('IN', 10, 42),
('IN', 10, 43),
('IN', 10, 44),
('IN', 10, 45),
('IN', 10, 46),
('IN', 10, 47),
('IN', 10, 48),
('IN', 10, 49),
('IN', 10, 50),
('IN', 10, 51),
('IN', 10, 52),
('IN', 10, 53),
('IN', 10, 54),
('IN', 10, 55),
('IN', 10, 56),
('IN', 10, 57),
('IN', 10, 58),
('IN', 10, 59),
('IN', 10, 60),
('IN', 10, 61),
('IN', 10, 62),
('IN', 10, 63),
('IN', 10, 64),
('IN', 10, 65),
('IN', 10, 66),
('IN', 10, 67),
('IN', 10, 68),
('IN', 10, 69),
('IN', 10, 70),
('IN', 10, 71),
('IN', 10, 72),
('IN', 10, 73),
('IN', 10, 74);

UPDATE inventorylog
SET changed_at = CASE product_detail_id
    WHEN 1 THEN '2025-06-19 03:25:11'
    WHEN 2 THEN '2025-06-22 14:11:33'
    WHEN 3 THEN '2025-06-24 20:57:02'
    WHEN 4 THEN '2025-06-23 09:42:18'
    WHEN 5 THEN '2025-06-19 21:16:44'
    WHEN 6 THEN '2025-06-20 06:37:50'
    WHEN 7 THEN '2025-06-21 18:48:29'
    WHEN 8 THEN '2025-06-25 10:05:17'
    WHEN 9 THEN '2025-06-24 23:12:40'
    WHEN 10 THEN '2025-06-20 04:22:36'
    WHEN 11 THEN '2025-06-22 07:41:55'
    WHEN 12 THEN '2025-06-23 15:53:10'
    WHEN 13 THEN '2025-06-21 11:30:26'
    WHEN 14 THEN '2025-06-24 05:58:01'
    WHEN 15 THEN '2025-06-22 16:19:38'
    WHEN 16 THEN '2025-06-20 19:25:07'
    WHEN 17 THEN '2025-06-21 09:50:44'
    WHEN 18 THEN '2025-06-23 22:48:19'
    WHEN 19 THEN '2025-06-25 13:02:54'
    WHEN 20 THEN '2025-06-19 18:31:33'
    WHEN 21 THEN '2025-06-20 14:10:23'
    WHEN 22 THEN '2025-06-22 10:43:05'
    WHEN 23 THEN '2025-06-24 08:29:50'
    WHEN 24 THEN '2025-06-19 21:45:12'
    WHEN 25 THEN '2025-06-23 05:57:38'
    WHEN 26 THEN '2025-06-20 07:49:29'
    WHEN 27 THEN '2025-06-22 16:58:14'
    WHEN 28 THEN '2025-06-24 12:01:08'
    WHEN 29 THEN '2025-06-21 23:32:37'
    WHEN 30 THEN '2025-06-19 03:14:46'
    WHEN 31 THEN '2025-06-20 20:44:21'
    WHEN 32 THEN '2025-06-22 14:35:53'
    WHEN 33 THEN '2025-06-25 19:50:41'
    WHEN 34 THEN '2025-06-23 09:23:10'
    WHEN 35 THEN '2025-06-20 17:41:59'
    WHEN 36 THEN '2025-06-21 05:34:26'
    WHEN 37 THEN '2025-06-24 14:27:55'
    WHEN 38 THEN '2025-06-19 11:15:03'
    WHEN 39 THEN '2025-06-20 10:39:16'
    WHEN 40 THEN '2025-06-22 20:54:43'
    WHEN 41 THEN '2025-06-25 01:07:21'
    WHEN 42 THEN '2025-06-23 16:52:34'
    WHEN 43 THEN '2025-06-20 08:18:40'
    WHEN 44 THEN '2025-06-21 19:46:03'
    WHEN 45 THEN '2025-06-24 22:11:08'
    WHEN 46 THEN '2025-06-19 06:38:51'
    WHEN 47 THEN '2025-06-20 12:55:27'
    WHEN 48 THEN '2025-06-22 14:07:39'
    WHEN 49 THEN '2025-06-25 17:02:53'
    WHEN 50 THEN '2025-06-23 09:14:17'
    WHEN 51 THEN '2025-06-20 19:58:25'
    WHEN 52 THEN '2025-06-21 11:36:54'
    WHEN 53 THEN '2025-06-24 03:41:30'
    WHEN 54 THEN '2025-06-19 08:50:02'
    WHEN 55 THEN '2025-06-20 15:13:12'
    WHEN 56 THEN '2025-06-22 10:39:57'
    WHEN 57 THEN '2025-06-25 21:24:18'
    WHEN 58 THEN '2025-06-23 06:51:42'
    WHEN 59 THEN '2025-06-20 09:28:36'
    WHEN 60 THEN '2025-06-21 22:13:54'
    WHEN 61 THEN '2025-06-24 14:42:16'
    WHEN 62 THEN '2025-06-19 17:11:50'
    WHEN 63 THEN '2025-06-20 20:27:33'
    WHEN 64 THEN '2025-06-22 12:15:24'
    WHEN 65 THEN '2025-06-25 08:41:49'
    WHEN 66 THEN '2025-06-23 16:33:21'
    WHEN 67 THEN '2025-06-20 10:58:12'
    WHEN 68 THEN '2025-06-21 23:41:07'
    WHEN 69 THEN '2025-06-24 19:18:39'
    WHEN 70 THEN '2025-06-19 05:44:20'
    WHEN 71 THEN '2025-06-20 14:07:55'
    WHEN 72 THEN '2025-06-22 18:36:29'
    WHEN 73 THEN '2025-06-25 02:23:47'
    WHEN 74 THEN '2025-06-23 10:19:58'
END
WHERE change_type = 'IN' AND
product_detail_id IN (
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
    51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
    61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
    71, 72, 73, 74
);

INSERT INTO InventoryLog(change_type, quantity, product_detail_id)
VALUES
('OUT', 3, 1),
('OUT', 8, 2),
('OUT', 5, 3),
('OUT', 9, 4),
('OUT', 1, 5),
('OUT', 10, 6),
('OUT', 7, 7),
('OUT', 2, 8),
('OUT', 6, 9),
('OUT', 4, 10),
('OUT', 9, 11),
('OUT', 5, 12),
('OUT', 3, 13),
('OUT', 7, 14),
('OUT', 2, 15),
('OUT', 1, 16),
('OUT', 10, 17),
('OUT', 6, 18),
('OUT', 4, 19),
('OUT', 8, 20),
('OUT', 2, 21),
('OUT', 6, 22),
('OUT', 3, 23),
('OUT', 10, 24),
('OUT', 9, 25),
('OUT', 5, 26),
('OUT', 4, 27),
('OUT', 1, 28),
('OUT', 7, 29),
('OUT', 6, 30),
('OUT', 2, 31),
('OUT', 8, 32),
('OUT', 3, 33),
('OUT', 7, 34),
('OUT', 9, 35),
('OUT', 5, 36),
('OUT', 4, 37),
('OUT', 1, 38),
('OUT', 10, 39),
('OUT', 6, 40),
('OUT', 2, 41),
('OUT', 8, 42),
('OUT', 3, 43),
('OUT', 5, 44),
('OUT', 7, 45),
('OUT', 1, 46),
('OUT', 4, 47),
('OUT', 9, 48),
('OUT', 10, 49),
('OUT', 6, 50),
('OUT', 2, 51),
('OUT', 8, 52),
('OUT', 3, 53),
('OUT', 5, 54),
('OUT', 7, 55),
('OUT', 1, 56),
('OUT', 4, 57),
('OUT', 9, 58),
('OUT', 10, 59),
('OUT', 6, 60),
('OUT', 2, 61),
('OUT', 8, 62),
('OUT', 3, 63),
('OUT', 5, 64),
('OUT', 7, 65),
('OUT', 1, 66),
('OUT', 4, 67),
('OUT', 9, 68),
('OUT', 10, 69),
('OUT', 6, 70),
('OUT', 2, 71),
('OUT', 8, 72),
('OUT', 3, 73),
('OUT', 5, 74);

UPDATE inventorylog
SET changed_at = CASE product_detail_id
    WHEN 1 THEN '2025-06-23 18:13:26'
    WHEN 2 THEN '2025-06-25 21:12:50'
    WHEN 3 THEN '2025-06-25 04:56:49'
    WHEN 4 THEN '2025-06-26 07:26:19'
    WHEN 5 THEN '2025-06-21 09:02:33'
    WHEN 6 THEN '2025-06-20 21:37:35'
    WHEN 7 THEN '2025-06-24 10:24:15'
    WHEN 8 THEN '2025-06-25 17:13:19'
    WHEN 9 THEN '2025-06-25 18:23:26'
    WHEN 10 THEN '2025-06-22 10:46:32'
    WHEN 11 THEN '2025-06-24 04:54:33'
    WHEN 12 THEN '2025-06-24 18:00:26'
    WHEN 13 THEN '2025-06-26 07:53:25'
    WHEN 14 THEN '2025-06-24 15:16:07'
    WHEN 15 THEN '2025-06-25 07:31:35'
    WHEN 16 THEN '2025-06-23 13:03:09'
    WHEN 17 THEN '2025-06-23 10:59:58'
    WHEN 18 THEN '2025-06-26 01:39:30'
    WHEN 19 THEN '2025-06-26 01:42:23'
    WHEN 20 THEN '2025-06-22 07:30:20'
    WHEN 21 THEN '2025-06-20 23:51:39'
    WHEN 22 THEN '2025-06-24 02:44:41'
    WHEN 23 THEN '2025-06-25 11:42:14'
    WHEN 24 THEN '2025-06-22 09:41:32'
    WHEN 25 THEN '2025-06-24 16:23:17'
    WHEN 26 THEN '2025-06-21 10:38:45'
    WHEN 27 THEN '2025-06-25 08:49:01'
    WHEN 28 THEN '2025-06-25 18:54:37'
    WHEN 29 THEN '2025-06-22 00:44:12'
    WHEN 30 THEN '2025-06-20 14:24:27'
    WHEN 31 THEN '2025-06-23 19:43:51'
    WHEN 32 THEN '2025-06-23 06:01:05'
    WHEN 33 THEN '2025-06-26 07:13:45'
    WHEN 34 THEN '2025-06-25 02:42:16'
    WHEN 35 THEN '2025-06-21 14:32:19'
    WHEN 36 THEN '2025-06-22 18:59:32'
    WHEN 37 THEN '2025-06-25 20:12:07'
    WHEN 38 THEN '2025-06-21 00:21:06'
    WHEN 39 THEN '2025-06-21 01:53:20'
    WHEN 40 THEN '2025-06-25 00:44:03'
    WHEN 41 THEN '2025-06-26 01:04:10'
    WHEN 42 THEN '2025-06-25 22:12:41'
    WHEN 43 THEN '2025-06-20 12:01:25'
    WHEN 44 THEN '2025-06-23 23:18:49'
    WHEN 45 THEN '2025-06-25 02:49:01'
    WHEN 46 THEN '2025-06-21 05:51:22'
    WHEN 47 THEN '2025-06-23 00:27:39'
    WHEN 48 THEN '2025-06-24 19:58:42'
    WHEN 49 THEN '2025-06-26 06:43:37'
    WHEN 50 THEN '2025-06-24 11:52:04'
    WHEN 51 THEN '2025-06-23 01:34:47'
    WHEN 52 THEN '2025-06-22 00:05:52'
    WHEN 53 THEN '2025-06-25 07:28:21'
    WHEN 54 THEN '2025-06-20 21:17:12'
    WHEN 55 THEN '2025-06-22 04:59:58'
    WHEN 56 THEN '2025-06-24 23:26:44'
    WHEN 57 THEN '2025-06-26 07:58:03'
    WHEN 58 THEN '2025-06-24 16:48:29'
    WHEN 59 THEN '2025-06-21 23:07:28'
    WHEN 60 THEN '2025-06-22 06:38:10'
    WHEN 61 THEN '2025-06-25 01:45:27'
    WHEN 62 THEN '2025-06-21 23:40:45'
    WHEN 63 THEN '2025-06-22 22:43:55'
    WHEN 64 THEN '2025-06-24 01:19:40'
    WHEN 65 THEN '2025-06-26 09:03:29'
    WHEN 66 THEN '2025-06-24 11:12:26'
    WHEN 67 THEN '2025-06-21 04:15:12'
    WHEN 68 THEN '2025-06-23 01:14:18'
    WHEN 69 THEN '2025-06-25 23:34:01'
    WHEN 70 THEN '2025-06-20 08:21:12'
    WHEN 71 THEN '2025-06-22 01:31:36'
    WHEN 72 THEN '2025-06-24 22:01:19'
    WHEN 73 THEN '2025-06-26 01:37:48'
    WHEN 74 THEN '2025-06-25 18:49:02'
END
WHERE change_type = 'OUT'
  AND product_detail_id IN (
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
    51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
    61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
    71, 72, 73, 74
);