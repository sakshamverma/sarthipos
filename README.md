
<h1>STEPS:</h1>
<ol>
<li>Install maven 3.6.3</li>
<li>open command prompt in /lib directory</li>
<li>execute the below commands:
    <ul>
        <li>mvn install:install-file -Dfile=epsonjpos.jar -DartifactId=epsonjpos -DgroupdId=epsonjpos -Dversion=1.0 -Dpackaging=jar</li>
        <li>mvn install:install-file -Dfile=jpos1141.jar -DartifactId=jpos1141 -DgroupdId=jpos1141 -Dversion=1.0 -Dpackaging=jar</li>
        <li>mvn install:install-file -Dfile=xercesImpl.jar -DartifactId=xercesImpl -DgroupdId=xercesImpl -Dversion=1.0 -Dpackaging=jar</li>
        <li>mvn install:install-file -Dfile=xml-apis.jar -DartifactId=xml-apis -DgroupdId=xml-apis -Dversion=1.0 -Dpackaging=jar</li>
    </ul>
    </li>
<li> cd ..</li>
<li>mvn install</li>
<li>mvn clean compile assembly:single</li>
<li>cd target</li>
<li>java -Djava.library.path=/path/to/lib/directory -jar JarNameInTarget.jar</li>
</ol>
