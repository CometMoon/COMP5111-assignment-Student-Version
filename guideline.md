# Guideline

This guideline is for Windows environment only.

## Generate randoop test case

Use the following commands to generate test case with randomized seed.

```batch
java -ea -classpath "./lib/randoop-all-4.2.5.jar;./target/classes" ^
randoop.main.Main gentests ^
--testclass=comp5111.assignment.cut.ToolBox ^
--time-limit=120 --junit-package-name=comp5111.assignment.cut ^
--randomseed=%random% ^
--junit-output-dir=./src/test/randoop0
```

Change the `--junit-output-dir` to generate different test case. Please note that this script is not working in powershell since `%random%` syntax is not supported.

## Compile and run the test case with EclEmma

1. Open Eclipse and import this project

2. Make sure the follow folders are in building directory
   
   1. {projectroot}/src/main/java
   
   2. {projectroot}/lib
   
   3. one of the generated test case (e.g. randoop0): {projectroot}/src/test/randoop0

3. Run the pre-configured test configuration `EclEmma Test` or right click the java file of comp5111.assignment.cut.RegressionTest.java and choose `Coverage As -> JUnit Test` to run the unit test

## Compile and run with Soot

### Compile the soot driver

During the compilation there may have errors like NullPointerException or Write File Error. Simply compile a few more times or run the EclEmma Test then the error will disappear. It may be casued by the auto build of Eclipse? Cannot find the root cause even with dubug mode.

1. Open Eclipse and import this project

2. Make sure the follow folders are in building directory
   
   1. {projectroot}/src/main/java
   
   2. {projectroot}/lib
   
   3. one of the generated test case (e.g. randoop0): {projectroot}/src/test/randoop0

3. Run the pre-configured run configuration `MainDriver` or run the `comp5111.assignment.MainDriver` class with following arguments:
   
   ```cmd
   --keep-line-number ^
   comp5111.assignment.cut.ToolBox ^
   comp5111.assignment.cut.ToolBox$ArrayTools ^
   comp5111.assignment.cut.ToolBox$CharSequenceTools ^
   comp5111.assignment.cut.ToolBox$CharTools ^
   comp5111.assignment.cut.ToolBox$LocaleTools ^
   comp5111.assignment.cut.ToolBox$RegExTools ^
   comp5111.assignment.cut.ToolBox$StringTools
   ```
   
   `--keep-line-number` is used to preserve the line number from source code. You may delete `^` and place the arguments in one line if it is not working.

4. After the successful run you can find the processed classes in `{projectroot}/sootOutput`

5. Three folders `{projectroot}/scripts/numOfStatement`,`{projectroot}/scripts/numOfBranch`, and `{projectroot}/scripts/numOfLine` are created to store the number of statement, branch, and line respectively for each class

### Run test with Soot

1. Open Command Prompt and navigate to folder `{projectroot}/scripts`

2. Double click and run the `runTest.bat` to perform JUnit test on compiled classes

`runTest.bat` does the following:

1. Delete the all folders and files generated from previous run

2. Copy all class files from `{projectroot}/target/classes` to `classes`

3. Delete all ToolBox class and subclasses in `classes` so that the generated classes from soot can be loaded

4. Run the JUnit test with classpath `./classes;../sootOutput;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar`

5. Three folders `scripts/executedStatement`, `scripts/executedBranch`, and `scripts/executedLine` are created to store the number of executed statement, branch, and line respectively for each class

*Randoop 2 will run a bit longer since the time limit was set to 300s when generating the test case.* 



### Measure the coverage

Run `{projectroot}/scripts/coverage.bat` to invoke the following batch program, display all the coverages and save them to a `coverage.txt`in the same directory.

- Statement Coverage: `statementCoverage.bat`

- Branch Coverage: `branchCoverage.bat`

- Line Coverage: `lineCoverage.bat`

The sample output of program will be in format:

```cmd
--- Statement Coverage ---

100.0% | comp5111.assignment.cut.ToolBox$ArrayTools
24.58% | comp5111.assignment.cut.ToolBox$CharSequenceTools
100.0% | comp5111.assignment.cut.ToolBox$CharTools
28.16% | comp5111.assignment.cut.ToolBox$LocaleTools
69.44% | comp5111.assignment.cut.ToolBox$RegExTools
63.96% | comp5111.assignment.cut.ToolBox$StringTools
100.0% | comp5111.assignment.cut.ToolBox

Overall Total Number of Statement: 1010
Overall Number of Executed Statement: 525

Overall Statement Coverage: 51.98%

--- Branch Coverage ---

100.0% | comp5111.assignment.cut.ToolBox$ArrayTools
17.21% | comp5111.assignment.cut.ToolBox$CharSequenceTools
100.0% | comp5111.assignment.cut.ToolBox$CharTools
18.96% | comp5111.assignment.cut.ToolBox$LocaleTools
43.33% | comp5111.assignment.cut.ToolBox$RegExTools
51.85% | comp5111.assignment.cut.ToolBox$StringTools
100.00% | comp5111.assignment.cut.ToolBox

Overall Total Number of Branch: 556
Overall Number of Executed Branch: 235

Overall Branch Coverage: 42.26%

--- Line Coverage ---

100.0% | comp5111.assignment.cut.ToolBox$ArrayTools
23.57% | comp5111.assignment.cut.ToolBox$CharSequenceTools
100.0% | comp5111.assignment.cut.ToolBox$CharTools
39.13% | comp5111.assignment.cut.ToolBox$LocaleTools
68.75% | comp5111.assignment.cut.ToolBox$RegExTools
66.15% | comp5111.assignment.cut.ToolBox$StringTools
100.0% | comp5111.assignment.cut.ToolBox

Overall Total Number of Line: 466
Overall Number of Executed Line: 251

Overall Line Coverage: 53.86%

Press any key to continue . . .
```

## Coverage difference

| Test Case | Statement Coverage (EclEmma) | Branch Coverage (EclEmma) | Line Coverage (EclEmma) | Statement Coverage (Soot) | Branch Coverage (Soot) | Line Coverage (Soot) |
|:---------:|:----------------------------:|:-------------------------:|:-----------------------:|:-------------------------:|:----------------------:|:--------------------:|
| Randoop0  | 53.4%                        | 43.7%                     | 55.2%                   | 54.25%                    | 44.96%                 | 56.22%               |
| Randoop1  | 50.6%                        | 41.0%                     | 52.8%                   | 51.98%                    | 42.26%                 | 53.86%               |
| Randoop2  | 50.6%                        | 41.2%                     | 52.8%                   | 51.88%                    | 42.44%                 | 53.6%                |
| Randoop3  | 52.2%                        | 42.1%                     | 54.1%                   | 53.16%                    | 43.34%                 | 55.15%               |
| Randoop4  | 52.3%                        | 42.1%                     | 54.3%                   | 53.16%                    | 43.34%                 | 55.36%               |

## Clean up the generated files

Run `{projectroot}/scripts/cleanup.bat` to clean up the generated folders and files before next run to prevent potential error.


