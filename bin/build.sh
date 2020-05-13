 #!/bin/bash
mvn clean install -Dmaven.test.skip=true
echo "------- MAVEN BUILD COMPLETED -------------- "
echo "------- START TO BUILD DOCKER IMAGE-------- "
docker build -t gooa.cc/facecard:latest .