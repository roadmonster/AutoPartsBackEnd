package com.hao.orderservice3.Common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private int paymentId;
    private String orderStatus;
    private String transactionId;
    private String orderUniqueId;
    private double amount;
}
