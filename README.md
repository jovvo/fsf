
###### Building and running the project
Build project with: mvn clean package 
Inside target directory run project with: java -jar fsf-0.0.1-SNAPSHOT.jar --file.upload.directory=/tmp/file-upload-2

###### Testing database state behavior:
In memory H2 database is configured so after application restart all data will be deleted.


###### Testing user registration:

Http method:
POST http://localhost:8080/fsf/register

Headers:
Content-Type: application/json


Request body example:
{
        "email": "jsfsfuser1@gmail.com",
        "password": "mypass1*"
}


This request will create user in table USER. 
Unique key is email property, so try to test repeat of this request and it should fail with primary key violation.


###### Testing file upload 
Authorization is required.	

Http method:
POST http://localhost:8080/fsf/register

Headers:
Authorization: Basic YWRtaW46cGFzc3dvcmQ=

Request body example:
Upload file with e.g. Postman application

###### Testing find file 
Take generated file id from file system. Copy/paste file name, e.g. 1afae1dd-b7cb-4914-9f42-f454524a4df8
Authorization is required.	

GET http://localhost:8080/fsf/api/file/1afae1dd-b7cb-4914-9f42-f454524a4df8

Headers:
Authorization: Basic YWRtaW46cGFzc3dvcmQ=


###### Testing share file
POST http://localhost:8080/fsf/api/share

Headers:
Authorization: Basic YWRtaW46cGFzc3dvcmQ=

Request body example:
{
        "email": "jsfsfuser2@gmail.com",
        "fileId": "ad093b1c-e9db-4f97-8215-be9a6bcb7b86"
}


