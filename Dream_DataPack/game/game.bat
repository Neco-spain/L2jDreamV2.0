@echo off
set PATH=C:\Program Files\Java\jre1.8.0_311\bin;%PATH%
title Game Server Console

:start
java -Xmx1g -XX:+HeapDumpOnOutOfMemoryError -cp ../libs/*; com.dream.game.L2GameServer
if ERRORLEVEL 2 goto start
pause
:question
set choix=q
set /p choix=(s) or (q) 
if /i %choix%==s goto start
if /i %choix%==q goto exit
:exit
exit