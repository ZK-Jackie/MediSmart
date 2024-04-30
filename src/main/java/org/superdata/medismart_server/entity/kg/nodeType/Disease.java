package org.superdata.medismart.entity.kg.nodeType;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Property;
import org.superdata.medismart.entity.kg.Node;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)    // 考虑当前类的值的同时，检查从父类继承而来的值
public class Disease extends Node {
    public final String category = "疾病";

    @Property("cause")
    public String cause;

    @Property("cure_department")
    public List<String> cureDepartment;

    @Property("cure_lasttime")
    public String cureLasttime;

    @Property("cure_way")
    public List<String> cureWay;

    @Property("cured_prob")
    public String curedProb;

    @Property("desc")
    public String desc;

    @Property("easy_get")
    public String easyGet;

    @Property("prevent")
    public String prevent;
}
