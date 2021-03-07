@echo off
setlocal EnableDelayedExpansion

if not exist coverageMap mkdir coverageMap
goto there
rem Statement Coverage

echo Generating Statement Coverage Report...

<numOfStatement/records/statements.txt more | sort >coverageMap/tmp.txt
<executedStatement/records/executed_statement.txt more | sort >coverageMap/tmp2.txt

rem Read executed statementID to array executedStatement
set i=0
for /F "tokens=* delims=" %%a in (coverageMap/tmp2.txt) do (
   set executedStatement[!i!]=%%a
   set /A i+=1
)
set n=%i%

rem Create file for storing generated map
echo Statement Coverage Report > coverageMap/statement_map.txt

rem Loop all statements and generate output
set i=0
for /F "tokens=* delims=" %%a in (coverageMap/tmp.txt) do (
	
	if not !i! equ %n% (
	
		for /f "tokens=1 delims=^|" %%c in ("!i!") do ( set es=!executedStatement[%%c]! )
	
		for /f "tokens=1 delims=^|" %%b in ("%%a") do (
			
			if "%%b" == "!es:~0,-1!" (
			
				echo Yes ^| %%a >>coverageMap/statement_map.txt
				
				set /A i+=1
			
			) else (
			
				echo No  ^| %%a >>coverageMap/statement_map.txt
			
			)
		
		)

	) else (
	
		echo No  ^| %%a >>coverageMap/statement_map.txt
	
	)
   
)
:there
rem Branch Coverage

echo Generating Branch Coverage Report...

<numOfBranch/records/branches.txt more | sort >coverageMap/tmp.txt
<executedBranch/records/executed_branches.txt more | sort >coverageMap/tmp2.txt

rem Read executed statementID to array executedBranch
set i=0
for /F "tokens=1 delims=-" %%a in (coverageMap/tmp2.txt) do (
   set executedBranch[!i!]=%%a
   set /A i+=1
)
set n=%i%

rem Create file for storing generated map
echo Branch Coverage Report > coverageMap/branch_map.txt

rem Loop all branches and generate output
set i=0
for /F "tokens=* delims=" %%a in (coverageMap/tmp.txt) do (
	
	if not !i! equ %n% (
	
		for /f "tokens=1 delims=^|" %%c in ("!i!") do ( set es=!executedBranch[%%c]! )
	
		for /f "tokens=1 delims=^|" %%b in ("%%a") do (
			
			if "%%b" == "!es:~0,-1!" (
			
				echo Yes ^| %%a >>coverageMap/branch_map.txt
				
				set /A i+=1
			
			) else (
			
				echo No  ^| %%a >>coverageMap/branch_map.txt
			
			)
		
		)

	) else (
	
		echo No  ^| %%a >>coverageMap/branch_map.txt
	
	)
   
)

rem Line Coverage

echo Generating Line Coverage Report...

<numOfLine/records/lines.txt more | sort >coverageMap/tmp.txt
<executedLine/records/executed_lines.txt more | sort >coverageMap/tmp2.txt

rem Read executed line number to array executedLine
set i=0
for /F "tokens=* delims=" %%a in (coverageMap/tmp2.txt) do (
   set executedLine[!i!]=%%a
   set /A i+=1
)
set n=%i%

rem Create file for storing generated map
echo Line Coverage Report > coverageMap/line_map.txt

rem Loop all lines and generate output
set i=0
for /F "tokens=* delims=" %%a in (coverageMap/tmp.txt) do (
	
	if not !i! equ %n% (
	
		for /f "tokens=1 delims=^|" %%c in ("!i!") do ( set es=!executedLine[%%c]! )
	
		for /f "tokens=1 delims=^|" %%b in ("%%a") do (
			
			if "%%b" == "!es:~0,-1!" (
			
				echo Yes ^| %%a >>coverageMap/line_map.txt
				
				set /A i+=1
			
			) else (
			
				echo No  ^| %%a >>coverageMap/line_map.txt
			
			)
		
		)

	) else (
	
		echo No  ^| %%a >>coverageMap/line_map.txt
	
	)
   
)

echo Cleaning tmp files...
pushd coverageMap
del "tmp*" /Q
popd
echo Done!
pause