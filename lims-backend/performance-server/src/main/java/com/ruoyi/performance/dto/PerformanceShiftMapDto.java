package com.ruoyi.performance.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PerformanceShiftMapDto {

    private String name;

    private String shiftTime;

    private String userId;

    private String department;

    private Map<String, Object> monthlyAttendance = new HashMap<>();

    private Map<String, Object> sidebarAnnualAttendance = new HashMap<>();;
    private List<Map<String, Object>> list = new ArrayList<>();

    private List<Map<Object, Object>> headerList = new ArrayList<>();
}
