package com.ruoyi.require.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.SuppliersDirectoryContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务和供应品采购目录 服务类
 * </p>
 *
 * @author
 * @since 2024-12-17 06:14:51
 */
public interface SuppliersDirectoryContentsService extends IService<SuppliersDirectoryContents> {

    ArrayList<SuppliersDirectoryContents> directoryListing();
    List<Map<String, Object>> getSuppliersDirectoryContentsNodeNames();
}
