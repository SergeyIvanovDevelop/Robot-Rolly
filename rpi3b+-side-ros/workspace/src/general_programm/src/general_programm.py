#! /usr/bin/python3
from __future__ import print_function
import rospy

import os
import time
import cv2
import datetime
from time import gmtime, strftime

############ MANIPULATION_SETTING ############################
# ДЛЯ РАЗНЫХ ХОДОВЫХ_БАЗ (КВАДРОКОПТЕР, ВОДЯНОЙ РОБОТ, ГУСЕНИЧНЫЙ, КОЛЕСНЫЙ нужно менять этот кусок кода (назовем это драйвера))
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

parametrs_configuration = []
print("Printing configurations: ")
robot_config = open('/home/pi/ROS_projects/workspace/RESURSES/configuration.txt','r')
for line in robot_config:
    parametrs_configuration.append(line)
    print(line)
robot_config.close()
voice = parametrs_configuration[0]
voice_m =""
if (voice[0]=="1"):
    voice_m = "Anna"
if (voice[0]=="0"):
    voice_m = "Aleksandr"
internet_login = parametrs_configuration[1]
internet_password = parametrs_configuration[2]
auto_inet = parametrs_configuration[3]
auto_move = parametrs_configuration[4]
Video_time = parametrs_configuration[5]
Audio_time = parametrs_configuration[6]

robot_locked = True

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

###################### For Bluetooth ##########################
import copy
import bluetooth
from bluetooth import *
###################### For Bluetooth ##########################

from threading import Thread

class Bluetooth_Thread(Thread):
    def __init__(self):
        Thread.__init__(self)

    def run(self):
        Bluetooth_Manipulation()
        print("Bluetooth_Manipulation working!")


class Wi_Fi_Thread(Thread):
    def __init__(self,login,password):
        Thread.__init__(self)
        self.login = login
        self.password = password

    def run(self):
        Wi_Fi_Connect(self.login,self.password)


def Wi_Fi_Connect(login,password):
    new_login = []
    for i in range(0,len(login)-1):
        new_login.append(login[i])
    new_password = []
    for i in range(0,len(password)-1):
        new_password.append(password[i])
    print("Trying connect to Wi-Fi Access Point: login =" + listToString(new_login)+"L" + " password =" + listToString(new_password) + "p")
    os.system("sudo wpa_passphrase " + listToString(new_login) + " " + listToString(new_password) + " > /etc/wpa_supplicant.conf")
    time.sleep(5)
    os.system("sudo wpa_supplicant -iwlan0 -c /etc/wpa_supplicant.conf")
    print("Trying coonect has been finished")


def Bluetooth_Manipulation():
    print("Bluetooth manipulation")
    # coonect with Bluetooth client
    PORT_ANY = 1
    server_sock=BluetoothSocket( RFCOMM )
    server_sock.bind(("",PORT_ANY))
    server_sock.listen(1)
    port = server_sock.getsockname()[1]
    uuid = "8ce255c0-200a-11e0-ac64-0800200c9a66"
    print(SERIAL_PORT_PROFILE)
    bluetooth.advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ],
                  )
    print("Waiting for connection on RFCOMM channel %d" % port)
    client_sock, client_info = server_sock.accept()
    print("Accepted connection from ", client_info)
    # listening Bluetooth Socket
    command_0 = client_sock.recv(1024)
    command = command_0.decode("utf-8")
    while (command != "quit"):
        print("recieve command = " + command)
        if (command == "forward"):
            forward_ride(0.5)

        if (command == "back"):
            back_ride(0.5)

        if (command == "left"):
            turn_left(0.3)

        if (command == "rigth"):
            turn_rigth(0.3)
        if (command == "reset"):
            print("Rebooting...")
            time.sleep(3)
            os.system("reboot")
        if (command == "returning"):
            returning()
        command_0 = client_sock.recv(1024)
        command = command_0.decode("utf-8")

    print("End of manipulation from Android with Bluetooth")
    client_sock.close()
    server_sock.close()
    print("all done")

def delete_file(filename):
    os.remove(filename)

# ---------------- OLD_FUNCTIONS_-------------------------------#
# ------------ ### --------- ### --------- ### --------- ### ----

