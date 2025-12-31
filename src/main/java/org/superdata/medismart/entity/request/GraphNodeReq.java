package org.superdata.medismart.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.node.Disease;
import org.superdata.medismart.entity.node.Drug;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphNodeReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点操作
     */
    private String operation;
    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点信息
     */
    private Disease nodeInfo;
}
