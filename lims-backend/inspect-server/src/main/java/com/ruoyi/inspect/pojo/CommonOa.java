package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * oa审核流程信息记录表
 * @TableName common_oa
 */
@TableName(value ="common_oa")
@Data
public class CommonOa implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 操作
     */
    private String operation;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 审批日期
     */
    private String approvalDate;

    /**
     * 审批时间
     */
    private String approvalTime;

    /**
     * 审批意见
     */
    private String approvalOpinion;

    /**
     * 流程id
     */
    private Long workflowId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
