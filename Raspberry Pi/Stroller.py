import RPi.GPIO as GPIO
from Sunroof_Servo import *
from Chair_Servo import *
from Signal import *
from Temp_Sensor import *
from Light_Sensor import *
from Fan_Motor import *
Angle = 0
Choice = 0
Data = 0
Duty = 0
Temperature = 0
Humidity = 0
def Receive_Data():
    global Data
    Value = ser.readline()
    Data = Value.decode('utf-8')
    print(Data)
def Send_Data():
    ser.write(str(Temperature).encode('utf-8'))
    ser.write(str(Humidity).encode('utf-8'))
def Data_Split():
    global Angle, Choice
    length = len(Data)
    if length >= 2:
       Choice = Data[length - 1]
       Angle = Data[0:length - 1]
def Servo_Angle(Angle):
    global Duty
    S_SLeft.start(0)
    S_SRight.start(0)
    if Angle > 180:
        Angle = 180
    if Angle <= 0:
        Angle = 0
    Duty = SERVO_MIN_DUTY+(Angle*(SERVO_MAX_DUTY-SERVO_MIN_DUTY)/180.0)
def Sunroof_Ctl():
    if Choice == '4':
       Servo_Angle(0)
       S_SRight.ChangeDutyCycle(Duty)
       S_SLeft.ChangeDutyCycle(13-Duty)
       time.sleep(0.3)
    if Choice == '5':
       Servo_Angle(90)
       S_SRight.ChangeDutyCycle(Duty)
       S_SLeft.ChangeDutyCycle(13-Duty)
       time.sleep(0.3)
def Chair_Ctl():
    S_CLeft.start(0)
    S_CRight.start(0)
    Servo_Angle(int(Angle))
    S_CRight.ChangeDutyCycle(Duty)
    S_CLeft.ChangeDutyCycle(13-Duty)
    time.sleep(0.3)
def Fan_Motor_Ctl():
    if Choice == '1':
       if Temperature >= 25 and Humidity >= 50:
               GPIO.output(Fan_Motor, GPIO.HIGH)
       else:
               GPIO.output(Fan_Motor, GPIO.LOW)
    else:
       if Choice == '6':
                GPIO.output(Fan_Motor, GPIO.LOW)
       if Choice == '7':
                GPIO.output(Fan_Motor, GPIO.HIGH)
    time.sleep(0.3)
def Temp_Data():
    global Temperature, Humidity
    T_Data = sensor.read()
    Temperature = T_Data['temp_c']
    Humidity = T_Data['humidity']
try:
   while True:
         Receive_Data()
         Data_Split()
         Temp_Data()
         Send_Data()
         Sunroof_Ctl()
         print(Angle)
         Chair_Ctl()
         Fan_Motor_Ctl()
except ValueError:
       pass
except KeyboardInterrupt:
       GPIO.cleanup()
