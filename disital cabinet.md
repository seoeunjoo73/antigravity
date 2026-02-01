Proposed Changes
[Component Name] Domain Entities
디지털 캐비닛을 위한 새로운 엔티티들을 추가합니다.

[NEW] 
Company.java
업체 정보를 담는 엔티티입니다.
[NEW] 
AppUser.java
사용자 정보를 담으며, 업체(Company)와 연결됩니다.
역할(Role): REPRESENTATIVE(업체대표자), USER(업체사용자)
[NEW] 
Folder.java
업체별 폴더를 나타냅니다.
maxFileSize 필드를 통해 파일 업로드 사이즈 제한을 설정합니다.
[NEW] 
FolderPermission.java
폴더와 사용자 간의 접근 권한을 관리하는 엔티티입니다.
[NEW] 
CabinetFile.java
폴더 내에 업로드된 파일 정보를 담는 엔티티입니다.
[Component Name] Services & Controllers
폴더 생성, 권한 제어, 파일 업로드 로직을 구현합니다.

[NEW] 
CabinetService.java
업체 대표자의 폴더 생성 로직.
업체 사용자에 대한 폴더 접근 권한 부여/취소 로직.
파일 업로드 시 사이즈 제한 체크 로직.
[NEW] 
CabinetController.java
디지털 캐비닛 관련 API 엔드포인트.
Verification Plan
Automated Tests
JUnit 테스트 코드를 작성하여 다음 시나리오를 검증합니다:
업체 대표자가 폴더를 생성할 수 있는지 확인.
업체 대표자가 다른 사용자에게 폴더 접근 권한을 부여할 수 있는지 확인.
권한이 없는 사용자가 폴더나 파일에 접근할 때 차단되는지 확인.
설정된 maxFileSize보다 큰 파일 업로드 시 실패하는지 확인.
Manual Verification
Postman 또는 Swagger를 사용하여 API 호출을 통해 기능을 확인합니다.
H2 Console 또는 DB 툴을 사용하여 데이터가 정상적으로 저장되는지 확인합니다.