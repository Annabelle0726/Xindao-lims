package com.ruoyi.inspect.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleDefectsChildrenVo {
    private String entrust_code;//委托编码
    private String inspection_item;//检验项
    private String name;//检验人
    private LocalDateTime create_time;//检验日期
}
