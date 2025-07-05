package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StandardMethod;
import com.ruoyi.basic.pojo.StandardMethodList;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【standard_method(标准方法)】的数据库操作Service
* @createDate 2024-03-03 19:21:41
*/
public interface StandardMethodService extends IService<StandardMethod> {

    IPage<StandardMethod> selectStandardMethodList(Page page, StandardMethod standardMethod);

    List<StandardMethod> selectStandardMethods();

    int addStandardMethod(StandardMethod standardMethod);

    int delStandardMethod(Integer id);

    int upStandardMethod(StandardMethod standardMethod);

    Map<String, List<?>> selectsStandardMethodByFLSSM(String tree);

    List<StandardMethodList> selectStandardMethodEnum();

}
