package com.ylz.log.elk.manage.controller;

import com.ylz.log.elk.base.util.LoginInfoUtil;
import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;
import com.ylz.log.elk.manage.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/indexManage")
public class IndexManageController {

    @Autowired
    private IndexService indexService;

    @RequestMapping("/dataSearch")
    public String index(Map<String, Object> map) {

        Map<String, Object> esFieldmap = indexService.esFieldMap();

        map.put("esFieldmap", esFieldmap);

        return "elk/data_search";
    }

    @RequestMapping("/queryByEs")
    @ResponseBody
    public Map<String, Object> queryByEs(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "index") String index,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "field", defaultValue = "") String field,
            @RequestParam(value = "searchContent", defaultValue = "") String searchContent
    ) {

        return indexService.queryByEs(page, pageSize, index, type, field, searchContent);
    }

    @RequestMapping("/listField")
    @ResponseBody
    public List<String> listField(
            @RequestParam("index") String index,
            @RequestParam(value = "type", defaultValue = "0") String type
    ) {
        return indexService.listField(index, type);
    }

    @RequestMapping("/dataManage")
    public String dataManage(Map<String, Object> map) {
        log.info("contorller: dataManage");

        Integer num = 1/0;

        List<MultiIndexBean> multiIndexList = indexService.listMultiIndex();
        List<String> indexList = indexService.listIndex();

        map.put("multiIndexList", multiIndexList);
        map.put("indexList", indexList);

        return "elk/data_manage";
    }

    /**
     * 列举所有的索引与对应的字段 Map index field
     *
     * @param index
     * @param type  判断是否是组合索引 1 是 0 否
     * @return
     */
    @RequestMapping("/listReflectField")
    @ResponseBody
    public List<Map<String, Object>> listReflectField(
            @RequestParam(value = "index", defaultValue = "") String index,
            @RequestParam(value = "type", defaultValue = "0") String type
    ) {

        return indexService.listReflectField(index, type);
    }

    @RequestMapping(value = "/saveMultiIndex")
    @ResponseBody
    public Map<String, Object> saveMultiIndex(
            @RequestParam("multiIndex") String multiIndex,
            @RequestParam("index") String index
    ) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = indexService.saveMultiIndex(multiIndex, Arrays.asList(index.split(",")));

            jsonMap.put("flag", flag);
            jsonMap.put("type", "1");
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
            jsonMap.put("errorMsg", e.getMessage());
        }

        return jsonMap;
    }

    @RequestMapping("/hasExist")
    @ResponseBody
    public Map<String, Object> hasExist(@RequestParam("multiIndex") String multiIndex) {
        Map<String, Object> jsonMap = indexService.hasExist(multiIndex);

        return jsonMap;
    }

    /**
     * 判断数据加载错误是否是因为组合索引对应的es索引不存在
     * 是，整理成信息提示
     * 否，不吭声，系统异常
     *
     * @param index
     * @param type  判断是否是组合索引 1 是 0 否
     * @return
     */
    @RequestMapping("/dealNotIndex")
    @ResponseBody
    public Map<String, Object> dealNotIndex(
            @RequestParam("index") String index,
            @RequestParam("type") String type
    ) {

        return indexService.dealNotIndex(index, type);
    }

    @RequestMapping("/delMultiRelIndex")
    @ResponseBody
    public Map<String, Object> delMultiRelIndex(@RequestParam("index") String index) {
        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = indexService.delMultiRelIndex(Arrays.asList(index.split(",")));

        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/delMultiIndex")
    @ResponseBody
    public Map<String, Object> delMultiIndex(@RequestParam("multiIndex") String multiIndex) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = indexService.delMultiIndex(multiIndex);

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
            jsonMap.put("errorMsg", e.getMessage());
        }

        return jsonMap;
    }

    @RequestMapping("/getUserCollIndexBean")
    @ResponseBody
    public UserCollIndexBean getUserCollIndexBean() {
        UserCollIndexBean userCollIndexBean = indexService.getUserCollIndexBean(LoginInfoUtil.getUserId());

        if (userCollIndexBean == null) {
            userCollIndexBean = new UserCollIndexBean();
        }

        return userCollIndexBean;
    }

    @RequestMapping("/dealCollIndex")
    @ResponseBody
    public Map<String, Object> dealCollIndex(
            @RequestParam("index") String index,
            @RequestParam("type") String type,
            @RequestParam("action") String action
    ) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = indexService.dealCollIndex(index, type, action);

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", false);
            jsonMap.put("errorMsg", e.getMessage());
        }

        return jsonMap;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {

        return indexService.test();
    }
}
