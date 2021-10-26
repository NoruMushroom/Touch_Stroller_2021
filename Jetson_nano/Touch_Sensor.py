import RPi.GPIO as GPIO

Touch_Left = 38
Touch_Right = 37
Touch_Back = 35
GPIO.setmode(GPIO.BOARD)
GPIO.setup(Touch_Left, GPIO.IN)
GPIO.setup(Touch_Right, GPIO.IN)
GPIO.setup(Touch_Back, GPIO.IN)
