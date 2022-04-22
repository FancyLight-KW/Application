# 현대트랜시스 산학연계 프로젝트 - AI기반의 모바일 헬프데스크 (Android)
> **사내 ICT팀 비즈니스부서 사용자들의 IT지원 요청사항에 대한 시나리오 기반의 자동 답변 및 Helpdesk 업무 관리**
---

## Project Period
2021.02~2021.05

## Architecture
![FancyLight](https://user-images.githubusercontent.com/50645183/119451429-33eba480-bd70-11eb-84e7-7fedb833434b.PNG)

## 팀 소개
- 🙋‍♂️[홍세화](https://github.com/jrhong95) - Server
- 💁‍♂️[성치훈](https://github.com/Chihoon-Sung) - Android
- 🙆‍♂️[이동기](https://github.com/rkdmf1026) - Android
- 🤷‍♂️[이우제](https://github.com/woojerry) - Web Front-end
- 🙎‍♂️[염연웅](https://github.com/bingoring) - Chatbot

## PREVIEW
<img width="200" src = "https://user-images.githubusercontent.com/50603273/164773602-f90f1de3-3b53-4a84-b782-d2ce5a3bcd60.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773627-eb304355-4d4d-4f35-b49b-5b1b3edaa25a.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773634-f34b879f-c706-44eb-8e02-18ad8ff426a0.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773636-e1e659c2-1b7d-4b49-8835-ceaf08a3671c.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773647-6e3567ad-dec9-4b96-92de-e12dbb795a67.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773652-70a29268-d902-45c1-8bb7-5fa5494b6ca3.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773655-d17bff0c-d2f9-4b2e-aa0d-c60ae90e6989.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773658-d658f19b-1838-4711-8d65-4c1cb6a40553.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773663-4474105d-4cf7-4f3f-95ff-a0502599079a.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773665-3abff90d-d863-4c0c-89d9-c8bc15f34177.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773666-7a1a6f61-7607-43e5-83dd-5b207379c60a.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773671-d51514b9-7722-4eeb-ae91-c147d1e0e374.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773674-f2adfb85-baa7-4fd3-90b9-ddd2be9295cb.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773675-d2fabda7-45d1-427f-9f29-1a4db6cb3f2a.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773677-8686f49d-9c11-4a02-af3e-83d18ee16867.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773680-34386ef6-80c2-4831-8b0e-b01c817f2cf7.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773682-6972e0cf-6513-489f-aea1-33b805bede1f.png" /><img width="200" src = "https://user-images.githubusercontent.com/50603273/164773685-dccb5b3e-03a6-49ff-a812-12475a4df3a8.png" />

## 주요 기능

홈
```bash
현재 접수된 요청들의 상태별 현황을 확인할 수 있으며, 드로어를 통해 메뉴의 기능들을 사용 가능합니다.
```

요청/접수
```bash
모든 요청 목록을 확인할 수 있으며, 선별적인 검색기능을 제공하고, 요청/접수하기 버튼 클릭 시 요청내용을 접수할 수 있습니다.
```

사용자 권한에 따른 나의 @@목록
```bash
사용자 권한에 따라 사원은 "나의 요청목록", 요원은 "나의 작업목록", 관리자는 "나의 결재목록"의 기능 사용이 가능합니다.
```

나의 요청목록(사원)
```bash
사용자(사원)가 등록한 요청 목록을 보여주며, 각 요청을 삭제 및 수정이 가능합니다.
```

나의 작업목록(요원)
```bash
사용자(요원)에게 할당된 요청 목록을 보여주며, 각 요청 내용을 처리할 수 있습니다.
```

나의 결재목록(관리자)
```bash
사용자(관리자)에게 결재가 필요한 요청 목록을 보여주며, 각 요청 내용을 요원에게 할당하거나 반려할 수 있습니다.
```

알림 기능
```bash
모든 요청이 할당되거나 변경될 때 해당하는 사용자 혹은 요원에게 알림을 전송합니다. 알림 클릭 시 관련된 화면으로 이동합니다.
```

챗봇
```bash
앱 내부에서 챗봇과의 채팅을 통해 단순 문의가 해결 가능합니다.
```

## Packaging
```bash
Helpdesk
  ├─📂adapter
  ├─📂model
  ├─📂network
  ├─📂object
  ├─📂sharePref
  └─📂ui      
```

## Tech Stack
```bash
  - Kotiln
  - FCM
  - Retrofit
```
