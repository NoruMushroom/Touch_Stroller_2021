import RPi.GPIO as GPIO
import time
Servo_CLeft = 32
Servo_CRight = 33
SERVO_MAX_DUTY = 12
SERVO_MIN_DUTY = 1
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Servo_CLeft, GPIO.OUT)
GPIO.setup(Servo_CRight, GPIO.OUT)
S_CLeft = GPIO.PWM(Servo_CLeft, 50)
S_CRight = GPIO.PWM(Servo_CRight, 50)
def setServoPos(degree):
    if degree > 180:
          degree = 180
    if degree <= 0:
          degree = 0
    duty = SERVO_MIN_DUTY+(degree*(SERVO_MAX_DUTY-SERVO_MIN_DUTY)/180.0)
    print("Degree: {} to {}(Duty)".format(degree, duty))
    S_CRight.ChangeDutyCycle(duty)
    S_CLeft.ChangeDutyCycle(13 - duty)
    time.sleep(2)
try:
    while True:
          S_CLeft.start(0)
          S_CRight.start(0)
          setServoPos(90)
          setServoPos(0)
          setServoPos(90)
except KeyboardInterrupt:
       GPIO.cleanup()
