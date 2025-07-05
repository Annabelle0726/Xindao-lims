package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.ProcurementSuppliesStore;
import lombok.Data;

@Data
public class StoreDto extends ProcurementSuppliesStore {


    private String consumablesName;

    private Integer storeNumber;

    private Double totalPrice;

    private String registrantName;

    private String storageUserName;

    private Integer consumablesId;

}
