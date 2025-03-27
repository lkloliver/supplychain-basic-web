package org.hust.cse.supplychain.basic.modules.Ledger.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LedgerDTO {
    private Long id;
    
    private String tenantType;       // 租户类型：base-基地版，nonbase-非基地版，null-通用数据
    
    private String buyerName;        // 采购方名称
    private String sellerName;       // 出售方名称
    private String sellerTaxType;    // 出售方纳税人类型
    private LocalDateTime purchaseTime; // 采购时间
    private String purchaseLocation; // 采购地点
    private String productName;      // 产品名称
    private BigDecimal actualQuantity; // 实际采购数量
    private BigDecimal priceWithoutTax; // 采购价格（不含税）
    private BigDecimal taxRate;      // 税率
    private BigDecimal taxAmount;    // 税额
    private BigDecimal payableAmount; // 应付金额
    private BigDecimal paidAmount;   // 实付金额
    
    // 关联信息（可以为空）
    @Nullable
    private String contractId;       // 合同ID
    @Nullable
    private String qualityNoteId;    // 质检单ID
    @Nullable
    private String transportNoteId;  // 运单ID
    @Nullable
    private String weightNoteId;     // 磅单ID
    @Nullable
    private String weightNoteNo;     // 磅单计量号
    @Nullable
    private String qualityNoteNo;    // 质检单计量号
    @Nullable
    private String settlementId;     // 结算单ID
    @Nullable
    private String invoiceId;        // 发票ID
    @Nullable
    private String paymentId;        // 付款记录ID
    
    private LocalDateTime invoiceTime; // 开票时间
} 