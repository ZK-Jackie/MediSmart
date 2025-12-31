package org.superdata.medismart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.common.constant.Constants;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.request.GraphNodeReq;
import org.superdata.medismart.mapper.NodeRepoFactory;
import org.superdata.medismart.service.GraphService;
import org.superdata.medismart.utils.ObjectUtils;
import org.superdata.medismart.utils.StringUtils;
import org.superdata.medismart.utils.neo4j.GraphNodeFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestGraph {
    @Resource
    private GraphService graphService;
//    @Resource
//    private CheckRepository checkRepository;
//    @Resource
//    private GraphRepository graphRepository;
//    @Resource
//    private NodeRepository nodeRepository;
    @Resource
    private NodeRepoFactory nodeRepoFactory;

    @Test
    public void testAdd() throws Exception {
//        GraphNodeReq req = new GraphNodeReq();
//        req.setNodeType("check");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("name", "test");
//        req.setNodeInfo(properties);
//        // 格式化nodeType为首字母大写的字符串
//        req.setNodeType(StringUtils.capitalizeFirstLetter(StringUtils.uncapitalize(req.getNodeType())));
//        // 根据nodeType，将nodeInfo转为GraphNode的子类实例
//        GraphNode node = (GraphNode) GraphNodeFactory.me().getGraphNode(
//                req.getNodeType(),
//                ObjectUtils.createAndLoadObject(
//                        Constants.NODE_ENTITY_PACKAGE+"."+req.getNodeType(),
//                        properties
//                )
//        );
//        graphService.addNodeByFile(node);
    }

    @Test
    public void testFind(){
        Map<String, List<Object>> queryResult = nodeRepoFactory.fuzzyMatchByName("病");
    }

}
