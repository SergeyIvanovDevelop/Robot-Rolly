#!/bin/bash
xterm -e "cd /home/sergey/ROS/Projects_ROS/workspace/ && source devel/setup.bash && export ROS_IP=192.168.0.15 && roslaunch run_java_servers run_server_part_robot.launch"
