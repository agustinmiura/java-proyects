# Kotlin Spring boot application with Dynamo DB .

### Instructions 

* Import the proyect in Intellij and setup the JDk version 17.
* Set the properties file .
* Use the following environment variables : 
``` 
AWS_ACCESS_KEY=AWS_ACCESS_KEY;AWS_SECRET_KEY=AWS_SECRET_KEY;AWS_REGION=AWS_REGION
```
* Set the **application.properties** like this :
``` 
spring.main.banner-mode=off
logging.level.org.springframework=INFO

spring.devtools.restart.enabled=false

#use your aws credentials here
aws.access-key = ${AWS_ACCESS_KEY}
aws.access-secret = ${AWS_SECRET_KEY}
aws.region = ${AWS_REGION}

dynamo.table = ${DYNAMO_TABLE}
```
* Setup the Dynamo database .
* Setup the users .
* Start application .

