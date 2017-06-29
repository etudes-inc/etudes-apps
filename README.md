# etudes-apps
Etudes LTI Apps Framework and Apps

### Tooling

Maven 3.x

NPM / Node

### Building

Get the code

git checkout https://github.com/etudes.inc/etudes-apps.git

git checkout https://github.com/etudes.inc/etudes-mneme-react.git

Build the front end (see install.sh)

cd etudes-mneme-react && npm install && npm run build && rm -rf ../etudes-apps/assets/src/main/resources/mneme && cp -pr ~./build ../etudes-apps/assets/src/main/resources/mneme

Build the backend and package

cd etudes-apps && mvn clean install

Run

java -run etudes-apps/framework/framework-latest.jar server etudes-apps/framework/config.yml


