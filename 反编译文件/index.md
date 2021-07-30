## 以上就是反编译文件


### Android 反编译

主要流程

1. [apktool](https://www.softpedia.com/get/Programming/Debuggers-Decompilers-Dissasemblers/ApkTool.shtml)

	   //反编译
	   apktool_2.3.4.jar d -f demo.apk 

	   //重新打包
	   apktool_2.3.4.jar b .\demo\   
     
   反编译.bat代码  
     
       @echo off
       java -jar apktool_2.3.4.jar b %1 
       pause
    
2. [dex2Jar](https://nchc.dl.sourceforge.net/project/dex2jar/dex2jar-2.0.zip)

        \dex2jar-2.0\d2j-dex2jar.bat demo\classes.dex
        
    直接解压后apk后的classes.dex文件鼠标拖动到  \dex2jar-2.0\d2j-dex2jar.bat 执行即可，直行成功后得到 classes-dex2jar.jar 文件

3. [jd-gui](https://www.softpedia.com/get/Programming/Debuggers-Decompilers-Dissasemblers/JD-GUI.shtml)
  
        用jd-gui软件打开dex2Jar反编译后的classes-dex2jar.jar文件即可查看java源码
