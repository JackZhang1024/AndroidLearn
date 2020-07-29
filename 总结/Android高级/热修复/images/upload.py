# -*- coding: utf-8 -*-
import sys
import os
import requests
import time
import json

# python upload.py mvc架构.jpg mvc.jpg
# python upload.py ./Android高级/Java虚拟机/images/compile.png mvc.jpg

reload(sys)
sys.setdefaultencoding( "utf-8" )
srcFilePath = sys.argv[1]
targetFileName = sys.argv[2]
print srcFilePath
print targetFileName

upload_url = 'http://122.51.191.103:8080/serverdemo/fileUploadOSS'
header={"ct":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"}
files = {'file':open(srcFilePath,'rb')}
upload_data={"file": targetFileName}
upload_res=requests.post(upload_url,upload_data,files=files,headers=header)
print upload_res.text
jsonResult = json.loads(upload_res.text)
print jsonResult
print jsonResult['message']
downloadUrl =  jsonResult['data']['data']['ossUrl']
lists =downloadUrl.split('?')
print lists[0]
