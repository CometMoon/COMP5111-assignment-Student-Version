@echo off
rmdir /s /Q "tmp"
robocopy /E "../target/classes" "classes"
pushd "classes/comp5111/assignment/cut"
del "ToolBox*" /Q
popd
java -cp "./classes;../sootOutput;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore comp5111.assignment.cut.RegressionTest
pause