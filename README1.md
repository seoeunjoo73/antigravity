# Board Management System

Spring Boot 기반의 유연한 게시판 관리 시스템입니다. 여러 개의 게시판을 생성하고 관리할 수 있으며, 게시글 작성 및 파일 첨부 기능을 제공합니다.

## 🚀 주요 기능

- **다중 게시판 관리**: 독립적인 여러 개의 게시판 운영 가능
- **게시글 CRUD**: 게시글 작성, 조회, 수정, 삭제 기능
- **페이징 처리**: Spring Data JPA를 활용한 효율적인 게시글 목록 페이징 (기본 10개씩)
- **파일 업로드**: 게시글 작성 시 이미지 및 문서 파일 첨부 기능
- **반응형 웹 인터페이스**: JSP와 JSTL을 사용한 서버 사이드 렌더링 화면

## 🛠 기술 스택

- **Backend**: Java 17, Spring Boot 3.2.2
- **Persistence**: Spring Data JPA, Hibernate
- **Database**: H2 Database (File-based)
- **View**: JSP, JSTL, Apache Tomcat Embed Jasper
- **Build Tool**: Maven
- **Lombok**: 코드 간소화를 위한 라이브러리

## ⚙️ 설정 및 환경

### Database (H2)
- **URL**: `jdbc:h2:file:./data/boarddb` (파일 기반 데이터베이스)
- **Console**: `/h2-console` (ID: `sa`, PW: 없음)

### 파일 업로드 경로
- **Upload Directory**: `./src/main/webapp/uploads`
- **최대 파일 크기**: 10MB

## 🏃 실행 방법

### Prerequisites
- JDK 17
- Maven 3.6+

### Build & Run
```bash
./mvnw spring-boot:run
```
또는 빌드 후 실행:
```bash
./mvnw clean package
java -jar target/board-0.0.1-SNAPSHOT.jar
```

## 📂 프로젝트 구조

- `controller/`: 웹 요청 처리 (BoardController, MainController 등)
- `service/`: 비즈니스 로직 (PostService, BoardService)
- `entity/`: 데이터베이스 테이블 매핑 (Board, Post, Attachment)
- `repository/`: Spring Data JPA 인터페이스
- `resources/`: 설정 파일 및 정적 리소스
- `webapp/WEB-INF/views/`: JSP 뷰 템플릿

![](image.png)  이모지 win + .  


## 📝 라이센스

이 프로젝트는 MIT 라이센스를 따라야 합니다.

