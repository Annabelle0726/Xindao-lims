package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeStandardSubstanceAcceptance;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptanceInspection;
import lombok.Data;

import java.util.List;

@Data
public class AcceptanceDto {

    private FeStandardSubstanceAcceptance acceptance;

    private List<FeStandardSubstanceAcceptanceInspection> list;
}
