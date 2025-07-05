package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.personnel.mapper.AnnexMapper;
import com.ruoyi.personnel.pojo.Annex;
import com.ruoyi.personnel.service.AnnexService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex> implements AnnexService {
}
