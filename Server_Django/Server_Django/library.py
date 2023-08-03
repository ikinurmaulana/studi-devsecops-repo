from Crypto.Cipher import AES
from django.http import JsonResponse
from django.db import models
from API.models import *
import os
import base64
from Server_Django.settings import *
from django.http import request as Request
def padding(buff):
    while ((len(buff)%8)!=0):
        buff+=b'\0'
    return buff

def depadding(buff):
    return buff.rstrip(b'\0')

IV = 'h2aeOT8WUjJdNIpQUnd4xbMfv1lfwGC0'
KEY = 'kCh5HkolUSZSkzOUKv1Ycr0SuhmIzrvb'

def AESEncrypt(buff):
    aes = AES.new(KEY, AES.MODE_CBC, IV)
    return aes.encrypt(padding(buff))

def AESDecrypt(buff):
    aes = AES.new(KEY, AES.MODE_CBC, IV)
    return aes.decrypt(depadding(buff))


def mergeDict(all_dict) -> dict:
    init_dict = all_dict[0]
    del all_dict[0]
    for i in range(len(all_dict)):
        init_dict.update(all_dict[i])
    return init_dict



def allDatasMethods(request):
    all_methods = [request.POST, request.GET]
    return mergeDict(all_methods)


def jujojaz_login(f):
    def wrapper(*args, **kw):
        request = args[0]
        print(request)
        datas = allDatasMethods(request)
        datas = eval(datas["data"])
        # print(datas)
        if ( ("username" in datas) and ("password" in datas)):
            user = list(User.objects.filter(username=datas["username"], password=datas["password"]))
            if (len(user)==0):
                user = User(username=datas["username"], password=datas["password"])
                user.save()
                print("Username not found, automatically create it...")
                return f(*args, **kw)
            else:
                return f(*args, **kw)
        else:
            print('Invalid Request')
            return JsonResponse({'success': '0', 'msg': 'Error'})
    return wrapper

def getB64StringImage(file) :
    full_path = os.path.join(PHOTOS_DIR, file) + ".jpg"
    if os.path.isfile(full_path):
        with open(full_path, "rb") as reader :
            b64_string = base64.encodebytes(reader.read())
            return b64_string.decode()
    else:
        return ""