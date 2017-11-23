Remember Assignment : PhotoStory

개발 툴
--------
Android Studio 3.0

사용 라이브러리
--------
Realm, RxJava(RxBinding), Mokito
앱 제작 기획서에 데이터 입출력에 대한 기준이 없어, 내부 DB를 사용하기 위해 Realm을 사용
Observable을 이용한 바인딩을 수행하고자 RxJava를 사용
RxJava의 Observable을 테스트하기 위해 Mock-up 데이터로 테스트 할 수 있게 해주는 Mokito를 사용

앱 구조
--------
RxJava를 통한 바인딩으로 MVVM 패턴
Model(Story, Photo...) : 데이터를 관리, realm을 통한 데이터 입출력
ViewModel(StoryViewModel...) : Model과 데이터를 주고 받고 이를 View와 상호작용하며 대응 (View에대한 의존성 없음)
View(layout, Activites...) : ViewModel과 바인딩을 통하여 유저에게 정보를 표시하거나 입력을 받음

Model
--------
앱 동작에 기본이 되는 데이터 단위로 구성함
Model을 통하여 데이터를 가져오거나 입력, 수정 (Realm 작업이 이루어지는 곳)
특정 데이터(model)에 대한 기본 로직, 비지니스 로직 등을 정의

ViewModel
--------
Model로부터 데이터를 가져와 View에 전달해주는 방식을 결정
View로부터 발생한 이벤트를 처리하고 Model에 어떠한 영향을 끼칠지 결정
View로부터 완전히 독립적이며 RxJava를 통한 바인딩으로 View와 상호작용

View
--------
Activity, layout 등으로 구성된 사용자와 상호작용하는 영역(화면)
해당 View에서 필요한 데이터를 다루는 Model을 처리하는 ViewModel을 사용
RxJava를 통한 바인딩으로 ViewModel과 유연하게 상호작용

필요 개선 사항
--------
Model에 Realm에 대한 작업이 구현되어 있음
Realm 입출력 등에 대한 구현을 Realm자체에서 처리하고 Model은 입출력 Unit(Realm)의 함수 콜을 통하여 처리
추후 Model의 입출력 unit이 바뀌더라도(ex: network(retrofit...)) 해당 Unit의 함수를 통하여 처리

MVVM패턴의 이해와 학습을 위해 추천되는 MVVM패턴을 참고하지 않음
추후 Android Architecture Components 등을 참고하고 추천 MVVM구조 등과 비교하여
현재 구조의 문제점과 파악하지 못한 부분 등을 수정할 것(ex: LifeCycle을 같는 ViewModel 구현)