@echo off
setlocal
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin"
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Maven...
    java -cp "%~dp0.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain -v >nul 2>&1
)
set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
if exist "%MVN_CMD%" (
    call "%MVN_CMD%" %*
) else (
    java -cp "%~dp0.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
)
endlocal
