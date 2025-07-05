package com.ruoyi.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.ProductPartMapper;
import com.ruoyi.basic.mapper.StructureTestObjectPartMapper;
import com.ruoyi.basic.pojo.ProductPart;
import com.ruoyi.basic.pojo.StructureTestObjectPart;
import com.ruoyi.basic.service.StructureTestObjectPartService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检验对象零件表
 *
 * @author zhuo
 * @since 2024-08-07
 */
@Transactional
@Service
@AllArgsConstructor
public class StructureTestObjectPartServiceImpl extends ServiceImpl<StructureTestObjectPartMapper, StructureTestObjectPart> implements StructureTestObjectPartService {

    private ProductPartMapper productPartMapper;

    @Override
    public IPage<StructureTestObjectPart> selectByTestObjectId(Page page, StructureTestObjectPart structureTestObjectPart) {
        return baseMapper.selectListByTestObjectId(page, QueryWrappers.queryWrappers(structureTestObjectPart),structureTestObjectPart.getTestObjectId());
    }

    @Override
    public void addTestObjectPart(StructureTestObjectPart structureTestObjectPart) {
        this.isPartNoExist(structureTestObjectPart.getPartNo(), null);
        if (structureTestObjectPart.getTestObjectId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        baseMapper.insert(structureTestObjectPart);
    }

    @Override
    public void updateTestObjectPart(StructureTestObjectPart structureTestObjectPart) {
        this.isPartNoExist(structureTestObjectPart.getPartNo(),  structureTestObjectPart.getId());
        if (structureTestObjectPart.getTestObjectId() == null) {
            throw new BaseException("缺少产品对象id");
        }
        baseMapper.updateById(structureTestObjectPart);
    }

    // 判断零件号是否存在
    public void isPartNoExist(String partNo, Integer id) {
        // 零件号唯一 但不必填
        if (StringUtils.isNotBlank(partNo)) {
            Long count = productPartMapper.selectCount(new LambdaQueryWrapper<ProductPart>()
                    .eq(ProductPart::getPartNo, partNo));
            Long selectCount = baseMapper.selectCount(Wrappers.<StructureTestObjectPart>lambdaQuery()
                    .eq(StructureTestObjectPart::getPartNo, partNo)
                    .ne(id != null, StructureTestObjectPart::getId, id));
            if (count > 0 || selectCount > 0) {
                throw new BaseException("该零件号已绑定过检验对象");
            }
        } else {
            throw new BaseException("请输入零件号");
        }
    }
}

