package org.hust.cse.supplychain.basic.modules.Ledger.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesLedgerVO {
    private Long id; // 主键ID
    
    private String sellerName;        // 销售方名称
    private String buyerName;         // 购买方名称
    private String buyerTaxType;      // 购买方纳税人类型
    private String saleTime;          // 销售时间
    private String saleLocation;      // 销售地点
    private String productName;       // 产品名称
    private BigDecimal actualQuantity; // 实际销售数量
    private BigDecimal priceWithoutTax; // 销售价格（不含税）
    private BigDecimal taxRate;       // 税率
    private BigDecimal taxAmount;     // 税额
    private BigDecimal receivableAmount; // 应收金额
    private BigDecimal receivedAmount; // 实收金额
    private String invoiceTime;       // 开票时间
    
    // 关联信息
    private String contractId;        // 合同ID
    private String qualityNoteId;     // 质检单ID
    private String transportNoteId;   // 运单ID
    private String weightNoteId;      // 磅单ID
    private String weightNoteNo;      // 磅单计量号
    private String qualityNoteNo;     // 质检单计量号
    private String settlementId;      // 结算单ID
    private String invoiceId;         // 发票ID
    private String paymentId;         // 收款记录ID
    
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
} 