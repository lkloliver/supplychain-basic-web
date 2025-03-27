package org.hust.cse.supplychain.basic.modules.Ledger.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.lang.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sales_ledger", indexes = {
    @Index(name = "idx_tenant", columnList = "tenant_type")
})
public class SalesLedgerPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String tenantType;       // 租户类型：base-基地版，nonbase-非基地版，null-通用数据
    
    private String sellerName;        // 销售方名称
    private String buyerName;         // 购买方名称
    private String buyerTaxType;      // 购买方纳税人类型
    private LocalDateTime saleTime;   // 销售时间
    private String saleLocation;      // 销售地点
    private String productName;       // 产品名称
    private BigDecimal actualQuantity; // 实际销售数量
    private BigDecimal priceWithoutTax; // 销售价格（不含税）
    private BigDecimal taxRate;       // 税率
    private BigDecimal taxAmount;     // 税额
    private BigDecimal receivableAmount; // 应收金额
    private BigDecimal receivedAmount; // 实收金额
    
    // 关联信息（可以为空）
    @Nullable
    private String contractId;        // 合同ID
    @Nullable
    private String qualityNoteId;     // 质检单ID
    @Nullable
    private String transportNoteId;   // 运单ID
    @Nullable
    private String weightNoteId;      // 磅单ID
    @Nullable
    private String weightNoteNo;      // 磅单计量号
    @Nullable
    private String qualityNoteNo;     // 质检单计量号
    @Nullable
    private String settlementId;      // 结算单ID
    @Nullable
    private String invoiceId;         // 发票ID
    @Nullable
    private String paymentId;         // 收款记录ID
    
    private LocalDateTime invoiceTime; // 开票时间
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 