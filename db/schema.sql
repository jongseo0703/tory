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
INSERT INTO Product(product_name, product_price, product_description, location_id)
VALUES
('스투시 티셔츠', 39000, '스투시의 기본 반팔 티셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '11' LIMIT 1)),

('폴로 옥스포드 셔츠', 79000, '클래식한 폴로 셔츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '12' LIMIT 1)),

('스투시 니트', 65000, '겨울용 두꺼운 니트웨어입니다.',
 (SELECT location_id FROM Location WHERE location_name = '13' LIMIT 1)),

('리바이스 청바지', 89000, '리바이스의 정통 데님 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '24' LIMIT 1)),

('무신사 슬랙스', 45000, '편안한 무신사 슬랙스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '25' LIMIT 1)),

('무신사 스웻팬츠', 43000, '데일리용 조거 스웻팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '26' LIMIT 1)),

('노스페이스 롱패딩', 179000, '노스페이스 구스다운 패딩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '37' LIMIT 1)),

('아크테릭스 윈드브레이커', 199000, '아크테릭스 방풍 자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '38' LIMIT 1)),

('스투시 청자켓', 98000, '빈티지 느낌의 청자켓입니다.',
 (SELECT location_id FROM Location WHERE location_name = '39' LIMIT 1)),

('뉴발란스 운동화', 129000, '990v6 운동화입니다.',
 (SELECT location_id FROM Location WHERE location_name = '410' LIMIT 1)),

('닥터마틴 더비슈즈', 189000, '클래식한 블랙 더비 슈즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '411' LIMIT 1)),

('그라더스 부츠', 159000, '가죽 첼시 부츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '412' LIMIT 1)),

('노스페이스 백팩', 79000, '여행용 백팩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '513' LIMIT 1)),

('프라이탁 토트백', 149000, '재활용 방수천 토트백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '514' LIMIT 1)),

('르메르 숄더백', 189000, '미니멀한 디자인의 숄더백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '515' LIMIT 1)),

('티파니 목걸이', 490000, '하트 펜던트 목걸이입니다.',
 (SELECT location_id FROM Location WHERE location_name = '616' LIMIT 1)),

('티파니 팔찌', 390000, '클래식 체인 팔찌입니다.',
 (SELECT location_id FROM Location WHERE location_name = '617' LIMIT 1)),

('크롬하츠 반지', 990000, '실버 팬던트 반지입니다.',
 (SELECT location_id FROM Location WHERE location_name = '618' LIMIT 1)),

('슈프림 볼캡', 79000, '로고 볼캡입니다.',
 (SELECT location_id FROM Location WHERE location_name = '719' LIMIT 1)),

('아크테릭스 비니', 59000, '겨울용 니트 비니입니다.',
 (SELECT location_id FROM Location WHERE location_name = '720' LIMIT 1)),

('엠엘비 스냅백', 69000, '뉴욕 양키즈 로고가 들어간 스냅백입니다.',
 (SELECT location_id FROM Location WHERE location_name = '721' LIMIT 1)),

('캘빈클라인 팬티', 29000, '3팩 남성 드로즈입니다.',
 (SELECT location_id FROM Location WHERE location_name = '822' LIMIT 1)),

('유니클로 런닝', 19900, '에어리즘 런닝웨어입니다.',
 (SELECT location_id FROM Location WHERE location_name = '823' LIMIT 1)),

('유니클로 양말', 9900, '기본 크루삭스 3팩입니다.',
 (SELECT location_id FROM Location WHERE location_name = '824' LIMIT 1)),

('버버리 미니원피스', 359000, '버버리 체크 미니 드레스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '925' LIMIT 1)),

('르메르 점프수트', 279000, '모노톤 여성용 점프수트입니다.',
 (SELECT location_id FROM Location WHERE location_name = '926' LIMIT 1)),

('르메르 맥시원피스', 299000, '롱 플로럴 드레스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '927' LIMIT 1)),

('나이키 레깅스', 39000, '운동용 하이웨이스트 레깅스입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1028' LIMIT 1)),

('아디다스 트레이닝 하의', 59000, '클래식 삼선 트레이닝 팬츠입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1029' LIMIT 1)),

('아디다스 트레이닝 상의', 69000, '지퍼 후디 스타일입니다.',
 (SELECT location_id FROM Location WHERE location_name = '1030' LIMIT 1));
 
 -- ProductDetail INSERT
INSERT INTO ProductDetail(product_size_name, product_quantity, product_id)
VALUES 
('S', 20, 1),
('M', 20, 2),
('L', 20, 3),
('S', 20, 4),
('M', 20, 5),
('L', 20, 6),
('S', 20, 7),
('M', 20, 8),
('L', 20, 9),
('S', 20, 10),
('M', 20, 11),
('L', 20, 12),
('S', 20, 13),
('M', 20, 14),
('L', 20, 15),
('S', 20, 16),
('M', 20, 17),
('L', 20, 18),
('S', 20, 19),
('M', 20, 20),
('L', 20, 21),
('S', 20, 22),
('M', 20, 23),
('L', 20, 24),
('S', 20, 25),
('M', 20, 26),
('L', 20, 27),
('S', 20, 28),
('M', 20, 29),
('L', 20, 30);

-- ProductImage INSERT
INSERT INTO ProductImage(image_url, product_id)
VALUES
('/images/product1.jpg', 1),
('/images/product2.jpg', 2),
('/images/product3.jpg', 3),
('/images/product4.jpg', 4),
('/images/product5.jpg', 5),
('/images/product6.jpg', 6),
('/images/product7.jpg', 7),
('/images/product8.jpg', 8),
('/images/product9.jpg', 9),
('/images/product10.jpg', 10),
('/images/product11.jpg', 11),
('/images/product12.jpg', 12),
('/images/product13.jpg', 13),
('/images/product14.jpg', 14),
('/images/product15.jpg', 15),
('/images/product16.jpg', 16),
('/images/product17.jpg', 17),
('/images/product18.jpg', 18),
('/images/product19.jpg', 19),
('/images/product20.jpg', 20),
('/images/product21.jpg', 21),
('/images/product22.jpg', 22),
('/images/product23.jpg', 23),
('/images/product24.jpg', 24),
('/images/product25.jpg', 25),
('/images/product26.jpg', 26),
('/images/product27.jpg', 27),
('/images/product28.jpg', 28),
('/images/product29.jpg', 29),
('/images/product30.jpg', 30);

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
('IN', 10, 30);