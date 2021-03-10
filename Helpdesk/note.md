# HelpDesk

## Master Branch

###  2.13 (토)

* Ver 0.1 - 어플 기본 틀 잡기
* splash 화면 구현, 로그인 화면 구현, 기본 홈 화면 구현
* 드로어 구현 - left : Expandable ListView , right : NavigationView
* Expandable ListView 어댑터 - 리소스 인플레이트해서 뷰 구성

```kotlin
        // 레이아웃 생성
        val view = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.item_nav_list_group, parent, false)
        // 텍스트 뷰 채우기
        val textView = view.findViewById<TextView>(R.id.txt_title)
        textView.text = itemList[groupPosition].title
        // 이미지 뷰 설정 : 열리고 닫힌 상태에 따라 drawable 다르게 설정
        val imageView = view.findViewById<ImageView>(R.id.img_indicator)
        val indicatorRes = if (isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand
        imageView.setImageResource(indicatorRes)
```



## Develop Branch

### 2.13 (토)

* Master 에서 가져옴

### 2.23 (화)

* 네비게이션 하위 탭 아이템 클릭 처리, 액티비티 띄우기 구현
* 회사에 웹 화면 스크린샷 요청

### 3.5 (금)

* 새 창 띄우기를 액티비티 -> 프래그먼트로 수정

### 3.8 (월)

* 홈버튼 dehaze(햄버거버튼) 으로 수정
* 불필요한 코드 삭제

### 3.10 (수)

* 

## 이후 필요한 개발사항

* 홈 화면 구성

* 챗봇 버튼 클릭시 (액티비티로 띄움) 액션바 구성

* 물음표 헤드셋 모양으로 바꾸고 좀더 키우고 왼쪽 위로좀더 이동

* 뒤로가기 입력시 바로 이전으로 (로그인 화면 말고)

  ---

* 로그인 분기해서 통신 구현

