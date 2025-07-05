package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.dto.InternalWastesDto;
import com.ruoyi.require.mapper.InternalWastesMapper;
import com.ruoyi.require.pojo.InternalWastes;
import com.ruoyi.require.pojo.InternalWastesDetail;
import com.ruoyi.require.service.InternalWastesDetailService;
import com.ruoyi.require.service.InternalWastesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 安全内务三废登记 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:27
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InternalWastesServiceImpl extends ServiceImpl<InternalWastesMapper, InternalWastes> implements InternalWastesService {

    private InternalWastesDetailService internalWastesDetailService;

    /**
     * 安全内务三废登记分页查询
     * @param page
     * @param internalWastes
     * @return
     */
    @Override
    public IPage<InternalWastesDto> pageInternalWastes(Page page, InternalWastes internalWastes) {
        return baseMapper.pageInternalWastes(page, QueryWrappers.queryWrappers(internalWastes));
    }

    /**
     * 安全内务三废登记新增
     * @param internalWastes
     * @return
     */
    @Override
    public boolean addInternalWastes(InternalWastesDto internalWastes) {

        baseMapper.insert(internalWastes);
        // 新增详情
        for (InternalWastesDetail internalWastesDetail : internalWastes.getWastesDetailList()) {
            internalWastesDetail.setWastesId(internalWastes.getWastesId());
        }
        internalWastesDetailService.saveBatch(internalWastes.getWastesDetailList());
        return true;
    }

    /**
     * 安全内务三废登记修改
     * @param internalWastes
     * @return
     */
    @Override
    public boolean updateInternalWastes(InternalWastesDto internalWastes) {
        baseMapper.updateById(internalWastes);

        // 删除之前的详情
        internalWastesDetailService.remove(Wrappers.<InternalWastesDetail>lambdaQuery()
                .eq(InternalWastesDetail::getWastesId, internalWastes.getWastesId()));

        // 新增详情
        for (InternalWastesDetail internalWastesDetail : internalWastes.getWastesDetailList()) {
            internalWastesDetail.setWastesId(internalWastes.getWastesId());
        }
        internalWastesDetailService.saveBatch(internalWastes.getWastesDetailList());

        return true;
    }

    /**
     * 安全内务三废登记删除
     * @param WastesId
     * @return
     */
    @Override
    public boolean delInternalWastes(Integer WastesId) {
        internalWastesDetailService.remove(Wrappers.<InternalWastesDetail>lambdaQuery()
                .eq(InternalWastesDetail::getWastesId, WastesId));
        baseMapper.deleteById(WastesId);
        return true;
    }

    /**
     * 安全内务三废登记查看详情
     * @param WastesId
     * @return
     */
    @Override
    public InternalWastesDto getInternalWastesOne(Integer WastesId) {
        InternalWastes internalWastes = baseMapper.selectById(WastesId);
        InternalWastesDto internalWastesDto = new InternalWastesDto();
        BeanUtils.copyProperties(internalWastes, internalWastesDto);

        // 查询详细信息
        internalWastesDto.setWastesDetailList(internalWastesDetailService.list(Wrappers.<InternalWastesDetail>lambdaQuery()
                .eq(InternalWastesDetail::getWastesId, WastesId)));
        return internalWastesDto;
    }

    /**
     * 导出三废处理
     * @param wastesId
     * @param response
     */
    @Override
    public void exportInternalWastes(Integer wastesId, HttpServletResponse response) {
        InternalWastes internalWastes = baseMapper.selectById(wastesId);

        List<InternalWastesDetail> wastesDetailList = internalWastesDetailService.list(Wrappers.<InternalWastesDetail>lambdaQuery()
                .eq(InternalWastesDetail::getWastesId, wastesId));

        int index = 1;
        for (InternalWastesDetail detail : wastesDetailList) {
            detail.setIndex(index);
            index++;
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-wastes.docx");
        Configure configure = Configure.builder()
                .bind("wastesDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("remark", internalWastes.getRemark());
                    put("wastesDetailList", wastesDetailList);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "安全内务三废处理", "UTF-8");
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
