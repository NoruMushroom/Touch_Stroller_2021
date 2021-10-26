import RPi.GPIO as GPIO
import time
Servo_SLeft = 32
Servo_SRight = 33
SERVO_MAX_DUTY = 12
SERVO_MIN_DUTY = 2
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Servo_SLeft, GPIO.OUT)
GPIO.setup(Servo_SRight, GPIO.OUT)
S_SLeft = GPIO.PWM(Servo_SLeft, 50)
S_SRight = GPIO.PWM(Servo_SRight, 50)
