#! /usr/bin/python

from __future__ import print_function

import rospy
from my_service.srv import FileData, FileDataResponse, FileDataRequest

import sys

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

def file_data_client_request(data_file, format_file, size_file):
	rospy.wait_for_service('file_data')
	try:
		request_object = rospy.ServiceProxy('file_data', FileData)
		responce_object = request_object(data_file, format_file, size_file)
		return responce_object.answer
	except rospy.ServiceException as e:
		print("Service call failed: %s"%e)

def file_data_client(filepath):
	rospy.init_node('publisher_service_py')
	in_file = open(filepath, "rb")
	data = in_file.read() # if you only wanted to read 512 bytes, do .read(512)
	in_file.close()
	format = get_format(filepath)	
	answer = file_data_client_request(data, format, len(data))
	return answer

if __name__ == "__main__":
	# There need made function, which another nodes will be call !!
	filepath = sys.argv[1]
	answer = file_data_client(filepath)
	print(answer)
