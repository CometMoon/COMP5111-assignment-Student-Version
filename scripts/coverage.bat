@echo off
echo.
echo --- Statement Coverage ---
echo.
call statementCoverage.bat
echo.
echo --- Branch Coverage ---
echo.
call branchCoverage.bat
echo.
echo --- Line Coverage ---
echo.
call lineCoverage.bat
echo.
>coverage.txt (
	echo.
	echo --- Statement Coverage ---
	echo.
	call statementCoverage.bat
	echo.
	echo --- Branch Coverage ---
	echo.
	call branchCoverage.bat
	echo.
	echo --- Line Coverage ---
	echo.
	call lineCoverage.bat
	echo.
)
pause