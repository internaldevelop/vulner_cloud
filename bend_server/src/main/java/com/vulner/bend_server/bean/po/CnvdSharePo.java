package com.vulner.bend_server.bean.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "cnvd_share")
@Data
public class CnvdSharePo {
    public String id;
    public String title;
    public String number;
    public String openTime;
    public String submitTime;
    public String serverity;
    public String isEvent;
    public String discovererName;
    public String description;
    public String patchName;
    public String patchDescription;
    public String vulType;
    public String formalWay;
    public String referenceLink;
    public List<ProductPo> products;
    public List<CvePo> cves;
    public List<BidPo> bids;

    @Data
    public class ProductPo {
        public String product;
    }

    @Data
    public class CvePo {
        public String cveNumber;
    }

    @Data
    public class BidPo {
        public String bidNumber;
    }

}
