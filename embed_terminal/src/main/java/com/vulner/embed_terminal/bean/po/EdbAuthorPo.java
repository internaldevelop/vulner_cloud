package com.vulner.embed_terminal.bean.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edb_author")
@Data
public class EdbAuthorPo {
    public String author_id;
    public String name;

}
