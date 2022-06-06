#! /usr/bin/python3

from __future__ import print_function

import rospy
from my_service.srv import FileData, FileDataResponse, FileDataRequest

from pocketsphinx import LiveSpeech, get_model_path
from pocketsphinx import Pocketsphinx, get_data_path
import os

# for dlib --------------------------
import sys
import dlib
import glob
#import cv2
from scipy.spatial import distance
import time;
import datetime
from time import gmtime, strftime
# -----------------------------------

# for Inet --------------------------
from bs4 import BeautifulSoup
from urllib.request import urlopen
import requests as req
# for Inet --------------------------

model_path = get_model_path()
data_path = get_data_path()

config = {
    
    'hmm': os.path.join(model_path, 'zero_ru.cd_cont_4000'),
    'lm': os.path.join(model_path, 'ru.lm'),
    'dict': os.path.join(model_path, 'my_dict.dic')
}

print("Initialise Pocket_Sphinx_Russian ...")

ps = Pocketsphinx(**config) 

print("Initialise Pocket_Sphinx_Russian finish successfull.")

print("Initialise Dlib_algorithms ...")
###################################################################

predictor_path = "/home/sergey/ROS/Projects_ROS/workspace/RESURSES/shape_predictor_5_face_landmarks.dat"
face_rec_model_path = "/home/sergey/ROS/Projects_ROS/workspace/RESURSES/dlib_face_recognition_resnet_model_v1.dat"
faces_folder_path = "/home/sergey/ROS/Projects_ROS/workspace/RESURSES/faces/"

detector = dlib.get_frontal_face_detector()
sp = dlib.shape_predictor(predictor_path)
facerec = dlib.face_recognition_model_v1(face_rec_model_path)

#win = dlib.image_window()

#Read_from_file_count_people
count_people = 0

f_count_people = open('/home/sergey/ROS/Projects_ROS/workspace/RESURSES/count_people.txt','r')
for line in f_count_people:
    count_people = int(line)
    print("count_people = ", line)
f_count_people.close()

#Read_from_file_list_people
list_people = []

f_list_people = open('/home/sergey/ROS/Projects_ROS/workspace/RESURSES/list_people.txt','r')
for line in f_list_people:
    list_people.append(line)
    print("Name = ", line)
f_list_people.close()


face_descriptors = []
fd = dlib.vector()

#Counting face_descriptors of people
for i in range(0,count_people):
    img1 = dlib.load_rgb_image(faces_folder_path + str(i+1) +".jpg")
    #img1 = cv2.imread("/home/pi/Documents/"+ str(i+1) +".jpg")
    dets1 = detector(img1, 1)
    for k, d in enumerate(dets1):
        print("Detection {}: Left: {} Top: {} Right: {} Bottom: {}".format(
            k, d.left(), d.top(), d.right(), d.bottom()))
        shape1 = sp (img1,d)
        fd = facerec.compute_face_descriptor(img1, shape1)
        face_descriptors.append(fd)
    
################################################################### 

print("Initialise Dlib_algorithms finish successfull.")

def Tell_news(url):
	rs=urlopen(url)
	root = BeautifulSoup(rs, 'html.parser')
	tags = root.find_all('h2')
	strings = []
	filename = "RESURSES/news.txt"
	out_file = open(filename, "w") # open for [w]riting as [b]inary
	for tag in tags:
		strings.append(" ".join(tag.text.split()))
	for i in range(0,10): #len(strings)
		out_file.write(strings[i] + "\n")
		print(strings[i])
	out_file.close()
	in_file = open(filename, "r") # opening for [r]eading as [b]inary
	data = in_file.read() # if you only wanted to read 512 bytes, do .read(512)
	in_file.close()
	delete_file(filename)
	return data

def Get_Whether(url):
    rs=urlopen(url)
    root = BeautifulSoup(rs, 'html.parser')
    count = 0
    strings = []
    for child in root.recursiveChildGenerator():
        if count < 24:
            if (child.name == 'td'):
                if (child.text != ""):
                    strings.append(child.text)
                    print(child.text)
                    print('-----------------')
                    count = count + 1
        else:
            break
    strings[0] = strings[0] + ""
    strings[2] = "Давление: " + strings[2] + " миллиметров ртутного столба"
    strings[3] = "Влажность: " + strings[3] + ""
    strings[4] = "Ветер: "+ strings[4] + " метров в секунду"
    strings[5] = "Ощущается как: " + strings[5]
     
    strings[6] = strings[6] + " "
    strings[8] = "Давление: " + strings[8] + " миллиметров ртутного столба"
    strings[9] = "Влажность: " + strings[9] + ""
    strings[10] = "Ветер: "+ strings[10] + " метров в секунду"
    strings[11] = "Ощущается как: " + strings[11]
     
    strings[12] = strings[12] + " "
    strings[14] = "Давление: " + strings[14] + " миллиметров ртутного столба"
    strings[15] = "Влажность: " + strings[15] + ""
    strings[16] = "Ветер: "+ strings[16] + " метров в секунду"
    strings[17] = "Ощущается как: " + strings[17]
     
    strings[18] = strings[18] + ""
    strings[20] = "Давление: " + strings[20] + " миллиметров ртутного столба"
    strings[21] = "Влажность: " + strings[21] + ""
    strings[22] = "Ветер: "+ strings[22] + " метров в секунду"
    strings[23] = "Ощущается как: " + strings[23]
    
    answer = ""
    for i in range(0,24):
        answer = answer + strings[i] + "\n"
        print(strings[i])
    return answer
    
