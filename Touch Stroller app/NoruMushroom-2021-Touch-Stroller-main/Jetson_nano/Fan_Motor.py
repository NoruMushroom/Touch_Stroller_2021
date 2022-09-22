import RPi.GPIO as GPIO
import time

Fan_Motor_A = 6
Fan_Motor_B = 7
GPIO.setmode(GPIO.BOARD)
GPIO.setup(A, GPIO.OUT)
GPIO.setup(B, GPIO.OUT)
