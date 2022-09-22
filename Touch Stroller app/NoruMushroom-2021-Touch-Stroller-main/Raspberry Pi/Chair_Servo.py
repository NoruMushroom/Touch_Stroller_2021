import RPi.GPIO as GPIO
import time
Servo_CLeft = 35
Servo_CRight = 12
SERVO_MAX_DUTY = 12
SERVO_MIN_DUTY = 1
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Servo_CLeft, GPIO.OUT)
GPIO.setup(Servo_CRight, GPIO.OUT)
S_CLeft = GPIO.PWM(Servo_CLeft, 50)
S_CRight = GPIO.PWM(Servo_CRight, 50)

