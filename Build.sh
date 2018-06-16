javac src/com/dan/ssdp/*.java
jar cf SSDP-Search-1-0.jar src/com/dan/ssdp/*.class
mkdir javadoc
javadoc -d javadoc src/com/dan/ssdp/*.java
