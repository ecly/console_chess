#ConsoleChess
An easily extendable console based chess game written in Java.

#Build  
gradlew build

#Run  
*Due to an issue with Java's Scanner being triggered by Gradle run, use attached .bat script for running the compiled -jar"*  
Windows: run.bat  
Other: java -jar build/libs/ConsoleChess.jar  

**Current known shortcomings**  
Castling  
En passant  
Check for mate - only checks if king can move out of check. Doesn't check if other pieces can uncheck.
