#!/bin/bash
#这里可替换为你自己的执行程序，其他代码无需更改
#Jenkins会在构建完成后不使用processTreeKiller杀掉了所有子进程
export BUILD_ID=dontKillMe
#export JAVA_HOME=/usr/jdk1.8.0_201
#export JRE_HOME=/usr/jdk1.8.0_201/jre
#export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
#export PATH=$JAVA_HOME/bin:$PATH

BASE_PATH=$(cd `dirname $0`; pwd)
APP_NAME=$(ls $BASE_PATH|grep *.jar)

echo "操作类型为：$1"
echo "使用环境为：$2"
ENV_CONFIG=$2

#使用说明，用来提示输入参数
usage() {
 echo "Usage: sh 脚本名.sh [start|stop|restart|status]    [prod|sit]"
 exit 1
}

#检查程序是否在运行
is_exist(){
 pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
 #如果不存在返回1，存在返回0
 if [ -z "${pid}" ]; then
 return 1
 else
 return 0
 fi
}

#启动方法
start(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is already running. pid=${pid} ."
 else
 nohup java -jar -Dspring.profiles.active=$ENV_CONFIG  $BASE_PATH/$APP_NAME  > $BASE_PATH/service.log 2>&1 &
 echo "${APP_NAME} start ..............."
 fi
}

#停止方法
stop(){
 is_exist
 if [ $? -eq "0" ]; then
 kill -9 $pid
 echo "${APP_NAME} stop ..............."
 else
 echo "${APP_NAME} is not running"
 fi
}

#输出运行状态
status(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is running. Pid is ${pid}"
 else
 echo "${APP_NAME} is NOT running."
 fi
}

#重启
restart(){
 stop
 start
}
  
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
 "start")
 start
 ;;
 "stop")
 stop
 ;;
 "status")
 status
 ;;
 "restart")
 restart
 ;;
 *)
 usage
 ;;
esac