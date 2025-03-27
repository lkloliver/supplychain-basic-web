package org.hust.cse.supplychain.basic.modules.Ledger.controller;

import org.hust.cse.supplychain.basic.modules.Ledger.dto.LedgerDTO;
import org.hust.cse.supplychain.basic.modules.Ledger.service.LedgerService;
import org.hust.cse.supplychain.basic.modules.Ledger.vo.LedgerVO;
import org.hust.cse.supplychain.basic.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {
    
    @Autowired
    private LedgerService ledgerService;
    
    @PostMapping("/create")
    public Result<Void> createLedger(@RequestBody LedgerDTO ledgerDTO, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.createLedger(ledgerDTO, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("创建台账失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/update")
    public Result<Void> updateLedger(@RequestBody LedgerDTO ledgerDTO, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.updateLedger(ledgerDTO, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新台账失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/delete")
    public Result<Void> deleteLedger(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.deleteLedger(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除台账失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/detail")
    public Result<LedgerVO> getLedgerDetail(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            LedgerVO ledgerVO = ledgerService.getLedgerDetail(id, tenantType);
            return Result.success(ledgerVO);
        } catch (Exception e) {
            return Result.error("获取台账详情失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/list")
    public Result<List<LedgerVO>> getAllLedgers(@RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            List<LedgerVO> ledgerVOs = ledgerService.getAllLedgers(tenantType);
            return Result.success(ledgerVOs);
        } catch (Exception e) {
            return Result.error("获取台账列表失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/query")
    public Result<List<LedgerVO>> getLedgersByField(@RequestParam String fieldName, @RequestParam String fieldValue, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            List<LedgerVO> ledgers = ledgerService.getLedgersByField(fieldName, fieldValue, tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("查询台账失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/time-range")
    public Result<List<LedgerVO>> getLedgersByPurchaseTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            List<LedgerVO> ledgerVOs = ledgerService.getLedgersByPurchaseTimeRange(startTime, endTime, tenantType);
            return Result.success(ledgerVOs);
        } catch (Exception e) {
            return Result.error("获取时间范围内的台账失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setContract")
    public Result<Void> setContractId(@RequestParam Long id, @RequestParam String contractId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setContractId(id, contractId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置合同ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setQualityNote")
    public Result<Void> setQualityNoteId(@RequestParam Long id, @RequestParam String qualityNoteId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setQualityNoteId(id, qualityNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置质检单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setTransportNote")
    public Result<Void> setTransportNoteId(@RequestParam Long id, @RequestParam String transportNoteId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setTransportNoteId(id, transportNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置运单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setWeightNote")
    public Result<Void> setWeightNoteId(@RequestParam Long id, @RequestParam String weightNoteId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setWeightNoteId(id, weightNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置磅单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setWeightNoteNo")
    public Result<Void> setWeightNoteNo(@RequestParam Long id, @RequestParam String weightNoteNo, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setWeightNoteNo(id, weightNoteNo, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置磅单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setQualityNoteNo")
    public Result<Void> setQualityNoteNo(@RequestParam Long id, @RequestParam String qualityNoteNo, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setQualityNoteNo(id, qualityNoteNo, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置质检单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setSettlement")
    public Result<Void> setSettlementId(@RequestParam Long id, @RequestParam String settlementId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setSettlementId(id, settlementId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置结算单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setInvoice")
    public Result<Void> setInvoiceId(@RequestParam Long id, @RequestParam String invoiceId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setInvoiceId(id, invoiceId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置发票ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setPayment")
    public Result<Void> setPaymentId(@RequestParam Long id, @RequestParam String paymentId, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.setPaymentId(id, paymentId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置付款记录ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearContract")
    public Result<Void> clearContractId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearContractId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除合同ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearQualityNote")
    public Result<Void> clearQualityNoteId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearQualityNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除质检单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearTransportNote")
    public Result<Void> clearTransportNoteId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearTransportNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除运单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearWeightNote")
    public Result<Void> clearWeightNoteId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearWeightNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除磅单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearWeightNoteNo")
    public Result<Void> clearWeightNoteNo(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearWeightNoteNo(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除磅单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearQualityNoteNo")
    public Result<Void> clearQualityNoteNo(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearQualityNoteNo(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除质检单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearSettlement")
    public Result<Void> clearSettlementId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearSettlementId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除结算单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearInvoice")
    public Result<Void> clearInvoiceId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearInvoiceId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除发票ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearPayment")
    public Result<Void> clearPaymentId(@RequestParam Long id, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            ledgerService.clearPaymentId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除付款记录ID失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public Result<List<LedgerVO>> searchLedgers(@RequestParam String keyword, @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            List<LedgerVO> ledgers = ledgerService.searchLedgers(keyword, tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("模糊查询采购台账失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/groupByField")
    public Result<Map<String, List<LedgerVO>>> getLedgersGroupByField(
            @RequestParam String fieldName,
            @RequestHeader("X-Tenant-Type") String tenantType) {
        try {
            Map<String, List<LedgerVO>> result = ledgerService.getLedgersGroupByField(fieldName, tenantType);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("按字段分组查询台账失败：" + e.getMessage());
        }
    }
} 