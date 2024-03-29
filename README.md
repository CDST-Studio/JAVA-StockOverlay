# 흑우상향

<img src="https://user-images.githubusercontent.com/37203016/106876573-2d7ee180-671b-11eb-98f8-5603d4735b13.png">

<p>코로나 시대에 따라 많은 개인들이 주식 투자에 뛰어들었고, 계속 주가 창을 바라볼 수 없는 바쁜 현대인들은 그에 따라 주식 시장의 흐름을 따라는 것이 힘들어, 많은 손해를 보기도 한다.</p> 
<p>이러한 점을 "흑우상향" 앱으로 해결해보자 </p>

[PlayStoreLink](https://play.google.com/store/apps/details?id=com.cdst.stockoverlay)

* 로그인 화면

![Login](https://user-images.githubusercontent.com/38196821/106884687-f9102300-6724-11eb-9d74-4fc25f6c6d7a.gif)

# 주요기능
관심종목 추가에 따른 부가기능 제공
* 관심종목 추가

![AddInterestedStocks](https://user-images.githubusercontent.com/38196821/106884698-fca3aa00-6724-11eb-9ffc-48deca87ce00.gif)


* 오버레이 뷰 (Overlay View)

![OverlayView](https://user-images.githubusercontent.com/38196821/106883752-c1ed4200-6723-11eb-9146-56b80e76eb3e.gif)

* 목표 수익 달성 알림 (Revenue of Target Alarm)

![TargetProfit](https://user-images.githubusercontent.com/38196821/106883707-b26df900-6723-11eb-8342-efb6d7b3e4a9.gif)


▶ 메뉴바에 알림표시

![5](https://user-images.githubusercontent.com/38196821/106883813-d8939900-6723-11eb-9130-511afe693ec3.jpg)

# 주의사항! (Precautions)
* 크롤링으로 주식 정보를 긁어와서 반영 속도에 딜레이가 있다.

* 내부 저장소에 개인 데이터가 저장되어 있어서 앱 삭제시 개인 데이터가 날라간다.  

# 사용 설명서 (Manual)
<img src="https://github.com/Team-NeedFor/StockOverlay/blob/master/app/src/main/res/drawable-v24/bottom_ic_bookmarkstock.png?raw=true" width = "80">
<p>메인화면을 보여줍니다.</p>
<p>메인화면에는 자신이 추가한 관심목록이 있으며, 이곳에서 주식을 터치하여 매입가와 목표수익을 설정할 수 있습니다.</p>


<img src="https://github.com/Team-NeedFor/StockOverlay/blob/master/app/src/main/res/drawable-v24/bottom_ic_search.png?raw=true" width = "80">
주식 종목을 검색합니다.


<img src="https://github.com/Team-NeedFor/StockOverlay/blob/master/app/src/main/res/drawable-v24/bottom_ic_setting.png?raw=true" width = "80">
스톡보드에 대한 딜레이와 매입가, 목표수익 표기 여부에 대한 설정을 합니다.


<img src="https://github.com/Team-NeedFor/StockOverlay/blob/master/app/src/main/res/drawable-v24/ic_up.png?raw=true" width = "80">
화면 상단에 표시되는 스톡보드를 실행합니다. 목표수익 달성시 달성 알람이 옵니다.


# 향후 업데이트 예정 (Update Scheduled Information)
FireStore 데이터 연동 중인 것을 감안한 여러 서비스 제공 예정    
* 마법의 소라고동

* 앱 삭제 후에도 유저 데이터 유지

* 손익에 대한 가능한만큼 깔끔한 기능과 UI 제공 예정

# 오류 제보 (Error Report)
(대충 링크)에 댓글로 오류 제보 부탁드립니다.

# 알려진 오류 (Issue)
* 구글 연동 로그인 오류 : 구글 앱 콘솔 → 설정 → 앱 무결성 → 앱 서명키 인증서를 Firebase 설정에 sha1 키를 추가해서 해결
* java.lang.RuntimeException : 스플래시 이미지를 xxhdpi로 바꿔서 해결
* java.util.NoSuchElementException : OverlayService에 iterator 다음 값 가져올 때 Null일 경우 NoSuch 안 뜨게 Null값 처리 추가해서 해결
* java.lang.NullPointerException : Thread 및 OverlayService에 Null값 처리 추가해서 해결
* android.view.WindowManager$BadTokenException : StartActivity 종료되고 DialogAlert가 실행이 되서 나는 에러였기에 finish()를 onDestroy() 메서드에서 실행되도록 옮겨서 해결

# 개발자 정보 (Developer Information)
* 송대석 : [Github](https://github.com/DaeSeokSong)  
* 배시현 : [Github](https://github.com/bbaesi)  
* 김경호 : [Github](https://github.com/ykm989)  

# 저작권 (CopyRight)
* 저희의 코드는 기본적으로 *MIT License*를 따르고 있습니다.  
* 로고 저작권자 : sunnyridgee@gmail.com
