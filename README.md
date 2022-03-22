## 참고
이 레포지토리는 https://developers.google.com/youtube/v3/code_samples/java?hl=ko#add_a_channel_subscription를 참고 해서 만들어 졌습니다.
현재 youtube docs에 나와있는 유튜브 구독 api는 cli 환경을 기반으로 만들어져 있습니다. 스프링 부트 환경에서 사용할 수가 없어서
Spring Security & oAuth2 기반에서 유튜브 구독 api를 사용할 수 있도록 오픈소스를 만들었습니다.


## 사용전 준비사항
1. spring security & oauth2를 구성하기 위해 구글 개발자 콘솔에 프로젝트를 등록하고 테스트 계정, 콜백 url을 등록합니다.
2. 구글 개발자 콘솔에서 client-secret.json 파일을 다운받아 src/resources 폴더에 등록합니다.
3. application.yml 파일에 database 및 oauth 관련 설정 값을 사용자의 환경에 맞게 변경합니다.


## 사용법
1. 서버를 띄우고, localhost:8080?channel=구독하려는채널id 로 요청합니다.
2. 구글 로그인을 하면 로그인 사용자 정보를 src/resources 하위에 사용자 별 Credential 파일을 생성합니다.
3. 유튜브 구독 권한을 체크하면 2번에서 생성한 파일의 정보를 읽어서 구독 Api를 요청하고 파일을 삭제합니다.

## 주의사항
Youtube Api 사용을 위해 Java 1.6이 강제됩니다. jdk 1.8사용은 가능하지만 8문법은 사용못합니다.
