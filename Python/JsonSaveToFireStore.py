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

with open('/content/gdrive/My Drive/NeedFor/StockOverlay/StockList.json', 'r') as target:
    jsonData = json.load(target)

# json이 List 형태로 넘어올 경우 Dictionary 형태로 변환
dicData = {} # 변환된 데이터 담을 변수
if type(jsonData) == type([]) :
    for dic in jsonData :
        # 종목코드를 문자열로 변환 (6자리 모드 채워넣기 위해서, 앞에 빈 곳은 0으로)
        int_stockCode = str(dic['stockCode'])
        # 종목코드가 6자리 모두 채워지지 않은 경우 Ex. 004321 int형으로 4321 같이 저장되어 있으므로, 앞에 0을 채워넣는다.
        if len(int_stockCode) < 6 :
            stockCode = '0'
            for i in range(1, 6-len(int_stockCode)) : stockCode = stockCode + '0'
            stockCode = stockCode + int_stockCode
            dicData[dic['name']] = stockCode
        # 그 외의 경우
        else :
            dicData[dic['name']] = int_stockCode

# 응용 프로그램 기본 인증 정보
cred = credentials.Certificate('~.json'); # 파이어베이스 프로젝트 설정(Settin) → 서비스 계정 → Python 선택 후 비공개 키 생성해서 나오는 json 파일 입력

# 파이어베이스 DB 정보 가져오기
db = firestore.client()
docRef = db.collection(u'Stock').document(u'StockNameToCode') # collection(u'(FireStore의 컬렉션 이름)').document(u'(FireStore의 특정 컬렉션 안의 문서 이름)')
docRef.set(dic_data) ; # dictionary 형태인 KEY : VALUE 형식으로 FireStore에 저장된다.