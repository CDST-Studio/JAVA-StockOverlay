"""
코랩에서 사용시 구글 클라우드 연동 코드

# 코랩 → 구글 클라우드 연동
# 기본 경로는 /cotent, 자신의 구글 클라우드 홈 경로는 /content/gdrive/My Drive/,
from google.colab import drive

drive.mount('/content/gdrive/')
"""

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import json

# 프로그램 기본 인증 정보
cred = credentials.Certificate('/content/gdrive/My Drive/NeedFor/StockOverlay/needfor-stockoverlay-firebase-adminsdk.json'); # 파이어베이스 프로젝트 설정(Settin) → 서비스 계정 → Python 선택 후 비공개 키 생성해서 나오는 json 파일 입력

# 파이어베이스 DB 정보 가져오기
# firebase_admin.initialize_app(cred), 처음 DB에 접근할 때 사용해야 하는 코드, 두번째 접근부터는 사용 안 해도 된다.
db = firestore.client()

with open('/content/gdrive/My Drive/NeedFor/StockOverlay/stockData.json', 'r') as target:
    jsonData = json.load(target)

# json이 List 형태로 넘어올 경우 Dictionary 형태로 변환
if type(jsonData) == type([]) :
    savedData = {} # 변환된 데이터 담을 변수
    for dic in jsonData :
        # 종목코드를 문자열로 변환 (6자리 모드 채워넣기 위해서, 앞에 빈 곳은 0으로)
        int_stockCode = str(dic['code'])
        int_detailCode = str(dic['detail_code'])

        # 종목코드가 6자리 모두 채워지지 않은 경우 Ex. 004321 int형으로 4321 같이 저장되어 있으므로, 앞에 0을 채워넣는다.
        if len(int_stockCode) < 6 :
            stockCode = '0'
            for i in range(1, 6-len(int_stockCode)) : stockCode = stockCode + '0'
            stockCode = stockCode + int_stockCode
            savedData['code'] = stockCode
        # 그 외의 경우
        else :
            savedData['code'] = int_stockCode

        # 업종코드가 6자리 모두 채워지지 않은 경우 Ex. 004321 int형으로 4321 같이 저장되어 있으므로, 앞에 0을 채워넣는다.
        if len(int_detailCode) < 6 :
            detailCode = '0'
            for i in range(1, 6-len(int_detailCode)) : detailCode = detailCode + '0'
            detailCode = detailCode + int_detailCode
            savedData['detail_code'] = detailCode
        # 그 외의 경우
        else :
            savedData['detail_code'] = int_detailCode

        # 주식명(name)을 문서명으로 하여 Stock 컬렉션에 저장
        docRef = db.collection(u'Stock').document(dic['name']) # collection(u'(FireStore의 컬렉션 이름)').document(u'(FireStore의 특정 컬렉션 안의 문서 이름)'), 뒤에 더 붙일 수도 있다(컬렉션-문서-컬렉션-문서 순서여야 한다.)
        docRef.set(savedData) ; # dictionary 형태인 KEY : VALUE 형식으로 FireStore에 저장된다.
else :
    """
    docRef = db.collection(~).document(~) # collection(u'(FireStore의 컬렉션 이름)').document(u'(FireStore의 특정 컬렉션 안의 문서 이름)'), 뒤에 더 붙일 수도 있다(컬렉션-문서-컬렉션-문서 순서여야 한다.)
    docRef.set(~) ; # dictionary 형태인 KEY : VALUE 형식으로 FireStore에 저장된다.
    """