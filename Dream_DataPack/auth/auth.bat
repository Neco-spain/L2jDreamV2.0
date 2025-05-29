@echo off
set PATH=C:\Program Files\Java\jre1.8.0_311\bin;%PATH%
title Auth Server Console

java -Xmx128m -cp ../libs/*; com.dream.auth.L2AuthServer

