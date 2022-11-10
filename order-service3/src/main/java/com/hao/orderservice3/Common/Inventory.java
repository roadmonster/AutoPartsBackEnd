package com.hao.orderservice3.Common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    int id;
    String productName;
    int unitsAvailable;
}
