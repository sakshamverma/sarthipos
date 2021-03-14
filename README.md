
#STEPS:

1. Install maven 3.6.3
2. open command prompt in /lib directory
3. execute the below commands:
    3.1 mvn install:install-file -Dfile=epsonjpos.jar -DartifactId=epsonjpos -DgroupdId=epsonjpos -Dversion=1.0 -Dpackaging=jar
    3.2 mvn install:install-file -Dfile=jpos1141.jar -DartifactId=jpos1141 -DgroupdId=jpos1141 -Dversion=1.0 -Dpackaging=jar
    3.3 mvn install:install-file -Dfile=xercesImpl.jar -DartifactId=xercesImpl -DgroupdId=xercesImpl -Dversion=1.0 -Dpackaging=jar
    3.4 mvn install:install-file -Dfile=xml-apis.jar -DartifactId=xml-apis -DgroupdId=xml-apis -Dversion=1.0 -Dpackaging=jar
4. cd ..
5. mvn install
6. mvn clean compile assembly:single
7. cd target
8. java -Djava.library.path=/path/to/lib/directory -jar JarNameInTarget.jar