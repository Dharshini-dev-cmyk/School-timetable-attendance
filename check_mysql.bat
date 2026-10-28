@echo off
set MYSQL_EXE="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
echo Testing connection without password...
%MYSQL_EXE% -u root -e "SELECT VERSION();" 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Testing with password: root
    %MYSQL_EXE% -u root -proot -e "SELECT VERSION();" 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo Testing with password: password
        %MYSQL_EXE% -u root -ppassword -e "SELECT VERSION();" 2>&1
    )
)
