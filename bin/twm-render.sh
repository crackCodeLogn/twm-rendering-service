APP_NAME="twm-rendering-service"
APP_VERSION="1.0-SNAPSHOT"
JAVA_PARAM="-Xmx251m"

BIN_PATH=$TWM_HOME_PARENT/TWM/$APP_NAME/bin     #TWM-HOME-PARENT :: exported in .bashrc
JAR_PATH=$BIN_PATH/../target/$APP_NAME-$APP_VERSION.jar

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', at '$JAR_PATH'"
java $JAVA_PARAM -jar $JAR_PATH
