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
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class S3RouterHandler {

    @Autowired
    private S3Util s3Util;



    public Mono<ServerResponse> upload2S3(ServerRequest request) {
        return request
                .multipartData()
                .flatMap(pMultiValueMap -> {
                    // get input
                    String name = ((FormFieldPart) pMultiValueMap.getFirst("name")).value();
                    List<Part> file = pMultiValueMap.get("file");
                    // processing
                    Flux<String> result = Flux
                            .fromIterable(file)
                            .cast(FilePart.class)
                            .flatMap(pFilePart -> {
                                Flux<String> contentParseResult = pFilePart
                                        .content()
                                        .flatMap(pDataBuffer -> {
                                            boolean uploadResult = false;
                                            try {
                                                byte[] data = new byte[pDataBuffer.readableByteCount()];
                                                pDataBuffer.read(data);
                                                uploadResult = s3Util.putObject(name, data);
                                                log.info("Upload to S3 result:{}", uploadResult);
                                            } catch (Exception e) {
                                                log.error("Upload to S3 failed", e);
                                            }
                                            return Mono.just(Boolean.toString(uploadResult));
                                        });
                                return contentParseResult;
                            });
                    return ok()
                            .contentType(org.springframework.http.MediaType.APPLICATION_STREAM_JSON)
                            .body(result, String.class);
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