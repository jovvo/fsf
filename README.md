
###### Building and running the project
Build project with: mvn clean package 
Inside target directory run project with: java -jar fsf-0.0.1-SNAPSHOT.jar --file.upload.directory=/tmp/file-upload-2

###### Testing database state behavior:
In memory H2 database is configured so after application restart all data will be deleted.

###### Testing api:
Please use "fsf api.postman_collection" for easier testing with Postman

###### Testing user registration:
POST http://localhost:8080/fsf/register

Request body example:
{
        "email": "jsfsfuser1@gmail.com",
        "password": "mypass1*"
}

This request will create user in table USER. 
Unique key is email property, so try to test repeat of this request and it should fail with primary key violation.


###### Testing file upload 
Authorization is required.	

POST http://localhost:8080/fsf/api/file

Request body example:
Upload file with e.g. Postman application

This request should transfer file to configured file upload location.

###### Testing find file 
Authorization is required.	
Take generated file id from file system. Copy/paste file name, e.g. 1afae1dd-b7cb-4914-9f42-f454524a4df8

GET http://localhost:8080/fsf/api/file/1afae1dd-b7cb-4914-9f42-f454524a4df8

###### Testing share file
Authorization is required.	
POST http://localhost:8080/fsf/api/share

Request body example:
{
        "email": "user2@mail.com",
        "fileId": "ad093b1c-e9db-4f97-8215-be9a6bcb7b86"
}

This request should grant access for user2@mail.com to file ad093b1c-e9db-4f97-8215-be9a6bcb7b86.


###### Testing file permissions
Authorization is required.	
GET http://localhost:8080/fsf/api/file

This request should return owned and shared files for user.

