@echo off
setlocal

REM Compile application classes
javac -d bin src\bank\data\FileHandler.java src\bank\entity\*.java src\bank\factory\*.java src\bank\singleton\*.java src\bank\strategy\*.java src\bank\service\*.java src\bank\ui\*.java src\bank\util\*.java src\Main.java src\bank\Main.java
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

REM Compile test classes using local JUnit 5 jars
set JUNIT_CP=bin;C:\Users\HP\.m2\repository\org\junit\jupiter\junit-jupiter-api\5.10.2\junit-jupiter-api-5.10.2.jar;C:\Users\HP\.m2\repository\org\junit\platform\junit-platform-commons\1.10.2\junit-platform-commons-1.10.2.jar;C:\Users\HP\.m2\repository\org\junit\platform\junit-platform-engine\1.10.2\junit-platform-engine-1.10.2.jar;C:\Users\HP\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\HP\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar
javac -cp %JUNIT_CP% -d bin src\bank\test\*.java
if errorlevel 1 (
    echo Test compilation failed.
    exit /b 1
)

echo Test compilation succeeded.
pause
