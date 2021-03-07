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

3. Right click the java file of comp5111.assignment.cut.RegressionTest.java and choose `Coverage As -> JUnit Test` to run the unit test

## Compile and run with Soot

### Compile the soot driver

1. Open Eclipse and import this project

2. Make sure the follow folders are in building directory
   
   1. {projectroot}/src/main/java
   
   2. {projectroot}/lib
   
   3. one of the generated test case (e.g. randoop0): {projectroot}/src/test/randoop0

3. Run the pre-configured run configuration `MainDriver` or run the `comp5111.assignment.MainDriver` class with following arguments:
   
   ```cmd
   comp5111.assignment.cut.ToolBox ^
   comp5111.assignment.cut.ToolBox$ArrayTools ^
   comp5111.assignment.cut.ToolBox$CharSequenceTools ^
   comp5111.assignment.cut.ToolBox$CharTools ^
   comp5111.assignment.cut.ToolBox$LocaleTools ^
   comp5111.assignment.cut.ToolBox$RegExTools ^
   comp5111.assignment.cut.ToolBox$StringTools
   ```
   
   You may delete `^` and place the arguments in one line if it is not working.

4. After the successful run you can find the processed classes in `{projectroot}/sootOutput`

5. Two folders `{projectroot}/scripts/numOfStatement` and `{projectroot}/scripts/numOfBranch` are created to store the number of statement and branch respectively for each class

### Run test with Soot

1. Open Command Prompt and navigate to folder `{projectroot}/scripts`

2. Double click and run the `runTest.bat` to perform JUnit test on compiled classes

`runTest.bat` does the following:

1. Delete the all folders and files generated from previous run

2. Copy all class files from `{projectroot}/target/classes` to `classes`

3. Delete all ToolBox class and subclasses in `classes` so that the generated classes from soot can be loaded

4. Run the JUnit test with classpath `./classes;../sootOutput;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar`

5. Two folders `scripts/executedStatement` and `scripts/executedBranch` are created to store the number of executed statement and branch respectively for each class



### Measure the coverage

Run `{projectroot}/scripts/coverage.bat` to invoke the following batch program and display all the coverages

- Statement Coverage: `statementCoverage.bat`

- Branch Coverage: `branchCoverage.bat`

The output of program will be in format:

```cmd
--- Statement Coverage ---
{Percentage of Coverage} | {Class A}
{Percentage of Coverage} | {Class B}
{Percentage of Coverage} | {Class C}
{Percentage of Coverage} | {Class D}
Overall {Statement} Coverage: {Percentage of Coverage}

--- Branch Coverage ---
{Percentage of Coverage} | {Class A}
{Percentage of Coverage} | {Class B}
{Percentage of Coverage} | {Class C}
{Percentage of Coverage} | {Class D}
Overall {Branch} Coverage: {Percentage of Coverage}
```

## Coverage difference

| Test Case | Statement Coverage (EclEmma) | Branch Coverage (EclEmma) | Statement Coverage (Soot) | Branch Coverage (Soot) |
|:---------:|:----------------------------:|:-------------------------:|:-------------------------:|:----------------------:|
| Randoop0  | 53.4%                        | 43.7%                     |                           |                        |
| Randoop1  | 50.6%                        | 41.0%                     | 51.98%                    | 42.26%                 |
| Randoop2  | 50.6%                        | 41.2%                     |                           |                        |
| Randoop3  | 52.2%                        | 42.1%                     |                           |                        |
| Randoop4  | 52.3%                        | 42.1%                     |                           |                        |


