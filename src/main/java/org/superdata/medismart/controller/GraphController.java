package org.superdata.medismart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.entity.request.GraphNodeReq;
import org.superdata.medismart.service.GraphService;
import org.superdata.medismart.utils.StringUtils;

import javax.annotation.Resource;

@Slf4j
@RestController
public class GraphController {
    @Resource
    private GraphService graphService;

    @GetMapping("/kg/search")
    public ResponseResult search(String keyword,
                                 @RequestParam(defaultValue = "1") Integer pageNow,
                                 @RequestParam(defaultValue = "5") Integer pageSize) {
        log.info("用户搜索 “{}” 信息，第 {} 页，每页 {} 条", keyword, pageNow, pageSize);
        return ResponseResult.success(graphService.fuzzyQuery(keyword, pageNow, pageSize));
    }

    @GetMapping("/kg/list")
    public ResponseResult list(String keyword,
                               @RequestParam(defaultValue = "1") Integer pageNow,
                               @RequestParam(defaultValue = "5") Integer pageSize) {
        log.info("用户查询 “{}” 信息，第 {} 页，每页 {} 条", keyword, pageNow, pageSize);
        return ResponseResult.success(graphService.preciseQuery(keyword, pageNow, pageSize));
    }

    @GetMapping("/kg/map")
    public ResponseResult map(Long id) {
        log.info("用户查询结点 “{}” 的连接关系", id);
        return ResponseResult.success(graphService.linkQuery(id));
    }

    @PutMapping("/kg/upload")
    public ResponseResult addByFile(@RequestParam("graphfile") MultipartFile file) {
        log.info("用户上传文件 “{}”", file.getOriginalFilename());
        return ResponseResult.success(graphService.addNodeByFile(file));
    }

    @PostMapping("/kg/add")
    public ResponseResult add(@RequestBody GraphNodeReq req) {
        log.info("用户添加结点 “{}”", req);
        // 格式化nodeType为首字母大写的字符串
        req.setNodeType(StringUtils.capitalizeFirstLetter(StringUtils.uncapitalize(req.getNodeType())));
        // 根据nodeType，将nodeInfo转为GraphNode的子类实例
        return ResponseResult.success(graphService.saveNode(req.getNodeInfo(), req.getNodeType()));
    }

    @PutMapping("/kg/update")
    public ResponseResult update(@RequestBody GraphNodeReq req) {
        log.info("用户更新结点 “{}”", req);
        // 格式化nodeType为首字母大写的字符串
        req.setNodeType(StringUtils.capitalizeFirstLetter(StringUtils.uncapitalize(req.getNodeType())));
        return ResponseResult.success(graphService.saveNode(req.getNodeInfo(), req.getNodeType()));
    }

    @DeleteMapping("/kg/delete/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        log.info("用户删除结点 “{}”", id);
        return ResponseResult.success(graphService.deleteNode(id));
    }

}