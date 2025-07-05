package com.ruoyi.inspect.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.PushOADto;
import com.ruoyi.inspect.dto.UnqualifiedHandlerDto;
import com.ruoyi.inspect.pojo.CommonOa;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandler;
import com.ruoyi.inspect.vo.UnqualifiedHandlerVO;

import java.util.List;
import java.util.Map;

/**
* @author 27233
* @description 针对表【ins_unqualified_handler(不合格处理表)】的数据库操作Service
* @createDate 2024-07-31 13:38:14
*/
public interface InsUnqualifiedHandlerService extends IService<InsUnqualifiedHandler> {

    IPage<UnqualifiedHandlerVO> pageList(Page page, UnqualifiedHandlerDto unqualifiedHandlerDto);

    Result pushOA(PushOADto pushOADto);

    void unqualifiedHandlerOACallback(Long oaWorkId, String checkResult, JSONArray objects);

    List<CommonOa> getOaFlow(Integer id);

    boolean addUnqualifiedHandler(UnqualifiedHandlerDto unqualifiedHandlerDto);

    boolean updateUnqualifiedHandler(UnqualifiedHandlerDto unqualifiedHandlerDto);

    UnqualifiedHandlerDto getUnqualifiedHandler(Integer id);

    boolean deleteUnqualifiedHandler(Integer id);
}
