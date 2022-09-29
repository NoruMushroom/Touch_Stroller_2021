<h1>< 프로젝트 명칭 - 터치 유모차 ></h1>
<h2> * 해결하고자 하는 과제</h2>
저희는 사고가 빈번히 발생하고 있는 유모차에 관점을 맞춰서 아이디어를 계획하였습니다.<br>
기존에 출시한 유모차들은 편의성을 중시하고 안정성을 놓치고 있다는 느낌을 받았습니다.<br>
저희 팀은 편의성과 안정성 어느 하나 놓치지 않고 동시에 집중하여 두 마리 토끼를 잡고자 아이디어를 구상했습니다.
<br>
<h2> * 해결 방안</h2>
<h3>1. 접촉</h3>우선 안정성문제를 해결하기 위해서는 유모차가 운전자와의 접촉이 없으면 유모차는 멈춤 상태를 유지하는 생각을 해봤습니다.<br>
저희는 다양한 방법 중에 부모님이 직접 손으로 터치하여 유모차를 움직이도록 하여 부담을 줄이는 방법을 선택했습니다.<br>
원격조종을 택하지 않은 이유는 부모님이 직접 유모차를 만지고 있어야 안심된다고 안전하다고 생각 했습니다. 그리고 실제로도 그렇습니다.<br>
부모님이 손을 대고 있을 때만 움직이도록 구현하기 위해서 터치센서 이용하여 전진 후진 멈춤을 제어하도록 했습니다.
<br>

<h3>2. LED</h3>두번째 안정성 문제로 밤이나, 어두운 곳을 갈 경우를 대비하여 LED를 장착하였습니다.<br>
밤에 혹은 어두운 곳을 필히 이동해야 할 경우가 있습니다. 그 때 유모차가 있다고 자동차나 지나가는 행인들에게 인식시켜 피해 갈수 있도록 하기 위해서 전방에 LED를 장착하였습니다.<br>
이 LED는 조도센서에 의해 자동으로 제어 될 수도 있고 어플에서 수동 제어도 가능합니다. 그리고 동시에 유모차의 시야를 확보하는 역할을 동반합니다.

<br>

<h3>3. 수평의자</h3>세번째 안정성 문제로 오르막길이나 올라갈 때 아이의 자세가 쏠려 불안해하는 경우와,<br>
내리막길에서 아이가 안전벨트에 아기가 압박 당하는 경우를 방지하기 위해서 자이로센서를 이용해 실시간 각도를 측정하여 서보모터를 이용해 의자를 움직이도록 하였습니다.<br>
이 기능은 아기의 편의성도 동반합니다.

<br>

<h3>4. 온습도센서</h3>네번째로는 편의성 요소를 준비했습니다.<br>
더운 날 아이와 외출을 해야 할 때를 대비하여 온습도센서로부터 온도를 받아와 일정 온도를 넘는다면 선풍기를 돌려줍니다.<br>
이 기능은 계절에 따라서 탈부착 할 수 있도록 설계했습니다. 이 기능 또한 어플에서 수동으로도 제어 가능합니다.

<br>

<h3>5. 아이상태</h3>다섯 번째 편의성 요소입니다. 아이가 상태를 확인하고자 할 때 확인하고자 할 때 유모차 앞으로 가서 확인을 해야 했습니다.<br>
이를 편리하게 해결하고자 아기 얼굴 앞에 카메라를 설치하고, 머신러닝(Haar 특징기반 다단계 분류자 물체 검출)을 통해 아기 얼굴을 인식하고 판단하여<br>
보호자 휴대폰으로 아이 상태를 어플에 보내 보호자가 확인할 수 있습니다.

<br>

<h3>6. 로그인</h3>여섯 번째로 보안 문제입니다. 유모차와 어플과 블루투스로 연결하여 제어하다 보니 보안문제는 아주 큰 해결 과제입니다.<br>
주요 보안 방법으로는 로그인이 있습니다. 어플을 켰을 때 로그인을 하도록 하여 보안방법을 마련하였습니다.<br>
로그인 기능은 안드로이드 스튜디오와 연동성이 뛰어나며 응용하기에 편리한 데이터베이스 Fire base를 사용하여 구현했습니다. 

<br>

<h3>7. 추가 예정 기능</h3>저희는 로그인 뿐만 아니라 다른 보안 인증 방식 또한 필요하다고 인식하였습니다.<br>
지문인식을 추가해볼 예정입니다. 로그인 방식의 방법과 동시에 사용하여 보안을 강화하거나 지문인식만을 사용하여 사용자가 접근하기 편리하도록 할 수도 있습니다.<br>
또 추가해볼 예정인 내용으로 조도센서를 이용하여 햇빛 가리개를 제어하는 기능, 자이로센서를 통해 받는 경사로에 따라<br>
모터의 출력을 조절하여 오르막길 내리막길을 이동하는데 도움이 되도록 하는 기능을 추가할 예정입니다.

<br>

<h2> * 기대 효과</h2>
터치 유모차를 통해 얻을 수 있는 기대효과는 다음과 같습니다. <br>
-보호자와 아이의 위험부담이 크게 떨어지는 효과가 있습니다. <br>
-보호자의 입장에서 유모차를 주행 시 아이에 대한 부담감을 줄여 줄 수 있습니다. <br>
-다양한 기능들이 자동으로 실행되며 보호자가 아이와의 외출 후 체력적 부담을 완화할 수 있습니다. <br>
-기능들을 사용하면 유모차의 사고를 완벽하게 방지할 수 있습니다. <br>
-아이에게 온전히 썼던 관심을 다른 곳에도 사용할 수 있습니다. <br>
-아이에 관한 모든 것을 휴대폰으로 제어할 수 있습니다. <br>
-아이의 상태를 휴대폰으로 확인 할 수 있습니다.<br>
<h2> * Languages & Tools </h2>
<div class="col">
  <img src="https://img.shields.io/badge/Python-black?style=flat-square&logo=python&logoColor=White"/>
  <img src="https://img.shields.io/badge/Java-Green?style=flat-square&logo=Java&logoColor=White"/>
  <img src="https://img.shields.io/badge/Android Studio-blue?style=flat-square&logo=androidstudio&logoColor=green"/>
  <img src="https://img.shields.io/badge/Github-black?style=flat-square&logo=github&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetson nano-yellow?style=flat-square&logo=jetsonnano&logoColor=black"/>
</div>
<br>
