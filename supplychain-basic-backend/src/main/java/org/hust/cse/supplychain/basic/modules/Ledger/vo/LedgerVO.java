package org.hust.cse.supplychain.basic.modules.Ledger.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LedgerVO {
    private Long id; // 主键ID
    
    private String buyerName;        // 采购方名称
    private String sellerName;       // 出售方名称
    private String sellerTaxType;    // 出售方纳税人类型
    private String purchaseTime;     // 采购时间
    private String purchaseLocation; // 采购地点
    private String productName;      // 产品名称
    private BigDecimal actualQuantity; // 实际采购数量
    private BigDecimal priceWithoutTax; // 采购价格（不含税）
    private BigDecimal taxRate;      // 税率
    private BigDecimal taxAmount;    // 税额
    private BigDecimal payableAmount; // 应付金额
    private BigDecimal paidAmount;   // 实付金额
    private String invoiceTime;      // 开票时间
    
    // 关联信息
    private String contractId;       // 合同ID
    private String qualityNoteId;    // 质检单ID
    private String transportNoteId;  // 运单ID
    private String weightNoteId;     // 磅单ID
    private String weightNoteNo;     // 磅单计量号
    private String qualityNoteNo;    // 质检单计量号
    private String settlementId;     // 结算单ID
    private String invoiceId;        // 发票ID
    private String paymentId;        // 付款记录ID
    
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
} 