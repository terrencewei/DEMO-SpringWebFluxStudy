package com.example.amazon.s3;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.mongodb.MongoRepository;
import com.example.mongodb.TestCollection;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class S3RouterHandler {

    @Autowired
    private S3Util          s3Util;
    @Autowired
    private MongoRepository mongoRepository;



    public Mono<ServerResponse> upload2S3(ServerRequest request) {
        return request
                .multipartData()
                .flatMap(pMultiValueMap -> {
                    // get input
                    List<Part> file = pMultiValueMap.get("files");
                    // dp upload
                    Flux<TestCollection> uploadResult = Flux
                            .fromIterable(file)
                            .cast(FilePart.class)
                            .flatMap(pFilePart -> {
                                String fileName = pFilePart.filename();
                                return pFilePart
                                        .content()
                                        .flatMap(pDataBuffer -> {
                                            boolean success = false;
                                            try {
                                                byte[] data = new byte[pDataBuffer.readableByteCount()];
                                                pDataBuffer.read(data);
                                                success = s3Util.putObject(fileName, data);
                                                log.info("Upload file:{} to S3 result:{}", fileName, success);
                                            } catch (Exception e) {
                                                log.error("Upload to S3 failed", e);
                                            }
                                            return Mono.just(new TestCollection(fileName, success));
                                        });
                            })
                            .flatMap(pTestCollection -> {
                                // save to mongo before upload, just for test mongo
                                Mono<TestCollection> mongoResult = mongoRepository.save(pTestCollection);
                                log.info("Save to Mongo result:{}", pTestCollection);
                                return mongoResult;
                            });
                    return ok()
                            .contentType(org.springframework.http.MediaType.APPLICATION_STREAM_JSON)
                            .body(uploadResult, TestCollection.class);
                });
    }



    public Mono<ServerResponse> downloadFromS3(ServerRequest request) {
        String name = request.pathVariable("name");
        InputStreamResource inputStreamResource = new InputStreamResource(s3Util.getObjectAsInputStream(name));
        return ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(BodyInserters.fromResource(inputStreamResource));

    }



    public Mono<ServerResponse> viewFromS3(ServerRequest request) {
        String name = request.pathVariable("name");
        //get the image from s3 in byte[]
        byte[] i = s3Util.getObjectAsByteArray(name);
        ByteArrayResource byteArrayResource = new ByteArrayResource(i);
        //use data Buffer to wrap the image in byte array
        DefaultDataBuffer buffer = new DefaultDataBufferFactory().wrap(i);
        return ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromDataBuffers(Flux.just(buffer)));

    }

}