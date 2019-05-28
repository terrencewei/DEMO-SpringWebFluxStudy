# DEMO-SpringWebFluxStudy
Demo project for Spring Web Flux and Amazon S3

## S3 APIs
### POST
#### http://localhost:8081/s3/upload
Upload file to S3

Body: form-data

key: files

value: type: File, the upload files, support multiple files
```
curl -X POST \
  http://localhost:8081/s3/upload \
  -H 'Accept: */*' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Host: localhost:8081' \
  -H 'cache-control: no-cache' \
  -H 'content-length: 1299' \
  -H 'content-type: multipart/form-data;' \
  -F files=@/home/terrence/test.png \
  -F files=@/home/terrence/test2.png
```

### GET
#### View
View file such as image directly in browser

`http://localhost:8081/s3/view/<file name>`

e.g.

`http://localhost:8081/s3/download/test.png`
#### Download
Trigger auto download file in browser

`http://localhost:8081/s3/download/<file name>`

e.g.

`http://localhost:8081/s3/download/test.png`

## Nginx
### Image Resize and Cache
View link in browser to invoke image resize and download image from S3 APIs

`http://localhost:8082/img/<width>x<height>/<file name in S3>`

e.g.

`http://localhost:8082/img/100x200/test.png`