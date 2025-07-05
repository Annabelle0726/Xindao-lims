package com.ruoyi.process.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.process.dto.ProcessMethodSearchNewArchivedDto;
import com.ruoyi.process.dto.ProcessMethodSearchNewBackupsDto;
import com.ruoyi.process.mapper.ProcessMethodSearchNewArchivedMapper;
import com.ruoyi.process.mapper.ProcessMethodSearchNewBackupsMapper;
import com.ruoyi.process.mapper.ProcessMethodSearchNewMapper;
import com.ruoyi.process.pojo.ProcessMethodSearchNew;
import com.ruoyi.process.pojo.ProcessMethodSearchNewArchived;
import com.ruoyi.process.pojo.ProcessMethodSearchNewBackups;
import com.ruoyi.process.service.ProcessMethodSearchNewBackupsService;
import com.ruoyi.process.service.ProcessMethodSearchNewService;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标准查新
 *
 * @author zhuo
 * @since 2024-11-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessMethodSearchNewServiceImpl extends ServiceImpl<ProcessMethodSearchNewMapper, ProcessMethodSearchNew> implements ProcessMethodSearchNewService {

    @Resource
    private ProcessMethodSearchNewArchivedMapper processMethodSearchNewArchivedMapper;
    @Resource
    private ProcessMethodSearchNewBackupsService processMethodSearchNewBackupsService;
    @Resource
    private ProcessMethodSearchNewBackupsMapper processMethodSearchNewBackupsMapper;

    /**
     * 新增标准查新
     *
     * @param processMethodSearchNewList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMethodSearchNew(List<ProcessMethodSearchNew> processMethodSearchNewList) {
        for (ProcessMethodSearchNew processMethodSearchNew : processMethodSearchNewList) {
            processMethodSearchNew.setIsNewStandard(1);
        }
        // 新增
        this.saveBatch(processMethodSearchNewList);
        return true;
    }

    /**
     * 标准查新列表
     *
     * @param processMethodSearchNew
     * @return
     */
    @Override
    public IPage<ProcessMethodSearchNew> pageMethodSearchNew(Page page, ProcessMethodSearchNewBackupsDto processMethodSearchNew) {
        String beginDate = processMethodSearchNew.getBeginDate();
        String endDate = processMethodSearchNew.getEndDate();
        processMethodSearchNew.setBeginDate(null);
        processMethodSearchNew.setEndDate(null);
        return baseMapper.pageMethodSearchNew(page, QueryWrappers.queryWrappers(processMethodSearchNew), beginDate, endDate);
    }

    /**
     * 标准查新导出
     *
     * @param archivedId
     * @param response
     */
    @Override
    public void exportMethodSearchNew(Integer archivedId, HttpServletResponse response) {
        // 查询标准查新存档信息
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ProcessMethodSearchNewArchived methodSearchNewArchived = processMethodSearchNewArchivedMapper.selectById(archivedId);
        Integer writeUserId = methodSearchNewArchived.getWriteUserId();
        Integer ratifyUserId = methodSearchNewArchived.getRatifyUserId();
        String writeDate = methodSearchNewArchived.getWriteTime() == null ?
                null : methodSearchNewArchived.getWriteTime().format(timeFormatter);
        String ratifyDate =methodSearchNewArchived.getRatifyTime() == null ?
                null :  methodSearchNewArchived.getRatifyTime().format(timeFormatter);
        // 查询存档备份
        List<ProcessMethodSearchNewBackups> methodSearchNewBackups = processMethodSearchNewBackupsMapper.selectList(Wrappers.<ProcessMethodSearchNewBackups>lambdaQuery()
                .eq(ProcessMethodSearchNewBackups::getArchivedId, archivedId));

        int index = 1;
        // 格式化参数
        List<ProcessMethodSearchNewBackupsDto> methodSearchNews = new ArrayList<>();
        for (ProcessMethodSearchNewBackups methodSearchNew : methodSearchNewBackups) {
            ProcessMethodSearchNewBackupsDto searchNewBackupsDto = new ProcessMethodSearchNewBackupsDto();
            BeanUtils.copyProperties(methodSearchNew, searchNewBackupsDto);
            // 是否是跟新标准
            if (methodSearchNew.getIsNewStandard().equals(1)) {
                searchNewBackupsDto.setIsNewStandardString("是");
            } else {
                searchNewBackupsDto.setIsNewStandardString("否");
            }

            // 备注
            if (methodSearchNew.getRemark() != null) {
                if (methodSearchNew.getRemark().equals(1)) {
                    searchNewBackupsDto.setRemarkString("替换");
                } else {
                    searchNewBackupsDto.setRemarkString("作废");
                }
            }
            switch (methodSearchNew.getSearchNewSource()) {
                case 0:
                    searchNewBackupsDto.setStandardNet("√");
                    break;
                case 1:
                    searchNewBackupsDto.setInformationOffices("√");
                    break;
                case 2:
                    searchNewBackupsDto.setStandardBookstore("√");
                    break;
                case 3:
                    searchNewBackupsDto.setOther("√");
                    break;
            }
            searchNewBackupsDto.setIndex(index);
            methodSearchNews.add(searchNewBackupsDto);
            index++;

        }
        // 查询签名地址
        String writeUserUrl = UserUtils.getUserSignatureUrl(writeUserId);
        String ratifyUserUrl = UserUtils.getUserSignatureUrl(ratifyUserId);

        //创建ExcelWriter 可以自动关流但还是手动关一次
        ExcelWriter excelWriter = null;

        try {
            // outputStream：要导出的文件的输出流
            OutputStream outputStream = response.getOutputStream();
            // 获取模版文件
            ClassPathResource classPathResource = new ClassPathResource("/static/excel/check-records.xlsx");
            // 使用模版文件的两种方式：
            // 	1、文件路径：.withTemplate(templateFileName)
            // 	2、输入流：.withTemplate(inputStream)
            // String templateFileName = classPathResource.getFile().getPath();
            InputStream inputStream = classPathResource.getInputStream();
            // 创建ExcelWriter
            excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
            // 获取第一个sheet页
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "标准查新导出").build();
            //excelWriter.fill() 这地方就是填充属性。
//            excelWriter.fill(methodSearchNews, fillConfig, writeSheet); // 这里fillConfig是配置文件
            excelWriter.fill(methodSearchNews, writeSheet);

            // 这里easy excel模板导出问题 会删除后面内容再进行进行填充
            // 所以list 后面还有数据 想办法手动写入
            // 手动创建一个list 存放数据 然后再进行填充
            // 这里List<Object>可以用对象代替 偷懒用list
            List<List<Object>> totalListList = ListUtils.newArrayList();
            List<Object> totalList = ListUtils.newArrayList();
            totalListList.add(totalList);
            // 第一列
            totalList.add("编制人：");
            // 第二列 设置签名
            if (StringUtils.isNotBlank(writeUserUrl)) {
                totalList.add(FileUtils.readFileToByteArray(new File(writeUserUrl)));
            }
            // 第三列
            totalList.add("日期：");
            // 第五列
            totalList.add(writeDate);
            // 第六列
            totalList.add("审核：");
            // 第七列
            if (StringUtils.isNotBlank(ratifyUserUrl)) {
                totalList.add(FileUtils.readFileToByteArray(new File(ratifyUserUrl)));
            }
            // 第八列
            totalList.add("日期：");
            // 第九列
            totalList.add(ratifyDate);
            // 这里追加是write 别和fill 搞错了
            excelWriter.write(totalListList, writeSheet);

            // 设置输出流格式以及文件名：
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(
                    "原材料检测信息导出", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".xlsx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 千万别忘记close关闭流
            if (excelWriter != null) {
                excelWriter.close();

            }
        }
    }


    /**
     * 导入标准查新
     *
     * @param file
     * @return
     */
    @Override
    public boolean importMethodSearchNew(MultipartFile file) {

        List<ProcessMethodSearchNewBackupsDto> searchNewDtoList = new ArrayList<>();

        try {
            // excel解析
            EasyExcel.read(file.getInputStream(), ProcessMethodSearchNewBackupsDto.class, new AnalysisEventListener<ProcessMethodSearchNewBackupsDto>() {
                @Override
                public void invoke(ProcessMethodSearchNewBackupsDto searchNewDto, AnalysisContext analysisContext) {
                    searchNewDtoList.add(searchNewDto);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();
            // 格式化查新对象
            List<ProcessMethodSearchNew> collect = searchNewDtoList.stream().map(searchNewDto -> {
                ProcessMethodSearchNew searchNew = new ProcessMethodSearchNew();
                BeanUtils.copyProperties(searchNewDto, searchNew);
                // 是否是更新
                if (StringUtils.isNotBlank(searchNewDto.getIsNewStandardString())) {
                    if (searchNewDto.getIsNewStandardString().equals("是")) {
                        searchNew.setIsNewStandard(1);
                    } else {
                        searchNew.setIsNewStandard(0);
                    }
                }
                // 查新记录
                if (StringUtils.isNotBlank(searchNewDto.getStandardNet())) {
                    searchNew.setSearchNewSource(0);
                } else if (StringUtils.isNotBlank(searchNewDto.getInformationOffices())) {
                    searchNew.setSearchNewSource(1);
                } else if (StringUtils.isNotBlank(searchNewDto.getIsNewStandardString())) {
                    searchNew.setSearchNewSource(2);
                } else if (StringUtils.isNotBlank(searchNewDto.getOther())) {
                    searchNew.setSearchNewSource(3);
                }

                // 备注
                if (StringUtils.isNotBlank(searchNewDto.getRemarkString())) {
                    if (searchNewDto.getRemarkString().equals("替换")) {
                        searchNew.setRemark(1);
                    } else if (searchNewDto.getRemarkString().equals("作废")) {
                        searchNew.setRemark(0);
                    }
                }

                return searchNew;
            }).collect(Collectors.toList());
            this.saveBatch(collect);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 新增标准查新审批流程
     * @param archived
     * @return
     */
    @Override
    public boolean addSearchNewArchived(ProcessMethodSearchNewArchived archived) {
        // 新增存档
        Integer userId = SecurityUtils.getUserId().intValue();
        archived.setWriteUserId(userId);
        archived.setWriteTime(LocalDateTime.now());
        processMethodSearchNewArchivedMapper.insert(archived);

        // 查询所有的标准查新
        List<ProcessMethodSearchNew> searchNewList = this.list();

        // 新增复制
        List<ProcessMethodSearchNewBackups> methodSearchNewBackups = searchNewList.stream().map(processMethodSearchNew -> {
            ProcessMethodSearchNewBackups backups = new ProcessMethodSearchNewBackups();
            BeanUtils.copyProperties(processMethodSearchNew, backups);
            backups.setArchivedId(archived.getArchivedId());
            return backups;
        }).collect(Collectors.toList());
        processMethodSearchNewBackupsService.saveBatch(methodSearchNewBackups);

        return true;
    }

    /**
     * 查询存档
     * @param page
     * @param archived
     * @return
     */
    @Override
    public IPage<ProcessMethodSearchNewArchivedDto> pageSearchNewArchived(Page page, ProcessMethodSearchNewArchivedDto archived) {
        return processMethodSearchNewArchivedMapper.pageSearchNewArchived(page, QueryWrappers.queryWrappers(archived));
    }

    /**
     * 查询存档备份列表
     * @param page
     * @param backups
     * @return
     */
    @Override
    public IPage<ProcessMethodSearchNewBackups> pageSearchNewBackups(Page page, ProcessMethodSearchNewBackups backups) {
        if (backups.getArchivedId() == null) {
            throw new ErrorException("缺少存档id");
        }
        return processMethodSearchNewBackupsMapper.pageSearchNewBackups(page, QueryWrappers.queryWrappers(backups));
    }

    /**
     * 存档批准
     * @param archived
     * @return
     */
    @Override
    public boolean ratifySearchNewArchivedr(ProcessMethodSearchNewArchived archived) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        processMethodSearchNewArchivedMapper.update(null, Wrappers.<ProcessMethodSearchNewArchived>lambdaUpdate()
                .eq(ProcessMethodSearchNewArchived::getArchivedId, archived.getArchivedId())
                .set(ProcessMethodSearchNewArchived::getRatifyUserId, userId)
                .set(ProcessMethodSearchNewArchived::getRatifyRemark, archived.getRatifyRemark())
                .set(ProcessMethodSearchNewArchived::getRatifyStatus, archived.getRatifyStatus())
                .set(ProcessMethodSearchNewArchived::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }
}

