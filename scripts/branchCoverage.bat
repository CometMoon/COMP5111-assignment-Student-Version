@echo off
setlocal enabledelayedexpansion

set allBranch=0
set allExecutedBranch=0

for %%f in (numOfBranch/*) do (

	set /p total=<numOfBranch/%%f
	set executed=0
	
	if exist "executedBranch/%%f" (
	
		set /p executed=<executedBranch/%%f
	
	)
	
	set /a allBranch=!allBranch!+!total!
	set /a allExecutedBranch=!allExecutedBranch!+!executed!
	
	if !total! neq 0 (
		
		set /a coverageInt=!executed!*100 / !total!
		set /a coverageFlt=!executed!*10000 / !total! %% 100
	
		set coverage=!coverageInt!.!coverageFlt!
		
	) else (
	
		set coverage=100.00
	
	)
	
	for /f "tokens=1 delims=_" %%a in ("%%f") do echo !coverage!%% ^| %%a
	
)

set /a coverageInt=!allExecutedBranch!*100 / !allBranch!
set /a coverageFlt=!allExecutedBranch!*10000 / !allBranch! %% 100
	
set coverage=!coverageInt!.!coverageFlt!

echo.
echo Overall Total Number of Branch: !allBranch!
echo Overall Number of Executed Branch: !allExecutedBranch!
echo.
echo Overall Branch Coverage: !coverage!%%