@echo off
java -jar apktool4.jar b %1 && java -jar signapk.jar hygamekey.x509.pem hygamekey.pk8 %1/dist/*.apk %1_newSign.apk && adb install %1_newSign.apk
pause