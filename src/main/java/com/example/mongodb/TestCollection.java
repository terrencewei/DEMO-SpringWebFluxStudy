package com.example.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "test_collection")// the collection name in MongoDB
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCollection {

    @Id
    private String  id;
    private String  name;
    private boolean upload2S3;



    public TestCollection(String pName, boolean pUpload2S3) {
        name = pName;
        upload2S3 = pUpload2S3;
    }
}
