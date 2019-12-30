package com.vulner.bend_server.bean.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edb_author")
@Data
public class EdbAuthorPo {
    public String author_id;
    public String name;

}
