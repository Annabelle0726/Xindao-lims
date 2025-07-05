package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.dto.ForeignRegisterDto;
import com.ruoyi.require.mapper.ForeignRegisterMapper;
import com.ruoyi.require.pojo.ForeignRegister;
import com.ruoyi.require.service.ForeignRegisterService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 外来人员登记 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-19 07:17:35
 */
@Service
public class ForeignRegisterServiceImpl extends ServiceImpl<ForeignRegisterMapper, ForeignRegister> implements ForeignRegisterService {
    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    /**
     * 外来人员登记分页查询
     * @param page
     * @param foreignRegister
     * @return
     */
    @Override
    public IPage<ForeignRegisterDto> pageForeignRegister(Page page, ForeignRegisterDto foreignRegister) {
        String beginDate = foreignRegister.getBeginDate();
        String endDate = foreignRegister.getEndDate();
        foreignRegister.setBeginDate(null);
        foreignRegister.setEndDate(null);
        return baseMapper.pageForeignRegister(page, QueryWrappers.queryWrappers(foreignRegister), beginDate, endDate);
    }

    /**
     * 导出外来人员登记
     * @param foreignRegister
     */
    @Override
    public void exportForeignRegister(ForeignRegisterDto foreignRegister, HttpServletResponse response) {
        String beginDate = foreignRegister.getBeginDate();
        String endDate = foreignRegister.getEndDate();
        foreignRegister.setBeginDate(null);
        foreignRegister.setEndDate(null);
        List<ForeignRegisterDto> register = baseMapper.getForeignRegisterList(QueryWrappers.queryWrappers(foreignRegister), beginDate, endDate);

        for (ForeignRegisterDto foreignRegisterDto : register) {
            // 添加协同人和批准人签名
            foreignRegisterDto.setAccompanyingRender(StringUtils.isNotBlank(foreignRegisterDto.getAccompanyingUrl())
                    ? Pictures.ofLocal(imgUrl + "/" + foreignRegisterDto.getAccompanyingUrl()).create() : null);
            foreignRegisterDto.setApproveRender(StringUtils.isNotBlank(foreignRegisterDto.getApproveUrl())
                    ? Pictures.ofLocal(imgUrl + "/" + foreignRegisterDto.getApproveUrl()).create() : null);
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/foreign-register.docx");
        Configure configure = Configure.builder()
                .bind("register", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("register", register);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "外来人员登记", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }
}
