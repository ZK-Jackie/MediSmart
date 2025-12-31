package org.superdata.medismart.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.node.Disease;
import org.superdata.medismart.mapper.NodeRepoFactory;
import org.superdata.medismart.mapper.neo4j.DiseaseRepository;
import org.superdata.medismart.service.impl.GraphServiceImpl;
import org.superdata.medismart.utils.TestDataFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 图谱服务测试类
 */
@SpringBootTest
@DisplayName("图谱服务测试")
class GraphServiceTest extends BaseTest {

    @Mock
    private NodeRepoFactory nodeRepoFactory;

    @Mock
    private DiseaseRepository diseaseRepository;

    @InjectMocks
    private GraphServiceImpl graphService;

    private Disease testDisease;
    private Map<String, List<Object>> testQueryResult;

    @BeforeEach
    void setUp() {
        testDisease = TestDataFactory.createDiseaseNode();
        testQueryResult = new HashMap<>();
        List<Object> diseases = new ArrayList<>();
        diseases.add(testDisease);
        testQueryResult.put("Disease", diseases);
    }

    @Test
    @DisplayName("模糊查询 - 成功")
    void testFuzzyQuery_success() {
        // 准备测试数据
        String keyword = "感冒";
        Integer pageNow = 1;
        Integer pageSize = 5;

        // Mock行为
        when(nodeRepoFactory.fuzzyMatchByName(keyword)).thenReturn(testQueryResult);

        // 执行测试
        Map<String, List<Object>> result = graphService.fuzzyQuery(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("Disease"));
        assertEquals(1, result.get("Disease").size());
        verify(nodeRepoFactory, times(1)).fuzzyMatchByName(keyword);
    }

    @Test
    @DisplayName("模糊查询 - 无结果")
    void testFuzzyQuery_noResults() {
        // 准备测试数据
        String keyword = "不存在的疾病";
        Integer pageNow = 1;
        Integer pageSize = 5;

        // Mock行为
        when(nodeRepoFactory.fuzzyMatchByName(keyword)).thenReturn(Collections.emptyMap());

        // 执行测试
        Map<String, List<Object>> result = graphService.fuzzyQuery(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("精准查询 - 成功")
    void testPreciseQuery_success() {
        // 准备测试数据
        String keyword = "感冒";
        Integer pageNow = 1;
        Integer pageSize = 5;

        // Mock行为
        when(nodeRepoFactory.preciseMatchByName(keyword)).thenReturn(testQueryResult);

        // 执行测试
        Map<String, List<Object>> result = graphService.preciseQuery(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("Disease"));
        assertEquals(1, result.get("Disease").size());
        verify(nodeRepoFactory, times(1)).preciseMatchByName(keyword);
    }

    @Test
    @DisplayName("精准查询 - 无结果")
    void testPreciseQuery_noResults() {
        // 准备测试数据
        String keyword = "不存在的疾病";
        Integer pageNow = 1;
        Integer pageSize = 5;

        // Mock行为
        when(nodeRepoFactory.preciseMatchByName(keyword)).thenReturn(Collections.emptyMap());

        // 执行测试
        Map<String, List<Object>> result = graphService.preciseQuery(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("关联查询 - 成功")
    void testLinkQuery_success() {
        // 准备测试数据
        Long nodeId = 1L;
        Map<String, List<Object>> linkResult = new HashMap<>();
        List<Object> relatedSymptoms = new ArrayList<>();
        relatedSymptoms.add("发热");
        relatedSymptoms.add("咳嗽");
        linkResult.put("Symptom", relatedSymptoms);

        // Mock行为
        when(nodeRepoFactory.detailMatchById(nodeId)).thenReturn(linkResult);

        // 执行测试
        Map<String, List<Object>> result = graphService.linkQuery(nodeId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("Symptom"));
        assertEquals(2, result.get("Symptom").size());
        verify(nodeRepoFactory, times(1)).detailMatchById(nodeId);
    }

    @Test
    @DisplayName("关联查询 - 无关联")
    void testLinkQuery_noLinks() {
        // 准备测试数据
        Long nodeId = 999L;

        // Mock行为
        when(nodeRepoFactory.detailMatchById(nodeId)).thenReturn(Collections.emptyMap());

        // 执行测试
        Map<String, List<Object>> result = graphService.linkQuery(nodeId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("保存节点 - 成功保存疾病节点")
    void testSaveNode_disease_success() {
        // 准备测试数据
        Disease disease = TestDataFactory.createDiseaseNode();
        String nodeType = "Disease";

        // Mock行为
        when(diseaseRepository.save(any(Disease.class))).thenReturn(disease);
        when(NodeRepoFactory.getRepository("Disease")).thenReturn(diseaseRepository);

        // 执行测试
        Boolean result = graphService.saveNode(disease, nodeType);

        // 验证结果
        assertTrue(result);
        verify(diseaseRepository, times(1)).save(any(Disease.class));
    }

    @Test
    @DisplayName("保存节点 - 未知节点类型")
    void testSaveNode_unknownType() {
        // 准备测试数据
        Disease disease = TestDataFactory.createDiseaseNode();
        String nodeType = "UnknownType";

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            graphService.saveNode(disease, nodeType);
        });

        assertEquals("未知的节点类型", exception.getMessage());
    }

    @Test
    @DisplayName("删除节点 - 成功")
    void testDeleteNode_success() {
        // 准备测试数据
        Long nodeId = 1L;

        // Mock行为
        when(NodeRepoFactory.getRepository()).thenReturn(diseaseRepository);
        doNothing().when(diseaseRepository).deleteById(nodeId);

        // 执行测试
        Boolean result = graphService.deleteNode(nodeId);

        // 验证结果
        assertTrue(result);
        verify(diseaseRepository, times(1)).deleteById(nodeId);
    }

    @Test
    @DisplayName("删除节点 - 节点不存在")
    void testDeleteNode_notFound() {
        // 准备测试数据
        Long nodeId = 999L;

        // Mock行为
        when(NodeRepoFactory.getRepository()).thenReturn(diseaseRepository);
        doThrow(new RuntimeException("节点不存在")).when(diseaseRepository).deleteById(nodeId);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            graphService.deleteNode(nodeId);
        });
    }

    @Test
    @DisplayName("保存GraphNode节点 - 成功")
    void testSaveGraphNode_success() {
        // 准备测试数据
        GraphNode node = new GraphNode();
        node.setId(1L);
        node.setName("测试节点");
        node.setCategory("Disease");

        // Mock行为
        when(NodeRepoFactory.getRepository("Disease")).thenReturn(diseaseRepository);
        when(diseaseRepository.save(any())).thenReturn(node);

        // 执行测试
        Boolean result = graphService.saveNode(node);

        // 验证结果
        assertTrue(result);
    }

    @Test
    @DisplayName("添加节点通过文件 - GraphNode")
    void testAddNodeByFile_graphNode() {
        // 准备测试数据
        GraphNode node = new GraphNode();
        node.setId(1L);
        node.setName("测试节点");
        node.setCategory("Disease");

        // Mock行为
        when(NodeRepoFactory.getRepository("Disease")).thenReturn(diseaseRepository);
        when(diseaseRepository.save(any())).thenReturn(node);

        // 执行测试
        Boolean result = graphService.addNodeByFile(node);

        // 验证结果
        assertTrue(result);
    }
}
