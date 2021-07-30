@echo off
java -jar signapk.jar hygamekey.x509.pem hygamekey.pk8 %1/dist/*.apk %1_newSign.apk && adb install %1_newSign.apk
pause