def get_distance():
    GPIO.output(GPIO_TRIGGER, GPIO.HIGH)

    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER, GPIO.LOW)

    StartTime = time.time()
    StopTime = time.time()


    while GPIO.input(GPIO_ECHO) == 0:
        StartTime = time.time()

    while GPIO.input(GPIO_ECHO) == 1:
        StopTime = time.time()

    TimeElapsed = StopTime - StartTime

    distance = (TimeElapsed * 34300) / 2
    print("Measured Distance = %.1f cm" % distance)

    return distance

# Требует доработки !!!
def patrul():
    while True:
        dist = get_distance()
        if dist < 7:
            break
        if dist > 30:
            forward_ride(0.3)
        else:
            turn_left(0.2)
        time.sleep(1)

# Требует доработки !!!
def follow_me():
    while True:
        dist = get_distance()
        print("dist = ",dist)
        if dist < 7:
            break
        if dist < 30:
            forward_ride(0.2)
        time.sleep(1)

def say_news(answer):
	answer_list = answer.split('\n')
	print("answer_list: ", answer_list)
	Say("Читаю последние новости", voice_m)
	count_news = 0
	for answ in answer_list:
		count_news = count_news + 1
		speech_string = "Новость номер " + str(count_news)
		Say(answ, voice_m)
	return


def say_deals(answer):
	answer_list = answer.split('\n')
	print("answer_list: ", answer_list)
	Say("Читаю дела на сегодня", voice_m)
	count_deals = 0
	for answ in answer_list:
		count_deals = count_deals + 1
		speech_string = "Дело номер " + str(count_deals)
		Say(answ, voice_m)
	return

def say_wether(answer):
	answer_list = answer.split('\n')
	print("answer_list: ", answer_list)
	Say("Погода на сегодня", voice_m)
	count = 0
	for answ in answer_list:
		Say(answ, voice_m)
	return

def Say(string_speak, voice_m):
    my_command = "echo \"" + string_speak + "\" | RHVoice-test -p " + voice_m
    os.system(my_command)
    print("Saying: ", string_speak)
    print("Commsnd SAY completed successfuly!")

def record_audio(time_seconds, file_name):
    print(" Audio recording ...")
    my_command = "arecord -B 2048 -f dat -r 16000 -d " + str(time_seconds) + " -c 1 " + str(file_name)
    os.system(my_command)
    print(" Recording finished")

def light(status):
    if status == "on":
        #  ---------- Ligth ON ------
        GPIO.output(Ligth1,GPIO.LOW)
        GPIO.output(Ligth2,GPIO.HIGH)
        #  ---------- Ligth ON ------
        print("light_ON")
        return
    if status == "off":
        #  ---------- Ligth OFF ------
        GPIO.output(Ligth1,GPIO.LOW)
        GPIO.output(Ligth2,GPIO.LOW)
        #  ---------- Ligth OFF ------
        print("light_OFF")
        return
    print("Error parametr 'status': ", status)
    return

def listToString(s):
    str1 = ""
    for ele in s:
        str1 += ele
    return str1

def Tell_time():
    time = datetime.datetime.time(datetime.datetime.now())
    time_str = strftime("Сейчас:  %H: %M ", gmtime())
    Say(time_str,voice_m)

def Take_Photo(device, filename):
    cap = cv2.VideoCapture(device)
    print("taking photo")
    ret, frame = cap.read()
    cv2.imwrite(filename, frame)
    cap.release()

def Shoot_Video(device, video_time, filename):
    print("Start shutting video")
    print("Video_time = ", video_time)
    Video_time_1 = video_time
    Video_time_1 = Video_time_1 * 20
    count_cadrs = Video_time_1 #(one min.) will be reading from configuration.txt
    count = 0;

    print("count_cadrs = ", count_cadrs)
    cap = cv2.VideoCapture(device)
    fourcc = cv2.VideoWriter_fourcc(*'XVID')
    #fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(filename, fourcc, 20.0,(640,480))
    while(cap.isOpened()):
        if count == count_cadrs:
            break
        ret, frame = cap.read()
        if ret == True:
            out.write(frame)
            count = count +1;
        else:
            break
    cap.release()
    out.release()
    print("Shutting Video has been finished")

def bytes_to_int(bytes):
    result = 0
    for b in bytes:
        result = result * 256 + int(b)
    return result

def int_to_bytes(value, length):
    result = []
    for i in range(0,length):
        result.append(value >> (i*8) & 0xff)
    result.reverse()
    return result

