root=$(date +'%Y-%d-%mT%H%M')_$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1).jar
echo "previous kosign-push-api-0.0.1.jar => $root"
mv kosign-push-api/build/libs/kosign-push-api-0.0.1.jar backup-root/$root
gradle bootJar
# mv bnk-web/build/libs/kosign-push-api-0.0.1.jar bnk-web/build/libs/ROOT.war