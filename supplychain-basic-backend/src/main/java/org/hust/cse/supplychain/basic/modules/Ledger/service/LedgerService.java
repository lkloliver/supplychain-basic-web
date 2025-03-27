package org.hust.cse.supplychain.basic.modules.Ledger.service;

import org.hust.cse.supplychain.basic.modules.Ledger.dto.LedgerDTO;
import org.hust.cse.supplychain.basic.modules.Ledger.vo.LedgerVO;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface LedgerService {
    // 创建台账记录
    void createLedger(LedgerDTO ledgerDTO, String tenantType);
    
    // 更新台账记录
    void updateLedger(LedgerDTO ledgerDTO, String tenantType);
    
    // 删除台账记录
    void deleteLedger(Long id, String tenantType);
    
    // 获取台账记录详情
    LedgerVO getLedgerDetail(Long id, String tenantType);
    
    // 获取所有台账
    List<LedgerVO> getAllLedgers(String tenantType);
    
    // 根据字段名和值查询台账
    List<LedgerVO> getLedgersByField(String fieldName, String fieldValue, String tenantType);
    
    // 计算税额
    BigDecimal calculateTaxAmount(BigDecimal priceWithoutTax, BigDecimal taxRate);
    
    // 单独设置关联信息
    void setContractId(Long id, String contractId, String tenantType);
    void setQualityNoteId(Long id, String qualityNoteId, String tenantType);
    void setTransportNoteId(Long id, String transportNoteId, String tenantType);
    void setWeightNoteId(Long id, String weightNoteId, String tenantType);
    void setWeightNoteNo(Long id, String weightNoteNo, String tenantType);
    void setQualityNoteNo(Long id, String qualityNoteNo, String tenantType);
    void setSettlementId(Long id, String settlementId, String tenantType);
    void setInvoiceId(Long id, String invoiceId, String tenantType);
    void setPaymentId(Long id, String paymentId, String tenantType);
    
    // 清除关联信息
    void clearContractId(Long id, String tenantType);
    void clearQualityNoteId(Long id, String tenantType);
    void clearTransportNoteId(Long id, String tenantType);
    void clearWeightNoteId(Long id, String tenantType);
    void clearWeightNoteNo(Long id, String tenantType);
    void clearQualityNoteNo(Long id, String tenantType);
    void clearSettlementId(Long id, String tenantType);
    void clearInvoiceId(Long id, String tenantType);
    void clearPaymentId(Long id, String tenantType);
    
    // 根据采购时间范围查询台账
    List<LedgerVO> getLedgersByPurchaseTimeRange(LocalDateTime startTime, LocalDateTime endTime, String tenantType);

    List<LedgerVO> searchLedgers(String keyword, String tenantType);

    // 根据字段值分组查询台账
    Map<String, List<LedgerVO>> getLedgersGroupByField(String fieldName, String tenantType);
} 