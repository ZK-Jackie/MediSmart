package org.superdata.medismart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.service.KgService;

@Slf4j
@RestController
public class KgController {
    @Autowired
    private KgService kgService;

    @GetMapping("/kg/search")
    public ResponseResult searchKGNodes(String keyword,
                                        @RequestParam(defaultValue = "1") Integer pageNow,
                                        @RequestParam(defaultValue = "5") Integer pageSize) {
        log.info("用户搜索 “{}” 信息，第 {} 页，每页 {} 条", keyword, pageNow, pageSize);
        return ResponseResult.success(kgService.fuzzyQuery(keyword, pageNow, pageSize));
    }

    @GetMapping("/kg/list")
    public ResponseResult listNode(String keyword,
                                   @RequestParam(defaultValue = "1") Integer pageNow,
                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        log.info("用户查询 “{}” 信息，第 {} 页，每页 {} 条", keyword, pageNow, pageSize);
        return ResponseResult.success(kgService.preciseQuery(keyword, pageNow, pageSize));
    }

    @GetMapping("/kg/map")
    public ResponseResult mapNode(Long id) {
        log.info("用户查询结点 “{}” 的连接关系", id);
        return ResponseResult.success(kgService.linkQuery(id));
    }

}