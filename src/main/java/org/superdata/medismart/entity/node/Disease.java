package org.superdata.medismart.entity.node;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Property;
import org.superdata.medismart.entity.GraphNode;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)    // 考虑当前类的值的同时，检查从父类继承而来的值
public class Disease extends GraphNode {
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

    public Disease(Long id, String name) {
        super(id, name, "Disease");
    }

    public Disease(String name) {
        super(name, "Disease");
    }

    public Disease() {
        super(null, "Disease");
    }
}
