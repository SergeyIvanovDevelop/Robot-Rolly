#! /usr/bin/python3

import rospy
import os
from threading import Thread
class First_Thread(Thread):
    def __init__(self):
        Thread.__init__(self)
    
    def run(self):
        my_command_1 = "java -cp /home/sergey/ROS/Projects_ROS/workspace/Java/ server_java"
        os.system(my_command_1)
        print("Java_Server_has_been_started # 1 !")

class Second_Thread(Thread):
    def __init__(self):
        Thread.__init__(self)
    
    def run(self):
        my_command_1 = "java -cp /home/sergey/ROS/Projects_ROS/workspace/Java/ server_java_3"
        os.system(my_command_1)
        print("Java_Server_has_been_started # 2 !")

if __name__ == "__main__":
    rospy.init_node('Run_Java_Servers_py')
    f_t = First_Thread()
    f_t.start()
    s_t = Second_Thread()
    s_t.start()
    print("Java_Server_has_been_started")
    f_t.join()
    s_t.join()
    print("Java_Server_has_been_finished")
    rospy.spin()


