package org.superdata.medismart.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.entity.node.Disease;
import org.superdata.medismart.entity.request.GraphNodeReq;
import org.superdata.medismart.service.GraphService;
import org.superdata.medismart.utils.TestDataFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 图谱控制器测试类
 */
@SpringBootTest
@DisplayName("图谱控制器测试")
class GraphControllerTest extends BaseTest {

    @Mock
    private GraphService graphService;

    @InjectMocks
    private GraphController graphController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(graphController).build();
    }

    @Test
    @DisplayName("搜索节点 - 成功")
    void testSearch_success() {
        // 准备测试数据
        String keyword = "感冒";
        Integer pageNow = 1;
        Integer pageSize = 5;
        Map<String, List<Object>> searchResult = new HashMap<>();
        List<Object> diseases = new ArrayList<>();
        diseases.add(TestDataFactory.createDiseaseNode());
        searchResult.put("Disease", diseases);

        // Mock行为
        when(graphService.fuzzyQuery(keyword, pageNow, pageSize)).thenReturn(searchResult);

        // 执行测试
        ResponseResult result = graphController.search(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        Map<String, List<Object>> data = (Map<String, List<Object>>) result.getData();
        assertNotNull(data);
        assertTrue(data.containsKey("Disease"));
        assertEquals(1, data.get("Disease").size());

        verify(graphService, times(1)).fuzzyQuery(keyword, pageNow, pageSize);
    }

    @Test
    @DisplayName("搜索节点 - 无结果")
    void testSearch_noResults() {
        // 准备测试数据
        String keyword = "不存在的疾病";
        Integer pageNow = 1;
        Integer pageSize = 5;

        // Mock行为
        when(graphService.fuzzyQuery(keyword, pageNow, pageSize)).thenReturn(Collections.emptyMap());

        // 执行测试
        ResponseResult result = graphController.search(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        Map<String, List<Object>> data = (Map<String, List<Object>>) result.getData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("搜索节点 - 默认分页参数")
    void testSearch_defaultPagination() {
        // 准备测试数据
        String keyword = "感冒";
        Map<String, List<Object>> searchResult = new HashMap<>();

        // Mock行为
        when(graphService.fuzzyQuery(eq(keyword), eq(1), eq(5))).thenReturn(searchResult);

        // 执行测试 - 使用默认分页参数
        ResponseResult result = graphController.search(keyword, 1, 5);

        // 验证结果
        assertNotNull(result);
        verify(graphService, times(1)).fuzzyQuery(keyword, 1, 5);
    }

    @Test
    @DisplayName("精准查询节点 - 成功")
    void testList_success() {
        // 准备测试数据
        String keyword = "感冒";
        Integer pageNow = 1;
        Integer pageSize = 5;
        Map<String, List<Object>> listResult = new HashMap<>();
        List<Object> diseases = new ArrayList<>();
        diseases.add(TestDataFactory.createDiseaseNode());
        listResult.put("Disease", diseases);

        // Mock行为
        when(graphService.preciseQuery(keyword, pageNow, pageSize)).thenReturn(listResult);

        // 执行测试
        ResponseResult result = graphController.list(keyword, pageNow, pageSize);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        Map<String, List<Object>> data = (Map<String, List<Object>>) result.getData();
        assertNotNull(data);
        assertTrue(data.containsKey("Disease"));

        verify(graphService, times(1)).preciseQuery(keyword, pageNow, pageSize);
    }

    @Test
    @DisplayName("查询节点关联关系 - 成功")
    void testMap_success() {
        // 准备测试数据
        Long nodeId = 1L;
        Map<String, List<Object>> linkResult = new HashMap<>();
        List<Object> symptoms = new ArrayList<>();
        symptoms.add("发热");
        symptoms.add("咳嗽");
        linkResult.put("Symptom", symptoms);

        // Mock行为
        when(graphService.linkQuery(nodeId)).thenReturn(linkResult);

        // 执行测试
        ResponseResult result = graphController.map(nodeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        Map<String, List<Object>> data = (Map<String, List<Object>>) result.getData();
        assertNotNull(data);
        assertTrue(data.containsKey("Symptom"));
        assertEquals(2, data.get("Symptom").size());

        verify(graphService, times(1)).linkQuery(nodeId);
    }

    @Test
    @DisplayName("查询节点关联关系 - 无关联")
    void testMap_noLinks() {
        // 准备测试数据
        Long nodeId = 999L;

        // Mock行为
        when(graphService.linkQuery(nodeId)).thenReturn(Collections.emptyMap());

        // 执行测试
        ResponseResult result = graphController.map(nodeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        
        @SuppressWarnings("unchecked")
        Map<String, List<Object>> data = (Map<String, List<Object>>) result.getData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("通过文件上传节点 - 成功")
    void testAddByFile_success() {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "graphfile",
                "test.csv",
                "text/csv",
                "name,category\n感冒,Disease".getBytes()
        );

        // Mock行为
        when(graphService.addNodeByFile(any(MultipartFile.class))).thenReturn(true);

        // 执行测试
        ResponseResult result = graphController.addByFile(file);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue((Boolean) result.getData());

        verify(graphService, times(1)).addNodeByFile(any(MultipartFile.class));
    }

    @Test
    @DisplayName("添加节点 - 成功")
    void testAdd_success() {
        // 准备测试数据
        Disease disease = TestDataFactory.createDiseaseNode();
        GraphNodeReq req = new GraphNodeReq();
        req.setNodeInfo(disease);
        req.setNodeType("disease");

        // Mock行为
        when(graphService.saveNode(any(Disease.class), eq("Disease"))).thenReturn(true);

        // 执行测试
        ResponseResult result = graphController.add(req);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue((Boolean) result.getData());

        verify(graphService, times(1)).saveNode(any(Disease.class), eq("Disease"));
    }

    @Test
    @DisplayName("添加节点 - 节点类型格式化")
    void testAdd_nodeTypeFormatting() {
        // 准备测试数据
        Disease disease = TestDataFactory.createDiseaseNode();
        GraphNodeReq req = new GraphNodeReq();
        req.setNodeInfo(disease);
        req.setNodeType("DISEASE");

        // Mock行为
        when(graphService.saveNode(any(Disease.class), eq("Disease"))).thenReturn(true);

        // 执行测试
        ResponseResult result = graphController.add(req);

        // 验证结果 - 验证nodeType被格式化为首字母大写
        assertNotNull(result);
        assertEquals("Disease", req.getNodeType());

        verify(graphService, times(1)).saveNode(any(Disease.class), eq("Disease"));
    }

    @Test
    @DisplayName("更新节点 - 成功")
    void testUpdate_success() {
        // 准备测试数据
        Disease disease = TestDataFactory.createDiseaseNode();
        disease.setId(1L);
        GraphNodeReq req = new GraphNodeReq();
        req.setNodeInfo(disease);
        req.setNodeType("disease");

        // Mock行为
        when(graphService.saveNode(any(Disease.class), eq("Disease"))).thenReturn(true);

        // 执行测试
        ResponseResult result = graphController.update(req);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue((Boolean) result.getData());

        verify(graphService, times(1)).saveNode(any(Disease.class), eq("Disease"));
    }

    @Test
    @DisplayName("删除节点 - 成功")
    void testDelete_success() {
        // 准备测试数据
        Long nodeId = 1L;

        // Mock行为
        when(graphService.deleteNode(nodeId)).thenReturn(true);

        // 执行测试
        ResponseResult result = graphController.delete(nodeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue((Boolean) result.getData());

        verify(graphService, times(1)).deleteNode(nodeId);
    }

    @Test
    @DisplayName("删除节点 - 节点不存在")
    void testDelete_nodeNotFound() {
        // 准备测试数据
        Long nodeId = 999L;

        // Mock行为
        when(graphService.deleteNode(nodeId)).thenReturn(false);

        // 执行测试
        ResponseResult result = graphController.delete(nodeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertFalse((Boolean) result.getData());
    }
}
