package org.superdata.medismart.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.superdata.medismart.entity.GraphNode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphNodeReq{
    /**
     * 节点操作
     * */
    private String operation;
    /**
     * 节点类型
     * */
    private String nodeType;

    /**
     * 节点信息
     * */
    private Object nodeInfo;
}
