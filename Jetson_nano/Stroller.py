import RPi.GPIO as GPIO
import time
import select
from bluetooth import *
from LED import *
from Touch_Sensor import *
from Gyro_Sensor import *
from DC_Motor import *
from Light_Sensor import *
from Signal import *
from Detection import *
Auto = 0
Choice = 0
Connection_Status = 0
Angle = 0
Data = 0
Temperature = 0
Humidity = 0
Face = ""
Reuslt = ""
Cam = cv2.VideoCapture(0, cv2.CAP_V4L)
def Receive_Data():
    global Data
    Value = ser1.readline()
    Data = Value.decode('utf-8')
def Send_Data():
    ser1.write(str(Angle).encode('utf-8'))
    ser1.write(str(Choice).encode('utf-8'))
def Data_Split():
    global Temperature, Humidity
    length = len(Data)
    if length >= 4:
       Temperature = Data[0:length - 2]
       Humidity = Data[length - 2:]
def Data_Combine():
    global Result
    Combine_str = str(Temperature)+','+str(Humidity)+','+Face+'\n'
    Result = Combine_str.encode('utf-8')
def LED_Ctl():
     if Auto: #자동일경
          Light_Value = GPIO.input(Light_Sensor)
          if Light_Value:
                GPIO.output(LED, GPIO.LOW)
          else:
                GPIO.output(LED, GPIO.HIGH)
     else: #수동일 경우
          if Choice == 2:
                GPIO.output(LED, GPIO.LOW)
          if Choice == 3:
                GPIO.output(LED, GPIO.HIGH)
def Touch_Ctl():
    #GPIO.setup(Touch_Left, GPIO.IN)
    #GPIO.setup(Touch_Right, GPIO.IN)
    Touch_Left_Value = GPIO.input(Touch_Left)
    Touch_Right_Value = GPIO.input(Touch_Right)
    return Touch_Left_Value or Touch_Right_Value
def Motor_Ctl():
    if Touch_Ctl():
       GPIO.output(Motor_Left_A, GPIO.LOW)
       GPIO.output(Motor_Left_B, GPIO.HIGH)
       GPIO.output(Motor_Right_A, GPIO.LOW)
       GPIO.output(Motor_Right_B, GPIO.HIGH)
    else:
       GPIO.output(Motor_Left_A, GPIO.LOW)
       GPIO.output(Motor_Left_B, GPIO.LOW)
       GPIO.output(Motor_Right_A, GPIO.LOW)
       GPIO.output(Motor_Right_B, GPIO.LOW)
def Gyro_Value():
       global Angle
       Angle = Cal_Angle()
       return Angle
def Video():
    x_, img = Cam.read()
    grayscale = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    final = detection(grayscale, img)
    cv2.imshow('Video', final)
    cv2.waitKey(1)
def detection(grayscale, img):
    global Face
    Detection = ""
    Detection_Face = ""
    Detection_Smile = ""
    face = cascade_face.detectMultiScale(grayscale, 1.3, 5)
    for (x_face, y_face, w_face, h_face) in face:
        cv2.rectangle(img, (x_face, y_face), (x_face+w_face, y_face+h_face), (255, 130, 0), 2)
        ri_grayscale = grayscale[y_face:y_face+h_face, x_face:x_face+w_face]
        ri_color = img[y_face:y_face+h_face, x_face:x_face+w_face]
        eye = cascade_eye.detectMultiScale(ri_grayscale, 1.2, 18)
        if(len(eye) >= 1):
           for (x_eye, y_eye, w_eye, h_eye) in eye:
                cv2.rectangle(ri_color,(x_eye, y_eye),(x_eye+w_eye, y_eye+h_eye), (0, 180, 60), 2)
                Detection_Face = "Wake Up"
        else:
                Detection_Face = "Sleep"
        smile = cascade_smile.detectMultiScale(ri_grayscale, 1.7, 20)
        for (x_smile, y_smile, w_smile, h_smile) in smile:
            cv2.rectangle(ri_color,(x_smile, y_smile),(x_smile+w_smile, y_smile+h_smile), (255, 0, 130), 2)
            Detection_Smile = "Smile"
    if len(face) == 0:
          Face ="Sleep"
    if Detection_Face or Detection_Smile:
          if Detection_Face == "Wake Up":
                 if Detection_Smile:
                     Face = Detection_Face+" "+Detection_Smile
                 else:
                     Face = Detection_Face
          if Detection_Face == "Sleep":
                 Face = Detection_Face
    if not Detection_Face and Detection_Smile:
                 Face = "No Detection"
    return img
try:
   while True:
      Video()
      uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
      server_sock=BluetoothSocket( RFCOMM )
      server_sock.bind(('',PORT_ANY))
      server_sock.listen(1)
      port = server_sock.getsockname()[1]
      advertise_service( server_sock, "BtChat",service_id = uuid,service_classes = [ uuid, SERIAL_PORT_CLASS ],profiles = [ SERIAL_PORT_PROFILE ] )
      client_sock, client_info = server_sock.accept()
      while True:
            if client_sock:
               Connection_Status = 1
               if Connection_Status:
                    Receive = client_sock.recv(1024)
                    if int(Receive) == 1: #자동모드
                       Auto = 1
                       while True:
                             if int(Receive) != 0:
                                ready = select.select([client_sock], [], [], 0.1)
                                if ready[0]:
                                   Receive = client_sock.recv(1024)
                                   Choice = int(Receive)
                                Video()
                                LED_Ctl()
                                Motor_Ctl()
                                if(flag >100):
                                   flag=0
                                   continue
                                Gyro_Value()
                                Send_Data()
                                Receive_Data()
                                Data_Split()
                                Data_Combine()
                                client_sock.send(Result)
                             else:
                                cv2.destroyAllWindows()
                                break
                    if int(Receive) == 0: #수동모드
                       Auto = 0
                       while True:
                             if int(Receive) != 1:
                                ready = select.select([client_sock], [], [], 0.1)
                                if ready[0]:
                                   Receive = client_sock.recv(1024)
                                   Choice = int(Receive)
                                Video()
                                LED_Ctl()
                                Motor_Ctl()
                                if(flag >100):
                                   flag=0
                                   continue
                                Gyro_Value()
                                print(Angle)
                                print(Choice)
                                Send_Data()
                                Receive_Data()
                                Data_Split()
                                Data_Combine()
                                client_sock.send(Result)
                             else:
                                cv2.destroyAllWindows()
                                break
except ValueError:
       pass
except KeyboardInterrupt:
       GPIO.cleanup()
       server_sock.close()
       Cam.release()
       cv2.destroyAllWindows()

