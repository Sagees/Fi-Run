#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys
import os
import time
import datetime
import RPi.GPIO as GPIO
//import socket

//host = ''
//port = 4000

//client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
//client.connect((host, port))

def run():

//	file = open('/sys/devices/w1_bus_master1/w1_master_slaves')
//	w1_slaves = file.readlines()
//	file.close()

	GPIO.setmode(GPIO.BCM)
	fire_pin_1=20
	fire_pin_2=21

	GPIO.setup(fire_pin_1, GPIO.IN)
	GPIO.setup(fire_pin_2, GPIO.IN)

	# output_file = raw_input("output file name:  ")
	# mins = int(raw_input("몇 분동안?  "))
	# t = mins*60

	# # 현재 시각
	# cur_time = (time.time())
	# t1 = time.localtime(cur_time)
	# print ("Start time:  " + time.strftime("%b %d %Y %H:%M:%S", t1))

	# # 끝날 시각
	# end_time = cur_time + t
	# t2 = time.localtime(end_time)
	# print ("End time:   "+ time.strftime("%b %d %Y %H:%M:%S", t2))
	i_l = 0; i_h = 0
	cur_time = time.time()

	while True:
		
		# Fire detect
		fire_pin_input_1=GPIO.input(fire_pin_1)
		fire_pin_input_2=GPIO.input(fire_pin_2)
		
		if(fire_pin_input_1==1 and fire_pin_input_2==1):
			i_h+=1
			fire_pin_input_1 = 1; fire_pin_input_2 = 1;
		elif(fire_pin_input_1==0 or fire_pin_input_2==0):
			i_l+=1
			
		# Tempurature for each 1-Wire Slave 
//		for line in w1_slaves:
		# 1-wire Slave result
//			w1_slave = line.split("\n")[0]
		# 1-wire Slave read file
//			file = open('/sys/bus/w1/devices/' + str(w1_slave) + '/w1_slave')
//			filecontent = file.read()
//			file.close()

		# Read temperature and convert
//			stringvalue = filecontent.split("\n")[1].split(" ")[9]
//			temperature = float(stringvalue[2:]) / 1000

		# 불이 났는가 알아보는 과정
		if (time.time() - cur_time > 5):
			if ((i_l+i_h)/2 < i_l):
//				client.send("b MiniBeacon_00058".encode())
				i_l = 0; i_h = 0
			cur_time = time.time()

		time.sleep(0.1)
	

if __name__=="__main__":
        while True:
                run()
