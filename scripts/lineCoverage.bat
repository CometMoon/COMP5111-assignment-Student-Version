@echo off
setlocal enabledelayedexpansion

set allLine=0
set allExecutedLine=0

for %%f in (numOfLine/*) do (

	set /p total=<numOfLine/%%f
	set executed=0
	
	if exist "executedLine/%%f" (
	
		set /p executed=<executedLine/%%f
	
	)
	
	set /a allLine=!allLine!+!total!
	set /a allExecutedLine=!allExecutedLine!+!executed!
	
	set /a coverageInt=!executed!*100 / !total!
	set /a coverageFlt=!executed!*10000 / !total! %% 100
	
	set coverage=!coverageInt!.!coverageFlt!
	
	for /f "tokens=1 delims=_" %%a in ("%%f") do echo !coverage!%% ^| %%a
	
)

set /a coverageInt=!allExecutedLine!*100 / !allLine!
set /a coverageFlt=!allExecutedLine!*10000 / !allLine! %% 100
	
set coverage=!coverageInt!.!coverageFlt!

echo.
echo Overall Total Number of Line: !allLine!
echo Overall Number of Executed Line: !allExecutedLine!
echo.
echo Overall Line Coverage: !coverage!%%