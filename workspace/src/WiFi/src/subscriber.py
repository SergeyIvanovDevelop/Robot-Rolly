#! /usr/bin/python3

import rospy
from wifi_command_service.srv import WiFiCommand, WiFiCommandResponse, WiFiCommandRequest

############ MANIPULATION_SETTING ############################
##############################################################

import RPi.GPIO as GPIO

# --------------- Settings of motors BEGIN -------------------

Motor1A = 18
Motor1B = 16
Motor1E = 22

Motor2A = 36
Motor2B = 37
Motor2E = 11

Ligth1 = 13
Ligth2 = 15

GPIO.setmode(GPIO.BOARD)

GPIO.setup(Motor1A, GPIO.OUT)
GPIO.setup(Motor1B, GPIO.OUT)
GPIO.setup(Motor1E, GPIO.OUT)

GPIO.setup(Motor2A, GPIO.OUT)
GPIO.setup(Motor2B, GPIO.OUT)
GPIO.setup(Motor2E, GPIO.OUT)

GPIO.setup(Ligth1, GPIO.OUT)
GPIO.setup(Ligth2, GPIO.OUT)

GPIO.output(Motor1E,GPIO.LOW)
GPIO.output(Motor2E,GPIO.LOW)

GPIO.output(Ligth1,GPIO.LOW)
GPIO.output(Ligth2,GPIO.LOW)

print("Ports has been initialized")

pwm_1 = GPIO.PWM(Motor1E,500)
pwm_1.start(0)

pwm_2 = GPIO.PWM(Motor2E,500)
pwm_2.start(0)

GPIO.output(Motor1E,GPIO.LOW)
GPIO.output(Motor2E,GPIO.LOW)


# --------------- Settings of motors END -------------------


GPIO_TRIGGER = 29
GPIO_ECHO = 31

GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)

def SetSpeedMotors(speed):
    pwm_1.ChangeDutyCycle(speed)
    pwm_2.ChangeDutyCycle(speed)

def turn_left(x = 0.8, speed = 100):
    SetSpeedMotors(speed)
    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.HIGH)
    GPIO.output(Motor2E,GPIO.HIGH)

    time.sleep(x)

    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.LOW)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.LOW)

    print("turning left to " + str(x) + " min_steps")

def turn_rigth(x = 0.8, speed = 100):
    SetSpeedMotors(speed)
    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.HIGH)
    GPIO.output(Motor1E,GPIO.HIGH)

    time.sleep(x)

    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.LOW)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.LOW)

    print("turning rigth to " + str(x) + " min_steps")

def forward_ride(x = 1, speed = 100):
    SetSpeedMotors(speed)
    GPIO.output(Motor1A,GPIO.HIGH)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.HIGH)

    GPIO.output(Motor2A,GPIO.HIGH)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.HIGH)

    time.sleep(x)

    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.LOW)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.LOW)

    print("forward ride to " + str(x) + " min_steps")

def back_ride(x = 1, speed = 100):
    SetSpeedMotors(speed)
    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.HIGH)
    GPIO.output(Motor1E,GPIO.HIGH)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.HIGH)
    GPIO.output(Motor2E,GPIO.HIGH)

    time.sleep(x)

    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.LOW)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.LOW)

    print("back ride to " + str(x) + " min_steps")

def returning(x = 0.8, speed = 100):
    SetSpeedMotors(speed)
    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.HIGH)
    GPIO.output(Motor2E,GPIO.HIGH)

    GPIO.output(Motor1A,GPIO.HIGH)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.HIGH)

    time.sleep(x)

    GPIO.output(Motor1A,GPIO.LOW)
    GPIO.output(Motor1B,GPIO.LOW)
    GPIO.output(Motor1E,GPIO.LOW)

    GPIO.output(Motor2A,GPIO.LOW)
    GPIO.output(Motor2B,GPIO.LOW)
    GPIO.output(Motor2E,GPIO.LOW)

    print("returning")

###############################################################

def get_format(filename):
	format = ""
	count = 0
	c = filename[count]
	while c != '.':
		if count == len(filename):
			break
		count = count + 1
		c = filename[count]
	if c=='.':
		format = filename[count+1:]
	else:
		format = "bin"
	return format

from threading import Thread
import os
import time
class Reboot_Thread(Thread):
    def __init__(self):
        Thread.__init__(self)

    def run(self):
        print("Rebooting...")
        time.sleep(3)
        os.system("reboot")

def handler_wifi_command_recieve(req):
    print("Receive wifi_message  [what_is_it: %s; data: %s]"%(req.what_is_it, req.data))
    print("req.what_is_it[:5] = ", req.what_is_it[:5])
    if req.what_is_it[0] == 'f':
        filename = req.what_is_it[5:]
        format = get_format(filename)
        new_filename = "recieved_wifi_file." + format
        out_file = open(new_filename, "wb")
        out_file.write(req.data)
        out_file.close()
        print("Received_file: ", new_filename)
        return WiFiCommandResponse("ok")

    if req.what_is_it == "command":
        format = "txt"
        new_filename = "recieved_wifi_file." + format
        out_file = open(new_filename, "wb")
        out_file.write(req.data)
        out_file.close()
        in_file = open(new_filename, "r")
        data = in_file.read()
        in_file.close()
        print("data = ", data)
        if data == "forward":
            forward_ride(0.5)
            return WiFiCommandResponse("ok")
        if data == "rigth":
            turn_rigth(0.3)
            return WiFiCommandResponse("ok")
        if data == "left":
            turn_left(0.3)
            return WiFiCommandResponse("ok")
        if data == "back":
            back_ride(0.5)
            return WiFiCommandResponse("ok")
        if data == "reset":
            r_t = Reboot_Thread()
            r_t.start()
            return WiFiCommandResponse("ok")
        # For config-command
        out_file = open("/home/pi/ROS_projects/workspace/RESURSES/configuration.txt", "w")
        out_file.write(data)
        out_file.close()
        return WiFiCommandResponse("ok")

def receive_wifi_commands():
	rospy.init_node('subscriber_wifi_command_service_py')
	subscriber = rospy.Service('wifi_command_communication', WiFiCommand, handler_wifi_command_recieve)
	print("Ready to WiFiCommandHandler")
	rospy.spin()

if __name__ == "__main__":
    receive_wifi_commands()
