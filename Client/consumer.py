import os
import requests
import hashlib
import time

path = "C:\\Users\\gaime\\OneDrive\\Documents\\AUI\\Spring 2022\\Advanced Distributed Programming Paradigms\\Homework 2\\GDriveP"




def initialize():
    dic = requests.get("http://localhost:8080").json()
    fileList = os.listdir(path)
    if(len(fileList) == 0):
        for res in dic.keys():
            syncfile = requests.get("http://localhost:8080/file",data = {'fileName':res})
            open(path+"\\"+res,'wb').write(syncfile.content)


def start():
    initialize()
    while True:
        fileList = os.listdir(path)
        dic = requests.get("http://localhost:8080").json()
        for file in fileList:
            if(file in dic.keys()):
                data = open(path+"\\"+file,'rb').read()
                check = hashlib.md5(data)
                if(check.hexdigest() != dic[file]):
                    requests.put("http://localhost:8080/"+file, {'data':data})
            else:
                data =open(path+"\\"+file,'rb').read()
                requests.put("http://localhost:8080/"+file, {'data':data})
        for file in dic.keys():
            if(file not in fileList):
                requests.delete("http://localhost:8080/"+file)
        time.sleep(10)



start()
