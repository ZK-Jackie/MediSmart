package org.superdata.medismart.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.GraphNode;
import org.superdata.medismart.entity.node.*;
import org.superdata.medismart.mapper.neo4j.*;
import org.superdata.medismart.service.GraphService;
import org.superdata.medismart.mapper.NodeRepoFactory;
import org.superdata.medismart.utils.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService{
    @Resource
    private NodeRepoFactory nodeRepoFactory;

    /**
     * 根据名称模糊查询，需要提供模糊词与分页参数
     *
     * @param keyword the key word that user input to search
     * @return Map > key: label name > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> fuzzyQuery(String keyword, Integer pageNow, Integer pageSize) {
        Map<String, List<Object>> queryResult = nodeRepoFactory.fuzzyMatchByName(keyword);
        // TODO 分页模糊查询
//        //1.设置分页参数
//        PageHelper.startPage(pageNow, pageSize);
//        //2.将查询结果交给Page泛型
//        List<Emp> data = empMapper.getEmp(empPageInfo);
//        //3.包装数据
//        empPageInfo.setTableData(((Page<Emp>) data).getResult());
//        empPageInfo.setTotalSize(((Page<Emp>) data).getTotal());
//        //4.返回controller
//        return empPageInfo;
        return queryResult;
    }

    /**
     * 根据名称精准查询节点的详细信息，需要提供节点名称与分页参数
     *
     * @param keyword the key word that user input to search
     * @return Map > key: label name > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> preciseQuery(String keyword, Integer pageNow, Integer pageSize) {
        return nodeRepoFactory.preciseMatchByName(keyword);
    }

    /**
     * 精准查询，根据id查询节点和与之相连的节点
     *
     * @param id the id of the node
     * @return Map > key: name of edge > value: list of nodes
     */
    @Override
    public Map<String, List<Object>> linkQuery(Long id) {
        return nodeRepoFactory.detailMatchById(id);
    }

    @Override
    public Boolean addNodeByFile(GraphNode node) {
        // 1. 获取节点类型
        String label = getLabelFromNode(node);
        // 2. 插入
        NodeRepoFactory.getRepository(label).save(node);
        return true;
    }

    @Override
    public Boolean addNodeByFile(MultipartFile file) {
        return null;
    }

    @Override
    public Boolean saveNode(GraphNode node) {
        // 1. 获取节点类型
        String label = getLabelFromNode(node);
        // 2. 更新
        NodeRepoFactory.getRepository(label).save(node);
        if(node.getCategory().equals("Check")){
            CheckRepository repo = (CheckRepository) NodeRepoFactory.getRepository("Check");
            repo.save((Check) node);
        }else {
            NodeRepoFactory.getRepository(label).save(node);
        }
        return true;
    }

    @Override
    public Boolean deleteNode(Long id) {
        NodeRepoFactory.getRepository().deleteById(id);
        return true;
    }

    @Override
    public Boolean saveNode(Disease node, String nodeType) {
        switch (nodeType) {
            case "Check": {
                CheckRepository repo = (CheckRepository) NodeRepoFactory.getRepository("Check");
                Check n = new Check();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            case "Department": {
                DepartmentRepository repo = (DepartmentRepository) NodeRepoFactory.getRepository("Department");
                Department n = new Department();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            case "Disease": {
                DiseaseRepository repo = (DiseaseRepository) NodeRepoFactory.getRepository("Disease");
                repo.save(node);
                break;
            }
            case "Drug": {
                DrugRepository repo = (DrugRepository) NodeRepoFactory.getRepository("Drug");
                Drug n = new Drug();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            case "Food": {
                FoodRepository repo = (FoodRepository) NodeRepoFactory.getRepository("Food");
                Food n = new Food();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            case "Producer": {
                ProducerRepository repo = (ProducerRepository) NodeRepoFactory.getRepository("Producer");
                Producer n = new Producer();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            case "Symptom": {
                SymptomRepository repo = (SymptomRepository) NodeRepoFactory.getRepository("Symptom");
                Symptom n = new Symptom();
                n.setId(node.getId());
                n.setName(node.getName());
                n.setCategory(node.getCategory());
                repo.save(n);
                break;
            }
            default:
                log.error("未知的节点类型");
                throw new ServiceException("未知的节点类型");
        }
        return true;
    }

    private String getLabelFromNode(GraphNode node) {
        try{
            return (String) ObjectUtils.getProperty(node, "category");
        }catch (Exception e){
            e.printStackTrace();
            log.error("节点信息获取失败");
            return null;
        }
    }
}
