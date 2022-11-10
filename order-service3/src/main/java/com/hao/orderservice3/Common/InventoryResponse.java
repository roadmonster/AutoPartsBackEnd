package com.hao.orderservice3.Common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private int stockId;
    private int availableUnits;
    private String productName;
    private boolean orderFulfilled;
}