# function recieve_file_from_Android
def RecieveFile(client_sock, filename):
    print("Recieve file from Android")
    # Recieving list_people.txt
    total = 0;
    total1 = 0;
    data_length = client_sock.recv(1)
    total1 = total1 + 1

    while total1 < 4:
        data_length0 = client_sock.recv(1)
        if len(data_length0) == 0: break
        total1 = total1 + 1
        data_length = data_length + data_length0

    print("received length of field = [%s]" % bytes_to_int(data_length))
    length = bytes_to_int(data_length)
    data1 = client_sock.recv(1024)
    total = total + len(data1)

    while total < length:
        data = client_sock.recv(1024)
        total = total + len(data)
        data1 = data1 + data
        print("length of received data = [%s]" % len(data))
        print("received data = [%s]" % data)

    out_file = open(filename, "wb") # open for [w]riting as [b]inary
    out_file.write(data1)
    out_file.close()
    print("File #1 recieved")

def Recieve_accept(client_sock):
    total1 = 0;
    data_length = client_sock.recv(1)
    total1 = total1 + 1
    while total1 < 4:
        data_length0 = client_sock.recv(1)
        if len(data_length0) == 0: break
        total1 = total1 + 1
        data_length = data_length + data_length0
    print("received of accept = [%s]" % bytes_to_int(data_length))

# function of update_robot
def update():
    print("Update Robot")
    PORT_ANY = 1
    server_sock=BluetoothSocket( RFCOMM )
    server_sock.bind(("",PORT_ANY))
    server_sock.listen(1)
    port = server_sock.getsockname()[1]
    uuid = "8ce255c0-200a-11e0-ac64-0800200c9a66"
    print(SERIAL_PORT_PROFILE)
    bluetooth.advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ],
                 )

    print("Waiting for connection on RFCOMM channel %d" % port)
    client_sock, client_info = server_sock.accept()
    print("Accepted connection from ", client_info)
    try:
        while True:
            SendFile(client_sock, "/home/pi/Documents/count_video.txt")
            time.sleep(3)
            Recieve_accept(client_sock)
            robot_config = open('/home/pi/Documents/count_video.txt','r')
            parametrs = []
            for line in robot_config:
                parametrs.append(line)
                print(line)
            robot_config.close()
            if (int(parametrs[0]) > 0):
                for i in range(1, int(parametrs[0])+1):
                    SendFile(client_sock, "/home/pi/Documents/video/" + str(i) +".avi")
                    Recieve_accept(client_sock)
                    time.sleep(1)
                    os.remove("/home/pi/Documents/video/" + str(i) +".avi")
                robot_config = open('/home/pi/Documents/count_video.txt','w')
                robot_config.write("0")
                robot_config.close()
                time.sleep(3)
            SendFile(client_sock, "/home/pi/Documents/count_audio.txt")
            Recieve_accept(client_sock)
            time.sleep(3)
            robot_config = open('/home/pi/Documents/count_audio.txt','r')
            parametrs = []
            for line in robot_config:
                parametrs.append(line)
                print(line)
            robot_config.close()
            if (int(parametrs[0]) > 0):
                for i in range(1, int(parametrs[0])+1):
                    SendFile(client_sock, "/home/pi/Documents/audio/" + str(i) +".wav")
                    Recieve_accept(client_sock)
                    time.sleep(3)
                    os.remove("/home/pi/Documents/audio/" + str(i) +".wav")
                robot_config = open('/home/pi/Documents/count_audio.txt','w')
                robot_config.write("0")
                robot_config.close()
            time.sleep(3)
            SendFile(client_sock, "/home/pi/Documents/count_photo.txt")
            Recieve_accept(client_sock)
            time.sleep(3)
            robot_config = open('/home/pi/Documents/count_photo.txt','r')
            parametrs = []
            for line in robot_config:
                parametrs.append(line)
                print(line)
            robot_config.close()
            if (int(parametrs[0]) > 0):
                for i in range(1, int(parametrs[0])+1):
                    SendFile(client_sock, "/home/pi/Documents/photo/" + str(i) +".jpg")
                    Recieve_accept(client_sock)
                    time.sleep(3)
                    os.remove("/home/pi/Documents/photo/" + str(i) +".jpg")
                robot_config = open('/home/pi/Documents/count_photo.txt','w')
                robot_config.write("0")
                robot_config.close()
            time.sleep(3)
            time.sleep(3)
            SendFile(client_sock, "/home/pi/Documents/map.jpg")
            Recieve_accept(client_sock)
            time.sleep(6)
            RecieveFile(client_sock,"/home/pi/Documents/count_people.txt")
            print("recieved count_people from Android")
            RecieveFile(client_sock,"/home/pi/Documents/list_people.txt")
            print("recieved list_people from Android")
            RecieveFile(client_sock,"/home/pi/Documents/trening.txt")
            print("recieved trening from Android")
            RecieveFile(client_sock,"/home/pi/Documents/deals.txt")
            print("recieved deals from Android")
            RecieveFile(client_sock,"/home/pi/Documents/configuration.txt")
            print("recieved configuration from Android")
            with open("/home/pi/Documents/count_people.txt") as file_in:
                lines = []
                for line in file_in:
                    lines.append(line)
                    print("count_of_people = " + str(int(line)))
                for i in range(1,int(lines[0])+1):
                    filename = "/home/pi/Documents/" + str(i) + ".jpg"
                    RecieveFile(client_sock,filename)
                print("recieved "+ str(int(line)) + " images from Android")
            break
    except IOError:
        pass
    print("disconnected")
    client_sock.close()
    server_sock.close()
    print("all done")

