#!/bin/bash
# this is a comment
sleep 4
sudo swapon /dev/mmcblk0p5
sudo hciconfig hci0 piscan
sudo su -c 'cd /home/pi/ROS_projects/workspace && export ROS_MASTER_URI=http://192.168.0.15:11311/ && export ROS_IP=192.168.0.30 && source devel/setup.bash && roslaunch general_programm run_rolly.launch'
  