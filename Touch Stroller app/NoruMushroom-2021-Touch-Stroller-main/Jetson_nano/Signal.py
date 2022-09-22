import time
import serial

ser1 = serial.Serial(
        port='/dev/ttyTHS1', #Replace ttyS0 with ttyAM0 for Pi1,Pi2,Pi0
        baudrate = 115200,
        parity=serial.PARITY_NONE,
        stopbits=serial.STOPBITS_ONE,
        bytesize=serial.EIGHTBITS,
        timeout = 1)
#counter= 0
#while True:
      #ser.write(str.encode(str(counter)))
      #value = ser.readline()
      #print("Raspberry Pi From:", value.decode('utf-8'))
