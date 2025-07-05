package com.ruoyi.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.FilePictureRenderData;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.process.mapper.ProcessSampleMapper;
import com.ruoyi.process.pojo.ProcessSample;
import com.ruoyi.process.service.ProcessSampleService;
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
 * 样品接收 服务实现类
 * </p>
 *
 * @author
 * @since 2024-12-12 05:02:49
 */
@Service
public class ProcessSampleServiceImpl extends ServiceImpl<ProcessSampleMapper, ProcessSample> implements ProcessSampleService {

    @Resource
    private ProcessSampleMapper processSampleMapper;

    @Override
    public IPage<ProcessSample> pageProcessSample(Page page, ProcessSample processSample) {
        return processSampleMapper.pageProcessSample(page, QueryWrappers.queryWrappers(processSample));
    }

    @Override
    public void exportProcessSample(ProcessSample processSample, HttpServletResponse response) {
        List<ProcessSample> processSampleList = pageProcessSample(new Page(-1, -1), processSample).getRecords();
        InputStream inputStream = this.getClass().getResourceAsStream("/static/sample-receive.docx");
        Configure configure = Configure.builder()
                .bind("sampleList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("sampleList", processSampleList);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "检验样品登记表", "UTF-8");
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
