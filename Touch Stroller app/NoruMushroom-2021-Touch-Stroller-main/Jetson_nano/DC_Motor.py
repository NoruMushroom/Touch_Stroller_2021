import RPi.GPIO as GPIO
import time
Motor_Left_A = 22
Motor_Left_B = 24
Motor_Right_A = 21
Motor_Right_B = 23
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Motor_Left_A, GPIO.OUT)
GPIO.setup(Motor_Left_B, GPIO.OUT)
GPIO.setup(Motor_Right_A, GPIO.OUT)
GPIO.setup(Motor_Right_B, GPIO.OUT)
