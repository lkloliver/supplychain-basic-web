package org.hust.cse.supplychain.basic.modules.Ledger.dao;

import org.hust.cse.supplychain.basic.modules.Ledger.po.SalesLedgerPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesLedgerDAO extends JpaRepository<SalesLedgerPO, Long> {
    
    // 根据ID和租户类型查询
    Optional<SalesLedgerPO> findByIdAndTenantType(Long id, String tenantType);
    
    // 根据租户类型查询
    List<SalesLedgerPO> findByTenantType(String tenantType);
    
    // 根据字段名和值动态查询
    @Query(
        value = "SELECT * FROM SalesLedgerPO " +
                "WHERE ?#{@columnValidator.validateColumn(#fieldName)} " +
                "= :fieldValue AND tenant_type = :tenantType",
        nativeQuery = true
    )
    List<SalesLedgerPO> findByField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue, @Param("tenantType") String tenantType);
    
    // 根据合同编号查询
    List<SalesLedgerPO> findByContractIdAndTenantType(String contractId, String tenantType);
    
    // 根据车辆编号查询
    List<SalesLedgerPO> findByVehicleIdAndTenantType(String vehicleId, String tenantType);
    
    // 根据磅单编号查询
    List<SalesLedgerPO> findByWeightNoteIdAndTenantType(String weightNoteId, String tenantType);
    
    // 根据结算单编号查询
    List<SalesLedgerPO> findBySettlementIdAndTenantType(String settlementId, String tenantType);
    
    // 根据发票编号查询
    List<SalesLedgerPO> findByInvoiceIdAndTenantType(String invoiceId, String tenantType);
    
    // 根据收款记录编号查询
    List<SalesLedgerPO> findByPaymentIdAndTenantType(String paymentId, String tenantType);
    
    // 根据销售时间范围查询
    List<SalesLedgerPO> findBySaleTimeBetweenAndTenantType(LocalDateTime startTime, LocalDateTime endTime, String tenantType);
    
    // 全字段模糊查询
    @Query("SELECT l FROM SalesLedgerPO l WHERE l.tenantType = :tenantType AND " +
           "(LOWER(l.sellerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.buyerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.buyerTaxType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.saleLocation) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.contractId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.vehicleId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.weightNoteId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.settlementId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.invoiceId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.paymentId) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SalesLedgerPO> searchByKeyword(@Param("keyword") String keyword, @Param("tenantType") String tenantType);
} 