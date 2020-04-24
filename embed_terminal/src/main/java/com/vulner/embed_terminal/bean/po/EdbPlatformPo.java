package com.vulner.embed_terminal.bean.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "edb_platform")
@Data
public class EdbPlatformPo {
    public String platform_id;
    public String platform;
    public String display;

}