# ---------------- THINKING_ABOUT_IT-------------------------------#


# ------------ ### --------- ### --------- ### --------- ### ----
# ---------------- OLD_FUNCTIONS_-------------------------------#

def biometric_identification():
	filename = "/home/pi/ROS_projects/workspace/RESURSES/biometric_identification.jpg"
	Take_Photo(0,filename)
	my_command = "rosrun client_server publisher.py " + filename
	answer=os.popen(my_command).read()
	delete_file(filename)
	if answer == "NOT_FOUND":
		Say("Не найдено ни одного человека",voice_m)
		Say("Пожалуйста, помотрите в камеру и попытайтесь еще раз",voice_m)
		return False
	if answer == "UNKNOW":
		Say("Ни один из найденных людей не имеет доступа к управлению",voice_m)
		Say("Если доступ был Вам предоставлен, попытайтесь еще раз",voice_m)
		Say("Если доступ не был Вам предоставлен, обратитесь к оператору",voice_m)
		return False
	speech_string = "Привет " + answer
	Say(speech_string, voice_m)
	global robot_locked
	robot_locked = False
	print("robot_locked = ", robot_locked)    
	return True

def analyse_answer(answer):
	if robot_locked == True:
		Say("Пройдите биометрическую идентификацию личности",voice_m)
		Say("Посмотрите в камеру: ",voice_m)
		Say("Три",voice_m)
		time.sleep(1)
		Say("Два",voice_m)
		time.sleep(1)
		Say("Один",voice_m)
		time.sleep(1)
		access = biometric_identification()
		if access == False:
			Say("Идентификация не пройдена",voice_m)
			return
			
	# Выполнение нужной функции
	answer = answer[:-1]
	if ((str(answer) == "прочитай последние новости") or (str(answer) == "последние новости")):
		filename = "/home/pi/ROS_projects/workspace/RESURSES/tell_news.txt" # Это и есть файл-команда
		my_command = "rosrun client_server publisher.py " + filename
		answer=os.popen(my_command).read()
		say_news(answer)
		return
	if ((str(answer) == "напомни дела на сегодня") or (str(answer) == "прочитай напоминания на сегодня") or (str(answer) == "напоминания на сегодня") or (str(answer) == "дела на сегодня")):
		filename = "/home/pi/ROS_projects/workspace/RESURSES/tell_deals.txt"
		my_command = "rosrun client_server publisher.py " + filename
		answer=os.popen(my_command).read()
		say_deals(answer)
		return
	if ((str(answer) == "скажи погоду на сегодня") or (str(answer) == "погоду на сегодня") or (str(answer) == "погоду на")):
		filename = "/home/pi/ROS_projects/workspace/RESURSES/tell_wether.txt" # Это и есть файл-команда
		my_command = "rosrun client_server publisher.py " + filename
		answer=os.popen(my_command).read()
		say_wether(answer)
		return
	if ((str(answer) == "скажи сколько время") or (str(answer) == "скажи сколько времени") or (str(answer) == "скажи времени") or (str(answer) == "скажи время")):
		Tell_time()
		return
	if ((str(answer) == "следуй за мной") or (str(answer) == "едь за мной") or (str(answer) == "за мной")):
		Say("Я буду следовать за вами",voice_m)
		follow_me()
		Say("Я закончил следовать за вами",voice_m)
		return
	if ((str(answer) == "обход территории") or (str(answer) == "территории")):
		Say("Приступаю к патрулированию",voice_m)
		patrul()
		Say("Патрулирование завершено",voice_m)
		return
	phrase = answer
	print("phrase ", phrase)
	if ((str(phrase) == "сделай фото") or (str(phrase) == "сделай фотографию") or (str(phrase) == "сделай фотку")):
		Say("Фотографирую",voice_m)
		return
	if ((str(phrase) == "начни запись аудио") or (str(phrase) == "начать запись аудио") or (str(phrase) == "запись аудио")):
		Say("запись аудио",voice_m)
		Say("запись аудио завершилась",voice_m)
		return
	if ((str(phrase) == "начни запись видео") or (str(phrase) == "начать запись видео") or (str(phrase) == "запись видео")):
		Say("запись видео",voice_m)
		Say("запись видео завершена",voice_m)
		return
	if ((str(phrase) == "начинаем тренироваться") or (str(phrase) == "начинаем тренировку") or (str(phrase) == "тренировку")  or (str(phrase) == "тренироваться")):
		Say("начинаем тренировку",voice_m)
		return
	if ((str(phrase) == "развернись кругом") or (str(phrase) == "развернись") or (str(phrase) == "кругом")or (str(phrase) == "повернись кругом") ):
		returning()
		print("command " + str(phrase))
		return
	if ((str(phrase) == "едь прямо") or (str(phrase) == "прямо")):
		forward_ride(1)
		print("command " + str(phrase))
		return
	if ((str(phrase) == "едь назад") or (str(phrase) == "назад")):
		back_ride(1)
		print("command " + str(phrase))
		return
	if ((str(phrase) == "повернись влево") or (str(phrase) == "влево")):
		turn_left(0.3)
		print("command " + str(phrase))
		return
	if ((str(phrase) == "повернись вправо") or (str(phrase) == "вправо")):
		turn_rigth(0.3)
		print("command " + str(phrase))
		return

	# Если ни одна функция не будет распознана
	speech_string = "Команда " + answer + " неизвестна роботу"
	Say(speech_string, voice_m)
	Say("Возможно, необходимо обновить прошивку. Обратитесь к оператору", voice_m)
	return

