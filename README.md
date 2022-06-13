<h1 align="center">
  Robot Rolly Server-part ROS
</h1>

## Robot Rolly Server-part ROS &middot; [![GitHub license](https://img.shields.io/badge/license-CC%20BY--NC--SA%203.0-blue)](./LICENSE) [![Java](https://img.shields.io/badge/Java-SE8-blue)](https://www.java.com/) [![Python](https://img.shields.io/badge/python-3.6-red)](https://www.python.org/) [![ROS](https://img.shields.io/badge/platform-ROS-critical)](https://www.ros.org/) [![LinkedIn](https://img.shields.io/badge/linkedin-Sergey%20Ivanov-blue)](https://www.linkedin.com/in/sergey-ivanov-33413823a/) [![Telegram](https://img.shields.io/badge/telegram-%40SergeyIvanov__dev-blueviolet)](https://t.me/SergeyIvanov_dev) ##

This branch of the repository contains the code and configuration for running the server side of the `Rolly` assistant robot platform.

The server code is based on [ROS](https://www.ros.org/) and the programming language [Python](https://www.python.org/) and [Java](https://www.java.com/).

## :computer: Getting Started  ##

**Step 1**

1. Go to home directory and clone repository from github: `cd ~ && git clone https://SergeyIvanovDevelop@github.com/SergeyIvanovDevelop/Robot-Rolly` 

**Step 2**<br>

2. Go to the directory of the downloaded repository: `cd ~/Robot-Rolly`, change branch on `server-side-ros`, and copy code to dir `~/ROS_projects` (need to create by command: `mkdir ~/ROS_projects`)

**Step 3**<br>

3. Install dependencies: 

```
cd ~/ROS_projects/bash_history && ./dependencies.sh
```

**Step 4**<br>

4. Run project: 

```
cd ~/ROS_projects/workspace/RESURSES/
./myscript.sh 
```

_Note: the [.bash_history](./bach_history/bash_history) file is attached to the server code with the history of all commands in the `Ubuntu 16.04 LTS` OS required to deploy the server part of the `Robot Rolly` project._<br>

_Note: Deployment to Virtual Box is recommended._

### :bookmark_tabs: Licence ###
Robot Rolly Server-part ROS is [CC BY-NC-SA 3.0 licensed](./LICENSE).
