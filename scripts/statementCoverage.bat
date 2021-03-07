@echo off
setlocal enabledelayedexpansion

set allStatement=0
set allExecutedStatement=0

for %%f in (numOfStatement/*) do (

	set /p total=<numOfStatement/%%f
	set executed=0
	
	if exist "executedStatement/%%f" (
	
		set /p executed=<executedStatement/%%f
	
	)
	
	set /a allStatement=!allStatement!+!total!
	set /a allExecutedStatement=!allExecutedStatement!+!executed!
	
	set /a coverageInt=!executed!*100 / !total!
	set /a coverageFlt=!executed!*10000 / !total! %% 100
	
	set coverage=!coverageInt!.!coverageFlt!
	
	for /f "tokens=1 delims=_" %%a in ("%%f") do echo !coverage!%% ^| %%a
	
)

set /a coverageInt=!allExecutedStatement!*100 / !allStatement!
set /a coverageFlt=!allExecutedStatement!*10000 / !allStatement! %% 100
	
set coverage=!coverageInt!.!coverageFlt!

echo Total Coverage: !coverage!%%

pause