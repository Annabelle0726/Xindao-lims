package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.pojo.ProcessTotaldeal;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 检测或校准物品的处置总表(历史) 服务类
 * </p>
 *
 * @author
 * @since 2024-11-02 03:59:09
 */
public interface ProcessTotaldealService extends IService<ProcessTotaldeal> {

    IPage<ProcessTotaldeal> pageProcessTotaldeal(Page page, ProcessTotaldeal processTotaldeal);

    int checkProcessTotaldeal(Integer id, String state);

    int submitProcessTotaldeal(Integer id);

    int ratifyProcessTotaldeal(Integer id, String state);

    int addProcessTotaldeal(String month);

    void exportProcessTotaldeal(Integer id, HttpServletResponse response);
}
