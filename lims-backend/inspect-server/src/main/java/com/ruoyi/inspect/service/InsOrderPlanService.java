package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.InsOrderPlanDTO;
import com.ruoyi.inspect.dto.InsOrderPlanProductDto;
import com.ruoyi.inspect.dto.SaveInsContextDto;
import com.ruoyi.inspect.pojo.InsOrderFactoryVerify;
import com.ruoyi.inspect.pojo.InsOrderFile;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.vo.InsOrderPlanTaskSwitchVo;
import com.ruoyi.inspect.vo.InsOrderPlanVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 检验任务-业务层
 */
public interface InsOrderPlanService {
    IPage<InsOrderPlanVO> selectInsOrderPlanList(Page page, InsOrderPlanDTO insOrderPlanDTO);

    IPage<InsOrderPlanTaskSwitchVo> inspectionOrderDetailsTaskSwitching(Page page, InsOrderPlanDTO insOrderPlanDTO);

    boolean claimInsOrderPlan(InsOrderPlanDTO entity);

    void saveInsContext(SaveInsContextDto saveInsContextDto);

    Map<String, Object> doInsOrder(Integer id, String laboratory);

    int upPlanUser(Integer userId, Integer orderId,String sonLaboratory);

    int submitPlan(Integer orderId, String laboratory, Integer verifyUser, String entrustCode);

    List<InsProduct> getInsProduct(InsOrderPlanProductDto insOrderPlanProductDto);

    List<String> checkSubmitPlan(Integer orderId, String laboratory);

    IPage<InsOrderFile> getFileList(Page page, InsOrderFile insOrderFile);

    int uploadFile(Integer orderId, MultipartFile file);


    List<String> upPlanUser2(Integer orderId);

    int rawMaterialVerifyPlan(Integer orderId, String laboratory, Integer type, String tell, Integer userId);

    /**
     * 电缆配置. 查看标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getCableTag(Integer id, String laboratory);

    /**
     * 原材料查看标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getRawMaterialTag(Integer id, String laboratory);

    /**
     * 查看重复标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getRepetitionTag(Integer id, String laboratory, String cableTag);


    /**
     * 新增不合格复测内容给
     * @param ids
     * @return
     */
    boolean addDisqualificationRetest(List<InsProduct> insProductsList);

    /**
     * 获取不合格复测数据
     * @return
     */
    List<InsProduct> getInsProductUnqualifiedRetest(InsOrderPlanProductDto insOrderPlanProductDto);

    /**
     * 保存不合格复测检验内容
     */
    void saveUnqualifiedContext(Map<String, Object> insContext,Integer currentTable,Integer currentSampleId,Integer orderId,String sonLaboratory);

    /**
     * 查询原材料进货验证
     * @param insOrderId
     * @return
     */
    InsOrderFactoryVerify getFactoryVerify(Integer insOrderId);

    /**
     * 保存原材料进货验证
     * @param factoryVerify
     * @return
     */
    boolean addFactoryVerify(InsOrderFactoryVerify factoryVerify);


    /**
     * 生成pd临时
     * @param path
     * @return
     */
    String wordToPdfTemp(String path);
}
