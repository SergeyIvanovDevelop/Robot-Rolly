<h1 align="center">
  Robot Rolly RPI3B+ ROS
</h1>

## Robot Rolly RPI3B+ ROS &middot; [![GitHub license](https://img.shields.io/badge/license-CC%20BY--NC--SA%203.0-blue)](./LICENSE) [![Python](https://img.shields.io/badge/python-3.6-red)](https://www.python.org/) [![ROS](https://img.shields.io/badge/platform-ROS-critical)](https://www.ros.org/) [![RPI3B+](https://img.shields.io/badge/rpi-3B%2B-yellow)](https://www.raspberrypi.com/products/raspberry-pi-3-model-b-plus/) [![LinkedIn](https://img.shields.io/badge/linkedin-Sergey%20Ivanov-blue)](https://www.linkedin.com/in/sergey-ivanov-33413823a/) [![Telegram](https://img.shields.io/badge/telegram-%40SergeyIvanov__dev-blueviolet)](https://t.me/SergeyIvanov_dev) ##

This repository branch contains the code and configuration for deploying and running the `rpi3b+` part of the `Robot Rolly` project on a microcomputer [Raspberry Pi 3B+](https://www.raspberrypi.com/products/raspberry-pi-3-model-b- plus/).

This part of the project is built using [ROS](https://www.raspberrypi.com/products/raspberry-pi-3-model-b-plus/) and the [Python](https://www.python. org/) programming language.

## :computer: Getting Started  ##

**Step 1**

1. Go to home directory and clone repository from github: `cd ~ && git clone https://SergeyIvanovDevelop@github.com/SergeyIvanovDevelop/Robot-Rolly` 

**Step 2**<br>

2. Go to the directory of the downloaded repository: `cd ~/Robot-Rolly`, change branch on `rpi3b+-side-ros`, and copy code on `RPI3B+` in dir `ROS_projects` (need to create by command: `mkdir ROS_projects`)

**Step 3**<br>

3. On `RPI3B+` install dependencies: 

```
cd /home/pi/ROS_projects/bash_history && ./dependencies.sh
```

**Step 4**<br>

4. Run project: 

```
cd /home/pi/ROS_projects/workspace/RESURSES/
./myscript.sh 
```

_Note: Along with the code for the `rpi3b+` part of the project, the branch also contains the file [.bash_history](./bash_history/bash_history) containing all the terminal commands needed to deploy and run this part of the project_.

### :bookmark_tabs: Licence ###
Robot Rolly RPI3B+ ROS is [CC BY-NC-SA 3.0 licensed](./LICENSE).