def SayDealsToday(parametrs_deals):
    today = str(datetime.datetime.date(datetime.datetime.today()))
    
    today_year = int(listToString(today[0] + today[1] + today[2] + today[3]))
    if (today[5]=='0'):
            today_month = int(listToString(today[6]))
    else:
        today_month = int(listToString(today[5] + today[6]))
    if (today[8] =='0'):
        today_day = int(listToString(today[9]))
    else:
        today_day = int(listToString(today[8] + today[9]))
        
    print("today_day = " + str(today_day))
    print("today_month = " + str(today_month))
    print("today_year = " + str(today_year))
    
    count_deals_today = 0;
    string_answer = ""
    for string in parametrs_deals:
        if (string[0] == 'z'):
            continue
        if (string[0] =='0'):
            day = int(listToString(string[1]))
        else:
            day = int(listToString(string[0] + string[1]))
        if (string[3]=='0'):
            month = int(listToString(string[4]))
        else:
            month = int(listToString(string[3] + string[4]))
        year = int(listToString(string[6] + string[7] + string[8] + string[9]))
        message = []
        for i in range(11, len(string)):
            message.append(string[i])
        print("day = " + str(day))
        print("month = " + str(month))
        print("year = " + str(year))
        
        if ((today_day == day)and(today_month==month)and(today_year == year)):
            #----
            count_deals_today = count_deals_today +1
            string_answer = string_answer + listToString(message) + "\n"
            
    if (count_deals_today == 0 ):
        string_answer = "На сегодня у вас не запланировано никаких дел. Удачного дня"
        return string_answer
    return string_answer  

def listToString(s):
    str1 = ""
    for ele in s:
        str1 += ele
    return str1

def delete_file(filename):
	os.remove(filename)

def choise_max_answer(phrase_all):
	for phrase in phrase_all:
		print(phrase)
		if ((str(phrase) == "роли") or (str(phrase) == "ролли") or (str(phrase) == "робот")):
			return phrase
		if ((str(phrase) == "приготовиться обновлению") or (str(phrase) == "обновлению")):
			return phrase
		if ((str(phrase) == "перейти на радио управление") or (str(phrase) == "перейди на радио управление") or (str(phrase) == "радио управление")):
			return phrase
		if ((str(phrase) == "начинаем тренироваться") or (str(phrase) == "начинаем тренировку") or (str(phrase) == "тренировку")  or (str(phrase) == "тренироваться")):
 			return phrase
		if ((str(phrase) == "начни запись видео") or (str(phrase) == "начать запись видео") or (str(phrase) == "запись видео")):
			return phrase
		if ((str(phrase) == "начни запись аудио") or (str(phrase) == "начать запись аудио") or (str(phrase) == "запись аудио")):
			return phrase
		if ((str(phrase) == "сделай фото") or (str(phrase) == "сделай фотографию") or (str(phrase) == "сделай фотку")):
			return phrase      
		if ((str(phrase) == "перейти на голосовое управление") or (str(phrase) == "перейди на голосовое управление") or (str(phrase) == "на голосовое управление") or (str(phrase) == "голосовое управление") or (str(phrase) == "перейди на ручное управление") or (str(phrase) == "на ручное управление") or (str(phrase) == "перейти на ручное управление") or (str(phrase) == "ручное управление")):
			return phrase
		if ((str(phrase) == "напомни дела на сегодня") or (str(phrase) == "прочитай напоминания на сегодня") or (str(phrase) == "напоминания на сегодня") or (str(phrase) == "дела на сегодня")):
			return phrase
		if ((str(phrase) == "скажи сколько время") or (str(phrase) == "скажи сколько времени") or (str(phrase) == "скажи времени") or (str(phrase) == "скажи время")):
			return phrase
		if ((str(phrase) == "прочитай последние новости") or (str(phrase) == "последние новости")):
			return phrase
		if ((str(phrase) == "скажи погоду на сегодня") or (str(phrase) == "погоду на сегодня") or (str(phrase) == "погоду на")):
			return phrase
		if ((str(phrase) == "обход территории") or (str(phrase) == "территории")):
			return phrase
		if ((str(phrase) == "следуй за мной") or (str(phrase) == "едь за мной") or (str(phrase) == "за мной")):
			return phrase
		if ((str(phrase) == "подключить интернет") or (str(phrase) == "интернет")):
			return phrase
		if ((str(phrase) == "едь прямо") or (str(phrase) == "прямо")):
			return phrase
		if ((str(phrase) == "едь назад") or (str(phrase) == "назад")):
			return phrase
		if ((str(phrase) == "повернись влево") or (str(phrase) == "влево")):
			return phrase
		if ((str(phrase) == "повернись вправо") or (str(phrase) == "вправо")):
			return phrase
		if ((str(phrase) == "развернись кругом") or (str(phrase) == "развернись") or (str(phrase) == "кругом")or (str(phrase) == "повернись кругом") ):
			return phrase
	return phrase_all[0]

