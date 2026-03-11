# 데이터베이스 생성
DROP DATABASE IF EXISTS `AM_jsp_2026_02`;
CREATE DATABASE `AM_jsp_2026_02`;
USE `AM_jsp_2026_02`;


# 게시글 테이블 생성
CREATE TABLE `article` (
    `id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `regDate` DATETIME NOT NULL,
    `updateDate` DATETIME NOT NULL,
    `title` CHAR(100) NOT NULL,
    `body` CHAR(100) NOT NULL
);

# 회원 테이블 생성
CREATE TABLE `member`(
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`regDate` DATETIME NOT NULL,
	`updateDate` DATETIME NOT NULL,
	`loginId` CHAR(100) NOT NULL,
	`loginPw` CHAR(100) NOT NULL,
	`name` CHAR(100) NOT NULL
);

# 게시글 데이터 삽입
INSERT INTO `article`
SET `regDate` = NOW(),
	`updateDate` = NOW(),
	`title` = '제목1',
	`body` = '내용1';
	
INSERT INTO `article`
SET `regDate` = NOW(),
	`updateDate` = NOW(),
	`title` = '제목2',
	`body` = '내용2';

INSERT INTO `article`
SET `regDate` = NOW(),
	`updateDate` = NOW(),
	`title` = '제목3',
	`body` = '내용3';

# 회원 데이터 삽입
INSERT INTO `member`
SET `regDate` = NOW(),
	`updateDate` = NOW(),
	`loginId` = 'test1',
	`loginPw` = 'test1',
	`name` = '회원1';
	
INSERT INTO `member`
SET `regDate` = NOW(),
	`updateDate` = NOW(),
	`loginId` = 'test2',
	`loginPw` = 'test2',
	`name` = '회원2';	

SELECT * FROM `article`;
	
SELECT * FROM `member`;


# 게시글 테이블에 memberId 추가
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER `updateDate`;

# 테이블 구조확인
DESC article;

# article 테이블 => memberId 값 추가
UPDATE article
SET memberId = 1
WHERE id IN (1,2);

UPDATE article
SET memberId = 2
WHERE id = 3;

DESC `member`;

# 테스트
SELECT *
FROM article
ORDER BY id DESC;

SELECT A.*, M.name AS e_w
FROM article AS A
INNER JOIN `member` AS M
ON A.memberId = M.id


# article 생성(대량) => 해당 쿼리 실행 시 article 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = CONCAT('제목', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
`body` = CONCAT('내용', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));

# member 생성(대량) => 해당 쿼리 실행 시 member 생성
INSERT INTO MEMBER
SET regDate = NOW(),
updateDate = NOW(),
loginId = CONCAT('loginId', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
loginPw = CONCAT('loginPw', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
`name` = CONCAT('name', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));

SELECT * FROM `member` WHERE loginId = 'test1';

SELECT COUNT(*) > 0
FROM `member`
WHERE loginId = 'test1';

SELECT COUNT(*) > 0
FROM `member`
WHERE loginId = 'test3';

SELECT SUBSTRING(RAND() * 1000 FROM 1 FOR 2);

UPDATE article
SET updateDate = NOW(),
title = '',
`body` = 'test1'
WHERE id = 1;

SELECT COUNT(*)
FROM article
WHERE id = 5;


UPDATE article
SET updateDate = NOW(),
`body` = 'test3'
WHERE id = 3;

SET regDate = NOW(),
updateDate = NOW(),
loginId = 'whdtjr970717',
loginPw = 'Jo1se1ph1@',
`name` ='이종석';

SELECT * FROM `article`;
	
SELECT * FROM `member`;
