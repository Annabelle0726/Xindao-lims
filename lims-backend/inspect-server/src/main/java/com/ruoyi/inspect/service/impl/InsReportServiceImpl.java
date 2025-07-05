package com.ruoyi.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Pictures;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.common.constant.InsOrderTypeConstants;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.config.WechatProperty;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.api.IfsApiUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.InsReportExport;
import com.ruoyi.inspect.dto.ReportPageDto;
import com.ruoyi.inspect.mapper.*;
import com.ruoyi.inspect.pojo.*;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.InsReportService;
import com.ruoyi.inspect.mapper.InsUnqualifiedHandlerMapper;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Administrator
 * @description 针对表【ins_report(检验报告)】的数据库操作Service实现
 * @createDate 2024-03-17 22:10:02
 */
@Service
@Slf4j
public class InsReportServiceImpl extends ServiceImpl<InsReportMapper, InsReport>
        implements InsReportService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private InsReportMapper insReportMapper;
    @Resource
    private InsOrderStateMapper insOrderStateMapper;
    @Resource
    private InsProductMapper insProductMapper;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Value("${wordUrl}")
    private String wordUrl;
    @Value("${file.path}")
    private String imgUrl;

    @Resource
    private InsOrderMapper insOrderMapper;
    @Resource
    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;
    @Resource
    private InsUnqualifiedHandlerMapper insUnqualifiedHandlerMapper;
    @Resource
    private InsSampleMapper insSampleMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InsOrderService insOrderService;
    @Resource
    private WechatProperty wechatProperty;
    @Resource
    private InsUnqualifiedRetestProductMapper insUnqualifiedRetestProductMapper;
    @Resource
    private IfsApiUtils ifsApiUtils;
    @Resource
    private InsSampleUserMapper insSampleUserMapper;


    @Override
    public Map<String, Object> pageInsReport(Page page, ReportPageDto reportPageDto) {
        Map<String, Object> map = new HashMap<>();
        // todo: 仅看自己
        //获取当前人所属实验室id
        String laboratory = null;

        Integer createOrderUser = reportPageDto.getCreateOrderUser();
        String queryStatus = reportPageDto.getQueryStatus();
        reportPageDto.setQueryStatus(null);
        reportPageDto.setCreateOrderUser(null);

        map.put("body", insReportMapper.pageInsReport(page,
                QueryWrappers.queryWrappers(reportPageDto),
                laboratory,
                SecurityUtils.getUserId().intValue(),
                queryStatus,
                createOrderUser));
        return map;
    }

    @Override
    public int inReport(String url, Integer id) {
        InsReport insReport = new InsReport();
        insReport.setId(id);
        insReport.setUrlS(url);
        // 还原pdf
        String tempUrlPdf = wordToPdfTemp(insReport.getUrlS().replace("/word", wordUrl));
        insReport.setTempUrlPdf("/word/" + tempUrlPdf);
        return insReportMapper.updateById(insReport);
    }

    @Override
    public int upReportUrl(Integer id) {
        InsReport report = insReportMapper.selectById(id);
        // 还原pdf
        String tempUrlPdf = wordToPdfTemp(report.getUrl().replace("/word", wordUrl));

        return insReportMapper.update(null, Wrappers.<InsReport>lambdaUpdate()
                .eq(InsReport::getId, id)
                .set(InsReport::getUrlS, null)
                .set(InsReport::getTempUrlPdf, "/word/" + tempUrlPdf));
    }

    //提交
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int writeReport(Integer id, Integer userId, Integer submitUserId) {
        submitUserId = submitUserId == null ? SecurityUtils.getUserId().intValue() : submitUserId;
        InsReport insReport = insReportMapper.selectById(id);
        insReport.setId(id);
        insReport.setState(1);
        insReport.setWriteUserId(submitUserId);//提交人
        if (userId == null) {
            throw new ErrorException("缺少审核人");
        }
        insReport.setExamineUserId(userId);//审核人
        insReport.setWriteTime(LocalDateTime.now());//提交时间
        //获取提交人的签名地址
        String signatureUrl;
        try {
            signatureUrl = userMapper.selectById(insReport.getWriteUserId()).getSignatureUrl();
        } catch (Exception e) {
            throw new ErrorException("找不到编制人的签名");
        }
        if (ObjectUtils.isEmpty(signatureUrl) || signatureUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        Integer insOrderId = insReportMapper.selectById(id).getInsOrderId();
        InsOrder order = insOrderMapper.selectById(insOrderId);
        boolean isRawMater = order.getTypeSource() != null && order.getTypeSource().equals(1);

        //发送消息
        InformationNotification info = new InformationNotification();
        info.setCreateUser(insProductMapper.selectUserById(userId).get("name"));
        info.setMessageType("3");
        info.setTheme("审核通知");
        info.setContent("您有一条报告编制待审核消息, 编号:" + insReport.getCode());
        info.setSenderId(submitUserId);    //发送人
        info.setConsigneeId(userId);     //收件人
        info.setViewStatus(false);
        info.setJumpPath(MenuJumpPathConstants.REPORT_PREPARATION);
        informationNotificationService.addInformationNotification(info);
        //系统生成报告地址
        String url = insReport.getUrl();
        //手动上传报告地址
        String urlS = insReport.getUrlS();

        // 判断是否是原材料  需要替换****成供应商
        IfsInventoryQuantity one = ifsInventoryQuantityMapper.selectOne(new LambdaQueryWrapper<IfsInventoryQuantity>()
                .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId()));
        if (one != null) {
            if (isRawMater && order.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY)) {
                changeText(new HashMap<String, String>() {{
                    put("**********", one.getSupplierName());
                }}, (StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl));
            }
        }

        wordInsertUrl(new HashMap<String, Object>() {{
            put("writeUrl", Pictures.ofLocal(imgUrl + "/" + signatureUrl).create());
            put("writeDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(null)).create());
            put("insUrl", Pictures.ofLocal(imgUrl + "/" + signatureUrl).create());
        }}, (StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl));

        // 修改临时pdf
        String tempUrlPdf = wordToPdfTemp((StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl));
        insReport.setTempUrlPdf("/word/" + tempUrlPdf);

        insReportMapper.updateById(insReport);

        // 清空审核时间, 审核状态
        insReportMapper.update(null, Wrappers.<InsReport>lambdaUpdate()
                .eq(InsReport::getId, insReport.getId())
                .set(InsReport::getRatifyTime, null)
                .set(InsReport::getIsRatify, null)
                .set(InsReport::getExamineTime, null)
                .set(InsReport::getIsExamine, null));

        return 1;
    }

    //审核
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int examineReport(Integer id, Integer isExamine, String examineTell, Integer userId) {
        InsReport insReport = insReportMapper.selectById(id);
        insReport.setIsExamine(isExamine);
        if (ObjectUtils.isNotEmpty(examineTell)) {
            insReport.setExamineTell(examineTell);
        }

        // 检验人
        String userName = insProductMapper.selectUserById(insReport.getWriteUserId()).get("name");
        String userAccount = insProductMapper.selectUserById(insReport.getWriteUserId()).get("account");

        // 审核人
        Integer checkUserId = SecurityUtils.getUserId().intValue();
        String checkUserName = insProductMapper.selectUserById(checkUserId).get("name");

        insReport.setExamineTime(LocalDateTime.now());//审核时间
        if (isExamine == 0) {
            // 发送企业微信通知(审核退回)
            threadPoolTaskExecutor.execute(() -> {
                // 查询订单
                InsOrder order = insOrderMapper.selectById(insReport.getInsOrderId());
                InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                        .eq(InsSample::getInsOrderId, insReport.getInsOrderId())
                        .last("limit 1"));
                // 查询原材料
                IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());

                String message = "";
                message += "报告编制审核退回通知";
                message += "\n审核人: " + checkUserName;
                message += "\n委托编号: " + order.getEntrustCode();
                message += "\n样品名称: " + insSample.getModel();
                message += "\n规格型号: " + order.getPartDetail();
                if (ifsInventoryQuantity != null) {
                    message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
                }
                message += "\n退回原因: " + examineTell;
                //发送企业微信消息通知  提交复核
                try {
                    WxCpUtils.inform(userAccount, message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            //如果审核不通过
            insReport.setState(0);//提交状态改为待提交
            return insReportMapper.updateById(insReport);
        } else {
            if (userId == null) {
                throw new ErrorException("缺少批准人");
            }
        }
        insReport.setRatifyUserId(userId);//批准人
        //获取审核人的签名地址
        String signatureUrl;
        try {
            signatureUrl = userMapper.selectById(insReport.getExamineUserId()).getSignatureUrl();
        } catch (Exception e) {
            throw new ErrorException("找不到审核人的签名");
        }
        if (StringUtils.isBlank(signatureUrl)) {
            throw new ErrorException("找不到审核人的签名");
        }

        // 批准人
        String sendUserAccount = insProductMapper.selectUserById(userId).get("account");

        //发送消息
        InformationNotification info = new InformationNotification();
        info.setCreateUser(insProductMapper.selectUserById(userId).get("name"));
        info.setMessageType("3");
        info.setTheme("批准通知");
        info.setContent("您有一条报告编制待批准消息, 编号:" + insReport.getCode());
        info.setSenderId(checkUserId);    //发送人
        info.setConsigneeId(userId);     //收件人
        info.setViewStatus(false);
        info.setJumpPath(MenuJumpPathConstants.REPORT_PREPARATION);
        informationNotificationService.addInformationNotification(info);
        //系统生成报告地址
        String url = insReport.getUrl();
        //手动上传报告地址
        String urlS = insReport.getUrlS();
        wordInsertUrl(new HashMap<String, Object>() {{
            put("examineUrl", Pictures.ofLocal(imgUrl + "/" + signatureUrl).create());
            put("examineDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(null)).create());
        }}, (StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl));

        // 修改临时pdf
        String tempUrlPdf = wordToPdfTemp((StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl));
        insReport.setTempUrlPdf("/word/" + tempUrlPdf);

        // 发送企业微信通知(通知批准人审批)
        threadPoolTaskExecutor.execute(() -> {
            // 查询订单
            InsOrder order = insOrderMapper.selectById(insReport.getInsOrderId());
            InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                    .eq(InsSample::getInsOrderId, insReport.getInsOrderId())
                    .last("limit 1"));
            // 查询原材料
            IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());

            String message = "";
            message += "报告编制批准通知";
            message += "\n检验人: " + userName;
            message += "\n复核人: " + checkUserName;
            message += "\n委托编号: " + order.getEntrustCode();
            message += "\n样品名称: " + insSample.getModel();
            message += "\n规格型号: " + order.getPartDetail();
            if (ifsInventoryQuantity != null) {
                message += "\n生产厂家: " + ifsInventoryQuantity.getSupplierName();
                message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
            }
            //发送企业微信消息通知  提交复核
            try {
                WxCpUtils.inform(sendUserAccount, message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        insReportMapper.updateById(insReport);

        // 清空批准备注, 批准时间, 批准状态
        insReportMapper.update(null, Wrappers.<InsReport>lambdaUpdate()
                .eq(InsReport::getId, insReport.getId())
                .set(InsReport::getExamineTell, null)
                .set(InsReport::getRatifyTime, null)
                .set(InsReport::getIsRatify, null));
        return 1;
    }

    //批准
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int ratifyReport(Integer id, Integer isRatify, String ratifyTell) {
        InsReport insReport = insReportMapper.selectById(id);
        insReport.setIsRatify(isRatify);
        if (ObjectUtils.isNotEmpty(ratifyTell)) {
            insReport.setRatifyTell(ratifyTell);
        }
        insReport.setRatifyTime(LocalDateTime.now());//批准时间
        if (isRatify == 0) {

            // 批准人
            Integer ratifyUserId = SecurityUtils.getUserId().intValue();
            String ratifyUserName = insProductMapper.selectUserById(ratifyUserId).get("name");

            // 发送人(审核人)(检验人)
            // 检验人
            String userAccount = insProductMapper.selectUserById(insReport.getWriteUserId()).get("account");
            // 审核人
            String checkUserAccount = insProductMapper.selectUserById(insReport.getExamineUserId()).get("account");


            // 发送企业微信通知(批准退回)
            threadPoolTaskExecutor.execute(() -> {
                // 查询订单
                InsOrder order = insOrderMapper.selectById(insReport.getInsOrderId());
                InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                        .eq(InsSample::getInsOrderId, insReport.getInsOrderId())
                        .last("limit 1"));
                // 查询原材料
                IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());

                String message = "";
                message += "报告编制批准退回通知";
                message += "\n批准人: " + ratifyUserName;
                message += "\n委托编号: " + order.getEntrustCode();
                message += "\n样品名称: " + insSample.getModel();
                message += "\n规格型号: " + order.getPartDetail();
                if (ifsInventoryQuantity != null) {
                    message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
                }
                message += "\n退回原因: " + ratifyTell;
                //发送企业微信消息通知  提交复核
                try {
                    // 审批人
                    WxCpUtils.inform(checkUserAccount, message, null);

                    // 检验人
                    WxCpUtils.inform(userAccount, message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            //如果批准不通过 直接退回到提交人那边去
            insReport.setState(0);
            insReport.setIsExamine(0);
            return insReportMapper.updateById(insReport);
        }
        //获取批准人的签名地址
        String signatureUrl;
        try {
            signatureUrl = userMapper.selectById(insReport.getRatifyUserId()).getSignatureUrl();
        } catch (Exception e) {
            throw new ErrorException("找不到批准人的签名");
        }
        if (StringUtils.isBlank(signatureUrl)) {
            throw new ErrorException("找不到批准人的签名");
        }
        Integer insOrderId = insReportMapper.selectById(id).getInsOrderId();
        InsOrder order = insOrderMapper.selectById(insOrderId);
        boolean isRawMater = order.getTypeSource() != null && order.getTypeSource().equals(1);

        //获取场所的报告专用章
        String sealUrl;
        String laboratory = insOrderMapper.selectById(insReport.getInsOrderId()).getLaboratory();
        try {
            String type = "";
            if (isRawMater && order.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY)) {
                type = "进厂报告";
            } else {
                type = "委托报告";
            }
            sealUrl = insReportMapper.getLaboratoryByName(laboratory, type);
        } catch (Exception e) {
            throw new ErrorException(laboratory + "找不到报告专用章");
        }
        if (StringUtils.isBlank(sealUrl)) {
            throw new ErrorException(laboratory + "找不到报告专用章");
        }
        //系统生成报告地址
        String url = insReport.getUrl();
        //手动上传报告地址
        String urlS = insReport.getUrlS();
        String finalUrl = (StrUtil.isBlank(urlS) ? url : urlS).replace("/word", wordUrl);

        wordInsertUrl(new HashMap<String, Object>() {{
            put("ratifyUrl", Pictures.ofLocal(imgUrl + "/" + signatureUrl).create());
            put("ratifyDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(null)).create());
            put("seal1", Pictures.ofLocal(imgUrl + "/" + sealUrl).create());
            put("seal2", Pictures.ofLocal(imgUrl + "/" + sealUrl).create());
        }}, finalUrl);

        // 修改临时pdf
        insReport.setTempUrlPdf((StrUtil.isBlank(urlS) ? url : urlS).replace(".docx", ".pdf"));

        InsOrder insOrder = new InsOrder();
        insOrder.setId(insOrderId);
        insOrder.setState(4);
        insOrderMapper.updateById(insOrder);

        wordToPdf(finalUrl, sealUrl, isRawMater && order.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY));

        // 判断是否为原材料
        if (isRawMater) {
            // 修改ifs库存状态
            ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getState, 2)
                    .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId()));
        }
        insReport.setRatifyTell("");

        // 发送文件到委托人
        if (StringUtils.isNotBlank(order.getPrepareCode())) {
            threadPoolTaskExecutor.execute(() -> {
                String message = "";
                message += "委托编号: " + order.getEntrustCode();
                message += "委托样品: " + order.getSampleView();
                message += "已检测结束, 请接收";
                try {
                    WxCpUtils.inform(order.getPrepareCode(), message, new File(finalUrl.replace(".docx", ".pdf")));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return insReportMapper.updateById(insReport);
    }

    @Override
    public int wordInsertUrl(Map<String, Object> map, String url) {
        XWPFTemplate template = XWPFTemplate.compile(url).render(map);
        try {
            template.writeAndClose(Files.newOutputStream(Paths.get(url)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }


    //报告批量下载
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String downAll(String ids) {
        List<Long> list = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<InsReport> insReports = insReportMapper.selectBatchIds(list);
        String zipFilePath = null;
        // 临时文件夹路径
        try {
            String tempFolderPath = wordUrl + "/tempFolder";
            File tempFolder = new File(tempFolderPath);
            if (tempFolder.exists()) {
                deleteDirectory(tempFolder); // 删除旧的临时文件夹
            }
            tempFolder.mkdirs(); // 创建新的临时文件夹
            for (InsReport insReport : insReports) {
                File sourceFile = new File((ObjectUtils.isNotEmpty(insReport.getUrlS()) ? insReport.getUrlS() : insReport.getUrl()).replace("/word", wordUrl));
                File destinationFile = new File(tempFolder, sourceFile.getName());
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            // 压缩临时文件夹
            zipFilePath = wordUrl + "/zip/output.zip";
            // 判断zip路径是否存在
            File zipDir = new File(wordUrl + "/zip");
            if(!zipDir.isDirectory()){
                zipDir.mkdirs();
            }
            zipDirectory(tempFolderPath, zipFilePath);

            // 清理临时文件夹
            deleteDirectory(tempFolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/word/zip/output.zip";
    }

    //批量上传
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upAll(MultipartFile file) throws IOException {
        File tempFile = null;
        File unzipDir = null;
        try {
            tempFile = File.createTempFile(wordUrl, ".zip");
            file.transferTo(tempFile);

            unzipDir = new File("uploaded_files");
            if (!unzipDir.exists()) {
                unzipDir.mkdir();
            }
            unzip(tempFile, unzipDir);
            // 处理解压后的文件
            File[] files = unzipDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    // 根据文件名查询id
                    String name = f.getName();
                    InsReport insReport = insReportMapper.selectOne(Wrappers.<InsReport>lambdaQuery().like(InsReport::getCode, f.getName().replace(".docx", "").replace("JCZX", "JCZX/")));
                    if (ObjectUtils.isEmpty(insReport)) {
                        throw new ErrorException("没有找到 " + f.getName() + " 这个文件对应的报告数据");
                    }
                    String urlString;
                    String pathName;
                    try {
                        String path = wordUrl;
                        File realpath = new File(path);
                        if (!realpath.exists()) {
                            realpath.mkdirs();
                        }
                        pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + f.getName();
                        urlString = realpath + "/" + pathName;
                        // 复制文件到指定路径
                        Files.copy(f.toPath(), new File(urlString).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        inReport("/word/" + pathName, insReport.getId());
                    } catch (IOException e) {
                        throw new ErrorException("文件上传失败");
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorException("文件处理失败");
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            // 递归删除解压目录及其中的文件
            if (unzipDir.exists()) {
                deleteDirectory(unzipDir); // 删除旧的临时文件夹
            }
        }
        return 0;
    }


    //解压文件夹
    private void unzip(File zipFile, File destDir) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File file = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    try (InputStream in = zip.getInputStream(entry);
                         OutputStream out = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }

    // 压缩文件夹
    public static void zipDirectory(String sourceDirPath, String zipFilePath) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path sourceDir = Paths.get(sourceDirPath);
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    // 删除文件夹及其内容
    public static void deleteDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        Files.delete(directory.toPath());
    }

    @Override
    public void wordToPdf(String path, String sealUrl, boolean isRawMater) {
        try {
            wordToPdf(path, path.replace(".docx", ".pdf"), sealUrl, isRawMater);
        } catch (Exception e) {
            throw new ErrorException("转换失败");
        }
    }


    public String wordToPdf(String wordPath, String pdfPath, String sealUrl, boolean isRawMater) {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
            InputStream is = new ClassPathResource("/lib/license.xml").getInputStream();
            License license = new License();
            license.setLicense(is);
            if (!license.getIsLicensed()) {
                log.info("License验证不通过...");
                return null;
            }
            //生成一个空的PDF文件
            File file;
            //判断是否是进厂报告
            if (!isRawMater) {
                file = new File(pdfPath.replace(".pdf", "-1.pdf"));
            } else {
                file = new File(pdfPath.replace(".pdf", ".pdf"));
            }
            os = new FileOutputStream(file);
            //要转换的word文件
            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);
            doc.save(os, SaveFormat.PDF);

            //添加骑缝章
            if (!isRawMater) {
                stamperCheckMarkPDF(pdfPath.replace(".pdf", "-1.pdf"), pdfPath, imgUrl + "/" + sealUrl);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 切割图片
     *
     * @param Path 图片路径
     * @param n    切割份数
     */
    public static com.itextpdf.text.Image[] slicingImages(String Path, int n) throws IOException, BadElementException {
        com.itextpdf.text.Image[] nImage = new com.itextpdf.text.Image[n];
        BufferedImage img = ImageIO.read(new File(Path));

        int h = img.getHeight();
        int w = img.getWidth();

        int sw = w / n;
        for (int i = 0; i < n; i++) {
            BufferedImage subImg;
            if (i == n - 1) {//最后剩余部分
                subImg = img.getSubimage(i * sw, 0, w - i * sw, h);
            } else {//前n-1块均匀切
                subImg = img.getSubimage(i * sw, 0, sw, h);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(subImg, Path.substring(Path.lastIndexOf('.') + 1), out);
            nImage[i] = com.itextpdf.text.Image.getInstance(out.toByteArray());

        }
        return nImage;
    }

    /**
     * 盖骑缝章
     *
     * @param infilePath  原PDF路径
     * @param outFilePath 输出PDF路径
     */
    public static void stamperCheckMarkPDF(String infilePath, String outFilePath, String picPath) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(infilePath);//选择需要印章的pdf
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outFilePath));//加完印章后的pdf


        com.itextpdf.text.Rectangle pageSize = reader.getPageSize(1);//获得第一页
        float height = pageSize.getHeight();
        float width = pageSize.getWidth();

        int nums = reader.getNumberOfPages();
        com.itextpdf.text.Image[] nImage = slicingImages(picPath, nums);//生成骑缝章切割图片

        for (int n = 1; n <= nums; n++) {
            PdfContentByte over = stamp.getOverContent(n);//设置在第几页打印印章
            com.itextpdf.text.Image img = nImage[n - 1];//选择图片
            float newHeight = 100f;
            float newWidth = img.getWidth() / (img.getHeight() / 100);
            img.scaleAbsolute(newWidth, newHeight);//控制图片大小
            img.setAbsolutePosition(width - newWidth, height / 2 - newHeight / 2);//控制图片位置
            over.addImage(img);
        }
        stamp.close();
    }

    @Transactional(rollbackFor = Exception.class)
    public void isRawMaterial(InsOrder insOrder) {
        IfsInventoryQuantity one = ifsInventoryQuantityMapper.selectOne(new LambdaQueryWrapper<IfsInventoryQuantity>()
                .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId()));
        if (Objects.isNull(one)) {
            throw new ErrorException("找不到原材料信息");
        }
        // 判断是否有不合格信息
        Long count = insUnqualifiedHandlerMapper.selectCount(Wrappers.<InsUnqualifiedHandler>lambdaQuery()
                .eq(InsUnqualifiedHandler::getInventoryQuantityId, one.getId()));
        String toLocation = null;

        // 判断是否有不合格
        Long unqualifiedCount = getUnqualifiedCount(insOrder);

        if (count.equals(0L) && unqualifiedCount.equals(0L) && one.getIsFinish().equals(0) && one.getIsSource().equals(1)) {
            // 原材料移库
            toLocation = this.moveRawMaterial(one);
        }

        // 判断结束状态修改合格状态
        int inspectStatus = (count == 0 && unqualifiedCount == 0) ? 1 : 2;
        insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getId, insOrder.getId())
                .set(InsOrder::getInsResult, inspectStatus));

        if (one.getIsFinish().equals(0)) {
            ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                    .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId())
                    .set(IfsInventoryQuantity::getInspectStatus, inspectStatus));

            // 修改ifs库存状态
            if (StringUtils.isBlank(toLocation)) {
                ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                        .set(IfsInventoryQuantity::getIsFinish, 1)
                        .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId()));
            } else {
                ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                        .set(IfsInventoryQuantity::getIsFinish, 1)
                        .set(IfsInventoryQuantity::getToLocation, toLocation)
                        .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId()));
            }

            threadPoolTaskExecutor.execute(() -> {
                // 企业微信通知
                String message = "";
                message += "检测结果提交通知";
                message += "\n批次号: " + one.getUpdateBatchNo();
                message += "\n零件号: " + one.getPartNo();
                message += "\n零件描述: " + one.getPartDesc();
                message += "\n供应商名称: " + one.getSupplierName();
                message += "\n抵达数量: " + one.getQtyArrived().stripTrailingZeros().toPlainString() + one.getBuyUnitMeas();
                // 发送企业inspectStatus信通知
                if (inspectStatus == 1) {
                    message += "\n检测结果: 合格";
                } else {
                    message += "\n检测结果: 不合格";
                }
                WxCpUtils.informWebHook(wechatProperty.getExaminingUrl(), message);
            });

        }


    }

    /**
     * 判断是否有不合格
     * @param insOrder
     * @return
     */
    @Override
    public Long getUnqualifiedCount(InsOrder insOrder) {
        Long unqualifiedCount = 0L;
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                .eq(InsSample::getInsOrderId, insOrder.getId()));
        if (CollectionUtils.isNotEmpty(insSamples)) {
            unqualifiedCount = insProductMapper.selectCount(Wrappers.<InsProduct>lambdaQuery()
                    .in(InsProduct::getInsSampleId, insSamples.stream().map(InsSample::getId).collect(Collectors.toList()))
                    .eq(InsProduct::getInsResult, 0));

            // 判断如果有不合格的检验项, 判断有没有检验项复测, 复核合格也算合格通过
            if (!unqualifiedCount.equals(0L)) {
                List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                        .in(InsProduct::getInsSampleId, insSamples.stream().map(InsSample::getId).collect(Collectors.toList()))
                        .eq(InsProduct::getInsResult, 0));

                boolean flag = true;
                for (InsProduct insProduct : insProducts) {
                    Long unqualifiedProductCount = insUnqualifiedRetestProductMapper.selectCount(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                            .eq(InsUnqualifiedRetestProduct::getInsProductId, insProduct.getId())
                            .ne(InsUnqualifiedRetestProduct::getInsResult, 0));
                    if (unqualifiedProductCount != 2) {
                        flag = false;
                    }
                }
                if (flag) {
                    unqualifiedCount = 0L;
                }
            }
        }
        return unqualifiedCount;
    }

    /**
     * ifs移库操作
     * @param one
     * @return
     */
    @Override
    public String moveRawMaterial(IfsInventoryQuantity one) {
        String toLocation;
        // 登记采购检验结果STD
        if (one.getIsRegister().equals(0)) {
            Map<String, Object> resultMap = new HashMap<>();
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("ORDER_NO", one.getOrderNo()); // 采购订单号
            map.put("LINE_NO", one.getLineNo()); // 行号
            map.put("RELEASE_NO", one.getReleaseNo()); // 下达号
            map.put("RECEIPT_NO", one.getReceiptNo()); // 接收号
            map.put("PURCH_QTY", one.getQtyToInspect()); // 要检验的采购数量
            resultList.add(map);
            resultMap.put("RECORD_ID", UUID.randomUUID().toString());
            resultMap.put("SYSCODE", "LIMS");
            resultMap.put("SYSMODEL", "登记采购检验结果");
            resultMap.put("BATCH_INFO", resultList);
            Result result = ifsApiUtils.getProcurementResults(JSONUtil.toJsonStr(resultMap));
            if (result.getCode() != 200) {
                throw new ErrorException("IFS登记采购检验结果失败: " + result.getMessage());
            }
        }
        insOrderService.updateIfsInventoryQuantity(one.getId());
        /**
         * TODO 后续需要调用IFS的接口 移入的库位号 toLocation
         */
        // 检验后移库
        toLocation = "1301";
        Map<String, Object> moveResultMap = new HashMap<>();
        List<Map<String, Object>> moveResultList = new ArrayList<>();
        Map<String, Object> moveMap = new HashMap<>();
        moveMap.put("ORDER_NO", one.getOrderNo()); // 采购订单号
        moveMap.put("LINE_NO", one.getLineNo());
        moveMap.put("RELEASE_NO", one.getReleaseNo());
        moveMap.put("RECEIPT_NO", one.getReceiptNo());
        moveMap.put("PART_NO", one.getPartNo());
        moveMap.put("QTY", one.getQtyArrived());
        moveMap.put("LOCATION_NO", one.getLocationNo());
        moveMap.put("TO_LOCATION_NO", toLocation);
        moveMap.put("LOT_BATCH_NO", one.getLotBatchNo());
        moveMap.put("SERIAL_NO", one.getSerialNo());
        moveMap.put("WAIV_DEV_REJ_NO", one.getWaivDevRejNo());
        moveMap.put("ENG_CHG_LEVEL", one.getEngChgLevel());
        moveMap.put("ACTIVITY_SEQ", one.getActivitySeq());
        moveResultList.add(moveMap);
        moveResultMap.put("RECORD_ID", UUID.randomUUID().toString());
        moveResultMap.put("SYSCODE", "LIMS");
        moveResultMap.put("SYSMODEL", "检验后移库");
        moveResultMap.put("BATCH_INFO", moveResultList);

        Result result1 = ifsApiUtils.moveReceipt(JSONUtil.toJsonStr(moveResultMap));
        // 如果有必须为零件指定批号报错需要重新提交移库信息去指定批号
        if (result1.getCode() != 200) {
            String message = result1.getMessage();
            if (message.contains("必须为零件") && message.contains("指定批号")) {
                updaeBatch(one, toLocation);
            } else {
                throw new ErrorException("IFS检验后移库失败: " + result1.getMessage());
            }
        }
        return toLocation;
    }

    /**
     * 退回到检验任务
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendBackTask(Integer id) {
        InsReport report = insReportMapper.selectById(id);
        // 根据订单查询试验室
        String laboratory = insOrderMapper.selectLaboratoryByOrderId(report.getInsOrderId());

        // 修改订单状态
        insOrderStateMapper.update(null, Wrappers.<InsOrderState>lambdaUpdate()
                .eq(InsOrderState::getInsOrderId, report.getInsOrderId())
                .eq(InsOrderState::getLaboratory, laboratory)
                .set(InsOrderState::getInsState, 4));

        Integer insSampleId = insSampleUserMapper.selectOne(Wrappers.<InsSampleUser>lambdaQuery()
                .eq(InsSampleUser::getInsSampleId, report.getInsOrderId())
                .orderByDesc(InsSampleUser::getId)
                .last("limit 1")).getId();
        insSampleUserMapper.deleteById(insSampleId);

        // 修改报告行为未显示
        insReportMapper.update(null, Wrappers.<InsReport>lambdaUpdate()
                .eq(InsReport::getId, id)
                .set(InsReport::getIsPass, 0));

        return false;
    }

    /**
     * 报告报表导出
     * @param dto
     * @param response
     */
    @Override
    public void reportAllExport(ReportPageDto dto, HttpServletResponse response) throws UnsupportedEncodingException {

        Integer createOrderUser = dto.getCreateOrderUser();
        String queryStatus = dto.getQueryStatus();
        dto.setQueryStatus(null);
        dto.setCreateOrderUser(null);

        List<InsReportExport> insReportExports = insReportMapper.reportAllExport(QueryWrappers.queryWrappers(dto),
                SecurityUtils.getUserId().intValue(),
                queryStatus,
                createOrderUser);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("报告报表导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "报告报表导出").head(InsReportExport.class).build();

            //向sheet0写入数据 传入空list这样只导出表头
            excelWriter.write(insReportExports, mainSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }


    }


    /**
     * 先修改采购订单批次号, 后进行移库操作
     * @param one
     * @param toLocation
     */
    private void updaeBatch(IfsInventoryQuantity one, String toLocation) {
        if (one.getIsUpdateBatch().equals(0)) {
            // 先修改批次号后进行移库
            Map<String, Object> resultMap = new HashMap<>();
            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("ORDER_NO", one.getOrderNo()); // 采购订单号
            map.put("LINE_NO", one.getLineNo()); // 行号
            map.put("RELEASE_NO", one.getReleaseNo()); // 下达号
            map.put("RECEIPT_NO", one.getReceiptNo()); // 接收号
            map.put("PART_NO", one.getPartNo()); //零件号
            map.put("CONFIGURATION_ID", one.getConfigurationId()); // 配置标识
            map.put("LOCATION_NO", one.getLocationNo()); // 库位号
            map.put("LOT_BATCH_NO", one.getLotBatchNo());// 批次号
            map.put("NEW_LOT_BATCH_NO", one.getUpdateBatchNo()); // 目标批次号
            map.put("SERIAL_NO", one.getSerialNo()); // 序列号
            map.put("ENG_CHG_LEVEL", one.getEngChgLevel()); // 版本号
            map.put("WAIV_DEV_REJ_NO", one.getWaivDevRejNo()); // wdr号
            map.put("ACTIVITY_SEQ", one.getActivitySeq()); // 活动序号
            map.put("QTY_TO_CHANGE", one.getQtyArrived()); // 变更数量
            resultList.add(map);
            resultMap.put("RECORD_ID", UUID.randomUUID().toString());
            resultMap.put("SYSCODE", "LIMS");
            resultMap.put("SYSMODEL", "修改采购订单批次号");
            resultMap.put("BATCH_INFO", resultList);

            Result result = ifsApiUtils.updateMoveReceiptLot(JSONUtil.toJsonStr(resultMap));

            if (result.getCode() != 200) {
                throw new ErrorException("IFS修改批次号失败: " + result.getMessage());
            }
            ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                    .set(IfsInventoryQuantity::getIsUpdateBatch, 1)
                    .eq(IfsInventoryQuantity::getId, one.getId()));
        }

        Map<String, Object> moveResultMap = new HashMap<>();
        List<Map<String, Object>> moveResultList = new ArrayList<>();
        Map<String, Object> moveMap = new HashMap<>();
        moveMap.put("ORDER_NO", one.getOrderNo()); // 采购订单号
        moveMap.put("LINE_NO", one.getLineNo());
        moveMap.put("RELEASE_NO", one.getReleaseNo());
        moveMap.put("RECEIPT_NO", one.getReceiptNo());
        moveMap.put("PART_NO", one.getPartNo());
        moveMap.put("QTY", one.getQtyArrived());
        moveMap.put("LOCATION_NO", one.getLocationNo());
        moveMap.put("TO_LOCATION_NO", toLocation);
        moveMap.put("LOT_BATCH_NO", one.getUpdateBatchNo());
        moveMap.put("SERIAL_NO", one.getSerialNo());
        moveMap.put("WAIV_DEV_REJ_NO", one.getWaivDevRejNo());
        moveMap.put("ENG_CHG_LEVEL", one.getEngChgLevel());
        moveMap.put("ACTIVITY_SEQ", one.getActivitySeq());
        moveResultList.add(moveMap);
        moveResultMap.put("RECORD_ID", UUID.randomUUID().toString());
        moveResultMap.put("SYSCODE", "LIMS");
        moveResultMap.put("SYSMODEL", "检验后移库");
        moveResultMap.put("BATCH_INFO", moveResultList);

        Result result1 = ifsApiUtils.moveReceipt(JSONUtil.toJsonStr(moveResultMap));
        if (result1.getCode() != 200) {
            throw new ErrorException("IFS检验后移库失败: " + result1.getMessage());
        }

    }

    /**
     * 替换段落文本
     * @param filePath docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changeText(Map<String, String> textMap, String filePath) {
        try {
            FileInputStream stream = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(stream);
            List<XWPFTable> tables = document.getTables();
            for (XWPFTable table : tables) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {

                        textMap.forEach((s, s2) -> {
                            if (cell.getText().equals(s)) {
                                XWPFParagraph paragraph = cell.getParagraphs().get(0);
                                XWPFRun oldRun = paragraph.getRuns().get(0);
                                // 保存原样式
                                String fontFamily = oldRun.getFontFamily();
                                int fontSize = oldRun.getFontSize();
                                String color = oldRun.getColor();
                                ParagraphAlignment alignment = paragraph.getAlignment();
                                // 替换内容
                                paragraph.removeRun(0); // 移除原有的 run
                                XWPFRun newRun = paragraph.createRun();
                                newRun.setText(s2);
                                // 应用原样式
                                newRun.setFontFamily(fontFamily);
                                newRun.setFontSize(fontSize);
                                newRun.setColor(color);
                                paragraph.setAlignment(alignment);
                            }
                        });
                    }
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            document.write(fileOutputStream);
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e.getMessage());
        }
    }

    /**
     * word转换pdf
     * @param path
     * @return
     */
    private String wordToPdfTemp(String path) {
        try {
            return wordToPdf(path, path.replace(".docx", "-临时.pdf"));
        } catch (Exception e) {
            throw new ErrorException("转换失败");
        }
    }

    private String wordToPdf(String wordPath, String pdfPath) {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
            InputStream is = new ClassPathResource("/lib/license.xml").getInputStream();
            License license = new License();
            license.setLicense(is);
            if (!license.getIsLicensed()) {
                log.info("License验证不通过...");
                return null;
            }
            //生成一个空的PDF文件
            File file;
            //判断是否是进厂报告
            file = new File(pdfPath);
            os = new FileOutputStream(file);
            //要转换的word文件
            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);
            doc.save(os, SaveFormat.PDF);
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}