def voice_processing(filename):
    ps.decode(
        audio_file=(filename),
        buffer_size=1000000,
        no_search=False,
        full_utt=False
    )
    #print(ps.hypothesis())  # => go forward ten meters
    #print(" ------------ ")
    
    string_str = []
    string_str.append("---begin---")
    for i in range(0,len(ps.best(count=10))):
        strings_0 = str(ps.best(count =10)[i])
        j=2;
        string_buf = []
        if strings_0[1] != "N":
            while strings_0[j] != "'":
                string_buf.append(strings_0[j])
                j=j+1
            string_str.append(listToString(string_buf))    
    return string_str

def image_processing(filename):
	img = dlib.load_rgb_image(filename)
	dets = detector(img, 1)
	print("Number of faces detected: {}".format(len(dets)))
	if (len(dets) > 0):
		for k, d in enumerate(dets):
			print("Detection {}: Left: {} Top: {} Right: {} Bottom: {}".format(
                    k, d.left(), d.top(), d.right(), d.bottom()))
			shape = sp(img, d)
			face_descriptor = facerec.compute_face_descriptor(img, shape)
			for i in range(0, count_people):
				result = distance.euclidean(face_descriptor, face_descriptors[i])
				print("distance = ", result)
				if result > 0.6:
					print("FALSE")
				else:
					print("TRUE")
					name_of_recognized_people = list_people[i]
					return name_of_recognized_people
			name_of_recognized_people = "UNKNOW"
			return name_of_recognized_people
	else:
		name_of_recognized_people = "NOT_FOUND"
		return name_of_recognized_people

def handle_file_data(req):
	print("Receive file  [format: %s; size: %s]"%(req.format_file, req.size_file))
	filename = "/home/sergey/ROS/Projects_ROS/workspace/RESURSES/out-file." + str(req.format_file)
	data = req.data_file
	out_file = open(filename, "wb")
	out_file.write(data)
	out_file.close()

	if req.format_file == "jpg" or req.format_file == "jpeg" or req.format_file == "png":
		responce_str = image_processing(filename)
		delete_file(filename)
		return FileDataResponse(responce_str)
		
	if req.format_file == "wav":
		phrases_all = voice_processing(filename)
		responce_str = choise_max_answer(phrases_all)
		delete_file(filename)
		return FileDataResponse(responce_str)
	
	if req.format_file == "txt":
		in_file = open(filename, "r") # opening for [r]eading as [b]inary
		data = in_file.read() # if you only wanted to read 512 bytes, do .read(512)
		in_file.close()
		print("text_data = ", data)
		if data == "tell_news\n":
			#print("yes")
			url = 'https://yandex.ru/news?msid=1579939399.8835.139899.101710&mlid=1579938434.glob_225'
			responce_str = Tell_news(url)
			delete_file(filename)
			return responce_str
		if data == "tell_wether\n":
			#print("yes")
			url = 'https://yandex.ru/pogoda/orel/details?via=ms#'
			responce_str = Get_Whether(url)
			delete_file(filename)
			return responce_str
		if data == "tell_deals\n":
			parametrs_deals = []
			print("Printing deals: ")
			robot_deals = open('/home/pi/Documents/deals.txt','r')
			for line in robot_deals:
    				parametrs_deals.append(line)    
    				print(line)
			robot_deals.close()
			responce_str = SayDealsToday(parametrs_deals)
			return responce_str
			
	# If recieved usually file (txt and e.t.c.)
	responce_str = "good_file. Size = " + str (req.size_file) + "bytes. Format = " + str(req.format_file)	
	return FileDataResponse(responce_str)
	
def file_data_server():
	rospy.init_node('subscriber_service_py')
	subscriber = rospy.Service('file_data', FileData, handle_file_data)
	print("Ready to FileDataHandler")
	rospy.spin()

if __name__ == "__main__":
    file_data_server()




