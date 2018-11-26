# LeapMotion服务

LeapMotion服务，支持TUIO及UDP报文格式
2018-11-26
   
配置文件conf/config.txt
>**服务消息类型[TUIO/UDP]**  
>>messageType=UDP

>**LeapMotion有效区域（单位mm）**  
>X轴  
>>XMin=-133.33  
>>XMax=133.33

>Y轴
>>YMin=100  
>>YMax=300 

>Z轴
>>ZMin=-100  
>>ZMax=100

>**是否进行坐标转换[true/false]（TUIO协议强制转换坐标）**   
>>mapCoordinate=true


>**客户端配置**  
>客户端IP
>>clientHost=127.0.0.1  

>客户端监听端口  
>>clientPort=3333  