## OAuth
## OAuth1 란
OAuth는 Open Authorization, Open Authentication 뜻하는 것으로 애플리케이션(페이스북,구글,트위터)(Service Provider)의 유저의 비밀번호를 Third party앱에 제공 없이 인증,인가를 할 수 있는 오픈 스탠다드 프로토콜이다. OAuth 인증을 통해 애플리케이션 API를 유저대신에 접근할 수 있는 권한을 얻을 수 있다. OAuth가 사용되기 전에는 외부 사이트와 인증기반의 데이터를 연동할 때  인증방식의 표준이 없었기 때문에 기존의 기본인증인 아이디와 비밀번호를 사용하였는데, 이는 보안상 취약한 구조였다. 유저의 비밀번호가 노출될 가망성이 크기 때문이다. 그렇기 때문에 이 문제를 보안하기 위해 OAuth의 인증은 API를 제공하는 서버에서 진행하고, 유저가 인증되었다는 Access Token을 발급하였다. 그 발급된 Access token으로 Third party(Consumer)애플리케이션에서는 Service Provider의 API를 안전하고 쉽게 사용할 수 있게 되었다.

### 동작 방식
먼저 OAuth의 동작 방식을 알기 위해서는 아래의 개념을 알고 있어야 한다.
#### OAuth의 개념
먼저 OAuth의는 아래와 같은 개념을 가지고있다.
| 용어               | 설명                                                                                                                   |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------- |
| User               | Service Provider에 계정을 가지고 있으면서, Consumer앱을 이용하려는 사용자                                              |
| Service Provider   | OAuth를 사용하는 Open API를 제공하는 서비스 (facebook,google등)                                                        |
| Protected Resource | Service Provider로부터 제공되어지는 API 자원들                                                                         |
| Consumer           | OAuth 인증을 사용해 Service Provider의 기능을 사용하려는 애플리케이션이나 웹 서비스                                    |
| Consumer Key       | Consumer가 Service Provider에게 자신을 식별하는 데 사용하는키                                                          |
| Consumer Secret    | Consumer Key의 소유권을 확립하기 위해 Consumer가 사용하는 Secret                                                       |
| Request Token      | Consumer가 Service Provider에게 접근 권한을 인증받기 위해 사용하는 값. 인증이 완료된 후에는 Access Token으로 교환한다. |
| Access Token       | 인증 후 Consumer가 Service Provider의 자원에 접근하기 위한 키를 포함한 값                                              |
| Token Secret       | 주어진 토큰의 소유권을 인증하기 위해 소비자가 사용하는 Secret                                                          |

