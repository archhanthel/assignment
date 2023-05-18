package com.speer.assignment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String title;

    @TextIndexed
    private String content;
    private List<String> sharedWith;

}
