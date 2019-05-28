# DEMO-SpringWebFluxStudy
Demo project for Spring Web Flux and Amazon S3

## S3 APIs
### POST
#### http://localhost:8081/s3/upload
Upload file to S3

Body: form-data

key1: file

value1: type: File, the upload file

key2: name

value2: type: Text, the upload file name
```
curl -X POST \
  http://localhost:8081/s3/upload \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Host: localhost:8081' \
  -H 'content-length: 619' \
  -H 'content-type: multipart/form-data;' \
  -F file=@/home/terrence/test.png \
  -F name=test.png
```

### GET
#### http://localhost:8081/s3/view/test.png
View file such as image directly in browswer

#### http://localhost:8081/s3/download/test.png
Trigger auto download file in browswer