def main():

######  Reading configuration of Robot #######
##############################################

    rospy.init_node('general_programm_py')

# Запускаем отдельный поток с Bluetooth-управлением
    b_t = Bluetooth_Thread()
    b_t.start()

# Запускаем отдельный Wi-Fi-поток
    if (auto_inet[0] == "1"):
        wi_fi_t = Wi_Fi_Thread(internet_login,internet_password)
        wi_fi_t.start()

    while True:
        try:
            print("robot_locked_begin = ", robot_locked)
            Say("Ролли запущен",voice_m)
            # there add code from RaspberryPi
            filename = "/home/pi/ROS_projects/workspace/RESURSES/speech.wav"
            light("on")
            record_audio(6,filename)
            light("off")
            print("Голосовая команда отправлена на сервер.")
            my_command = "rosrun client_server publisher.py " + filename
            answer=os.popen(my_command).read()
            delete_file(filename)
            answer = answer[:-1]
            print("answer_GP = ", answer)
            if ((answer == "роли") or (answer == "ролли") or (answer == "робот")):
                Say("Ролли готов к работе",voice_m)
                # there add code from RaspberryPi
                filename = "/home/pi/ROS_projects/workspace/RESURSES/speech.wav"
                light("on")
                record_audio(6,filename)
                light("off")
                print("Голосовая команда отправлена на сервер.")
                my_command = "rosrun client_server publisher.py " + filename
                answer=os.popen(my_command).read()
                delete_file(filename)
                print("answer_GP = ", answer)
                analyse_answer(answer)
            print("robot_locked_end_ = ", robot_locked)	
        except:
            Say("Ошибка при выполнении команды",voice_m)

# executable_code
if __name__ == "__main__":
    main()