#### OAuth의 WorkFlow
다음은  OAuth의 1.0의 WorkFlow이다.
![](https://i.imgur.com/7T48KvR.png)


위 WorkFlow 흐름은 간단하게 이렇다.
1. 그림에는 없지만 가장 먼저 Consumer는 Service Provider로부터 Client key와 Secret을 발급 받아야한다. 이것은 Service Provider에 API를 사용할것을 등록하는것과 동시에 Service Provider가 Consmer를 식별할 수 있게 해준다.
2. 그림에 A 처럼 Request Token을 요청할 때 Consumer 정보, Signature 정보를 포함하여 Request token을 요청을하고 B의 흐름처럼 Request token을 발급받는다.
3. Request Token값을 받은후 Consumer는 C처럼 User를 Service Provider에 인증 사이트로 다이렉트시키고, 유저는 그곳에서 Service Provider에 유저임을 인증하게 된다.
4. 그러면 Consumer는 D의 정보처럼 해당 유저가 인증이되면 OAuth_token와 OAuth_verifier를 넘겨준다.
5. 그이후에 Consumer는 OAuth_token와 OAuth_verifier받았다면 E의 흐름처럼 다시 서명을 만들어 Access Token을 요청하게 된다.
6. 그리고 Service Provider는 받은 토큰과 서명들이 인증이 되었으면 Access Token을 F의 정보 처럼 넘기게된다.
7. 그리고 그 Access Token 및 서명정보를 통해 Service Provider에 Protected Resource에 접근할 수 있게 된다.

아래는 Consumer의 요청시 매개변수의 종류에 대한 설명이다.

##### Request Token 발급 매개변수
Request Token 발급 요청 시 사용하는 매개변수는 다음 표와 같다.
| 매개변수               | 설명                                                                                                                                                                                           |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| OAuth_callback         | Service Provider가 인증을 완료한 후 리다이렉트할 Consumer의 웹 주소. 만약 Consumer가 웹 애플리케이션이 아니라 리다이렉트할 주소가 없다면 소문자로 'oob'(Out Of Band라는 뜻)를 값으로 사용한다. |
| OAuth_consumer_key    |Consumer를 구별하는 키 값. Service Provider는 이 키 값으로 Consumer를 구분한다. |                                                                                                                                                                                                |
| OAuth_nonce  |Consumer에서 임시로 생성한 임의의 문자열. OAuth_timestamp의 값이 같은 요청에서는 유일한 값이어야 한다. 이는 악의적인 목적으로 계속 요청을 보내는 것을 막기 위해서이다.          |                                                                                                                                                                                                |
| OAuth_signature |OAuth 인증 정보를 암호화하고 인코딩하여 서명 값. OAuth 인증 정보는 매개변수 중에서 OAuth_signature를 제외한 나머지 매개변수와 HTTP 요청 방식을 문자열로 조합한 값이다. 암화 방식은 OAuth_signature_method에 정의된다.       |                                                                                                                                                                                                |
| OAuth_signature_method |OAuth_signature를 암호화하는 방법. HMAC-SHA1, HMAC-MD5 등을 사용할 수 있다. |                                                                                                                                                                                                |
| OAuth_timestamp |요청을 생성한 시점의 타임스탬프. 1970년1월 1일 00시 00분 00초 이후의 시간을 초로 환산한 초 단위의 누적 시간이다.       |                                                                                                                                                                                                |
| OAuth_version   |	OAuth 사용 버전. 1.0a는 1.0이라고 명시하면 된다.      |                                                                                                                    
##### Request Token Signature 생성

OAuth 1.0에서 Service Provider에게 요청을 할려면 매번 전자 서명을 만들어서 보내야한다. 아래는 전자서명을 만드는 순서이다.

1. 요청 매개변수를 모두 모은다.
OAuth_signature를 제외하고 'OAuth_'로 시작하는 OAuth 관련 매개변수를 모은다. 모든 매개변수를 사전순으로 정렬하고 각각의 키(key)와 값(value)에 URL 인코딩(rfc3986)을 적용한다. URL 인코딩을 실시한 결과를 = 형태로 나열하고 각 쌍 사이에는 &을 넣는다. 이렇게 나온 결과 전체에 또 URL 인코딩을 적용한다.

2. 매개변수를 정규화(Normalize)한다.
모든 매개변수를 사전순으로 정렬하고 각각의 키(key)와 값(value)에 URL 인코딩(rfc3986)을 적용한다. URL 인코딩을 실시한 결과를 = 형태로 나열하고 각 쌍 사이에는 &을 넣는다. 이렇게 나온 결과 전체에 또 URL 인코딩을 적용한다.

3. Signature Base String을 만든다.
HTTP method 명(GET 또는 POST), Consumer가 호출한 HTTP URL 주소(매개변수 제외), 정규화한 매개변수를 '&'를 사용해 결합한다. 즉 '[GET|POST] + & + [URL 문자열로 매개변수는 제외] + & + [정규화한 매개변수]' 형태가 된다.

4. 키 생성
3번 과정까지 거쳐 생성한 문자열을 암호화한다. 암호화할 때 Consumer Secret Key를 사용한다. Consumer Secret Key는 Consumer가 Service Provider에 사용 등록을 할 때 발급받은 값이다. HMAC-SHA1 등의 암호화 방법을 이용하여 최종적인 OAuth_signature를 생성한다.


##### Access Token 매개변수
다음은 Access Token 발급을 요청할 때 사용하는 매개변수 표이다.

| 매개변수               | 설명 |
| ---------------------- | ---- |
| OAuth_consumer_key     | Consumer를 구별하는 키 값. Service Provider는 이 키 값으로 Consumer를 구분한다.     |
| OAuth_nonce            | Consumer에서 임시로 생성한 임의의 문자열. OAuth_timestamp의 값이 같은 요청에서는 유일한 값이어야 한다. 이는 악의적인 목적으로 계속 요청을 보내는 것을 막기 위해서이다.     |
| OAuth_signature        |OAuth 인증 정보를 암호화하고 인코딩하여 서명 값. OAuth 인증 정보는 매개변수 중에서 OAuth_signature를 제외한 나머지 매개변수와 HTTP 요청 방식을 문자열로 조합한 값이다. 암화 방식은 OAuth_signature_method에 정의된다.      |
| OAuth_signature_method | OAuth_signature를 암호화하는 방법. HMAC-SHA1, HMAC-MD5 등을 사용할 수 있다.     |
| OAuth_timestamp        | 요청을 생성한 시점의 타임스탬프. 1970년1월 1일 00시 00분 00초 이후의 시간을 초로 환산한 초 단위의 누적 시간이다.     |
| OAuth_version          |  OAuth 사용 버전    |
| OAuth_verifier         | Request Token 요청 시 OAuth_callback으로 전달받은 OAuth_verifier 값     |
| OAuth_token            | Request Token 요청 시 OAuth_callback으로 전달받은 OAuth_token 값     |


##### API호출을 위한 매개변수
다음은 Access Token을 사용해 API를 호출할 때 사용하는 매개변수는 다음 표이다.
| 매개변수               | 설명 |
| ---------------------- | ---- |
| OAuth_consumer_key     | Consumer를 구별하는 키 값. Service Provider는 이 키 값으로 Consumer를 구분한다.     |
| OAuth_nonce            | Consumer에서 임시로 생성한 임의의 문자열. OAuth_timestamp의 값이 같은 요청에서는 유일한 값이어야 한다. 이는 악의적인 목적으로 계속 요청을 보내는 것을 막기 위해서이다.     |
| OAuth_signature        | OAuth 인증 정보를 암호화하고 인코딩하여 서명 값. OAuth 인증 정보는 매개변수 중에서 OAuth_signature를 제외한 나머지 매개변수와 HTTP 요청 방식을 문자열로 조합한 값이다. 암화 방식은 OAuth_signature_method에 정의된다.     |
| OAuth_signature_method | OAuth_signature를 암호화하는 방법. HMAC-SHA1, HMAC-MD5 등을 사용할 수 있다.     |
| OAuth_timestamp        |요청을 생성한 시점의 타임스탬프. 1970년1월 1일 00시 00분 00초 이후의 시간을 초로 환산한 초 단위의 누적 시간이다.      |
| OAuth_version          | OAuth 버전     |
| OAuth_token                       | OAuth_callback으로 전달받은 OAuth_token     |


## OAuth2란
OAuth의2는 OAuth의1의 유저의 인증플로우, 전반적인 목적만 공유하고 OAuth의1.0을 새로 작성한것이다. OAuth의1.0과 OAuth의2.0의 차이는 앱 애플리케이션, 웹 애플리케이션, 데스크탑 애플리케이션등의 인증방식을 강화하고 Consumer에 개발 간소화를 중심으로 개발 되었다.

### OAuth의1.0 과 OAuth의2.0차이점
아래는 OAuth 1.0 에서 OAuth2.0 차이점은 일단 인증 절차 간소화 됨으로써 개발자들이 구현하기 더쉬워졌고, 기존에 사용하던 용어도 바뀌면서 Authorizaiton server와 Resource서버의 분리가 명시적으로 되었다. 또한 다양한 인증 방식을 지원하게 됐다. 아래는 1.0과 2.0의 차이점을 나열한것이다.
#### 인증 절차 간소화
* 기능의 단순화, 기능과 규모의 확장성 등을 지원하기 위해 만들어 졌다.
* 기존의 OAuth1.0은 디지털 서명 기반이었지만 OAuth2.0의 암호화는 https에 맡김으로써 복잡한 디지털 서명에관한 로직을 요구하지 않기때문에 구현 자체가 개발자입장에서 쉬워짐.

#### 용어 변경
* Resource Owner : 사용자 (1.0 User해당)
* Resource Server : REST API 서버 (1.0 Protected Resource)
* Authorization Server : 인증서버 (API 서버와 같을 수도 있음)(1.0 Service Provider)
* Client : 써드파티 어플리케이션 (1.0 Service Provider 해당)

#### Resource Server와 Authorization Server서버의 분리
* 커다란 서비스는 인증 서버를 분리하거나 다중화 할 수 있어야 함.
* Authorization Server의 역할을 명확히 함.

#### 다양한 인증 방식(Grant_type)
* Authorization Code Grant
* Implicit Grant
* Resource Owner Password Credentials Grant
* Client Credentials Grant
* Device Code Grant
* Refresh Token Grant

### 인증 종류
OAuth 2.0의 인증종류는 6가지 입니다. 아래는 각각의 인증방식의 flow와 간단한 설명입니다.

#### Authorization Code Grant
일반적인 웹사이트에서 소셜로그인과 같은 인증을 받을 때 가장 많이 쓰는 방식으로 기본적으로 지원하고 있는 방식이다. 아래는 Authorization Code Grant type 으로 Access Token을 얻어오는 시퀀스 다이어그램이다.
![](https://i.imgur.com/xaKCz9E.png)
1. 먼저 클라이언트가 Redirect URL을 포함하여 Authorization server 인증 요청을 한다.
2. AuthorizationServer는 유저에게 로그인창을 제공하여 유저를 인증하게 된다.
3. AuthorizationServer는 Authorization code를 클라이언트에게 제공해준다.
4. Client는 코드를 Authorization server에 Access Token을 요청한다.
5. Authorization 서버는 클라이언트에게 Access token을 발급해준다.
6. 그 Access token을 이용하여 Resource server에 자원을 접근할 수 있게 된다.
7. 그이후에 토큰이 만료된다면 refresh token을 이용하여 토큰을 재발급 받을 수 있다.

#### Implicit Grant
Public Client인 브라우저 기반의 애플리케이션(Javascript application)이나 모바일 애플리케이션에서 바로 Resource Server에 접근하여 사용할 수 있는 방식이다.
![](https://i.imgur.com/DayLoth.png)
1. 클라이언트는 Authorization server에 인증을 요청한다.
2. 유저는 Authorization server를 통해 인증한다.
3. Authorization server는 Access token을 포함하여 클라이언트의 Redirect url을 호출한다.
4. 클라이언트는 해당 Access token이 유효한지 Authorization server에 인증요청한다.
5. 인증서버는 그 토큰이 유효하다면 토큰의 만기시간과함께 리턴해준다.
6. 클라이언트는 Resource server에 접근할 수 있게된다.


#### Resource Owner Password Credentials Grant
Client에 아이디/패스워드를 받아 아이디/패스워드로 직접 access token을 받아오는 방식이다. Client가 신용이 없을 때에는 사용하기에 위험하다는 단점이 있다. 클라이언트가 확실한 신용이 보장될 때 사용할 수 있는 방식이다.
![](https://i.imgur.com/FVv86QT.png)
1. User가 Id와 Password를 입력한다
2. 클라이언트는 유저의 id와 password와 클라이언트 정보를 넘긴다.
3. Authorization sever는 Access token을 넘긴다.


#### Client Credentials Grant
애플리케이션이 Confidential Client일 때 id와 secret을 가지고 인증하는 방식이다.
![](https://i.imgur.com/TMXAjvo.png)
1. 클라이언트 정보를 Authorization server에 넘긴다.
2. Access Token을 Client에 전달한다.

#### Device Code Grant
장치 코드 부여 유형은 브라우저가 없거나 입력이 제한된 장치에서 사용됩니다.

#### Refresh Token Grant
기존에 저장해둔 리프러시 토큰이 존재할 때 엑세스토큰 재발급 받을 필요가 있을 때 사용한다. 그리고 기존 액세스는 토큰이 만료된다.



참고 사이트

* [Naver D2](http://d2.naver.com/helloworld/24942)
* [OAuth.net](https://OAuth.net/core/1.0/)
* [Showerbugs](https://showerbugs.github.io/2017-11-16/OAuth-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C)
* [OAuth 2.0 Servers](https://www.OAuth.com/OAuth2-servers/differences-between-OAuth-1-2/)
* [이수홍](https://brunch.co.kr/@sbcoba/4)
* [Authorization Code Flow](https://developer.accela.com/docs/construct-authCodeFlow.html)
* [OX Wiki](https://ox.gluu.org/doku.php?id=oxauth:implicitgrant)
