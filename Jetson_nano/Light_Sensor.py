import RPi.GPIO as GPIO
import time
Light_Sensor = 11
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Light_Sensor, GPIO.IN)


