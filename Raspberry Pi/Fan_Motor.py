import RPi.GPIO as GPIO
import time
Fan_Motor = 13
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Fan_Motor, GPIO.OUT)

