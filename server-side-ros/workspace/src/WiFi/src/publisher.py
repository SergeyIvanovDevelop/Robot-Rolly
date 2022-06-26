#! /usr/bin/python3

import rospy
from wifi_command_service.srv import WiFiCommand, WiFiCommandResponse, WiFiCommandRequest
import sys

def wifi_data_send_request(what_is_it, data):
	rospy.wait_for_service('wifi_command_communication')
	try:
		request_object = rospy.ServiceProxy('wifi_command_communication', WiFiCommand)
		responce_object = request_object(what_is_it, data)
		return responce_object.ok
	except rospy.ServiceException as e:
		print("Service call failed: %s"%e)

def wifi_data_send(what_is_it, filepath):
	data = 0
	if what_is_it == "command":
		in_file = open(filepath, "rb") # opening for [r]eading as [b]inary
		data = in_file.read() # if you only wanted to read 512 bytes, do .read(512)
		in_file.close()
	else:
		in_file = open(filepath, "rb") # opening for [r]eading as [b]inary
		data = in_file.read() # if you only wanted to read 512 bytes, do .read(512)
		in_file.close()
	rospy.init_node('publisher_wifi_command_service_py')
	ok = wifi_data_send_request(what_is_it, data)
	return ok

if __name__ == "__main__":
    what_is_it = sys.argv[1]
    filepath = sys.argv[2]
    answer = wifi_data_send(what_is_it, filepath)
    print(answer)
