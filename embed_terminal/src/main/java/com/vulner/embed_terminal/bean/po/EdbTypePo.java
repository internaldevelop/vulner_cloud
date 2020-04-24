package com.vulner.embed_terminal.bean.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edb_type")
@Data
public class EdbTypePo {
    public String type_id;
    public String name;

}
