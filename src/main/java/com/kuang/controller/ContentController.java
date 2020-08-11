package com.kuang.controller;

import com.kuang.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by wonders on 2020-07-17 09:38
 */
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {

        return contentService.parseContent(keyword);
    }


    @GetMapping("/parseMT")
    public Boolean parseMT(String keyword) throws Exception {

        return contentService.parseMContent(keyword);
    }


    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword,
                                            @PathVariable("pageNo") int pageNo,
                                            @PathVariable("pageSize") int pageSize) throws Exception {

        return contentService.highlightSearchPageList(keyword,pageNo,pageSize);
    }

    @GetMapping("/searchList/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> searchList(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") int pageNo,
                                           @PathVariable("pageSize") int pageSize) throws Exception {
        List<Map<String, Object>> maps = contentService.searchPageList(keyword, pageNo, pageSize);
        System.out.println(maps.size());
        return contentService.searchPageList(keyword,pageNo,pageSize);
    }


    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
