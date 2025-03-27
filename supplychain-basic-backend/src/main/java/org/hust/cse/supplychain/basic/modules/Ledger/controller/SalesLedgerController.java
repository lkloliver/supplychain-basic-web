package org.hust.cse.supplychain.basic.modules.Ledger.controller;

import org.hust.cse.supplychain.basic.modules.Ledger.dto.SalesLedgerDTO;
import org.hust.cse.supplychain.basic.modules.Ledger.service.SalesLedgerService;
import org.hust.cse.supplychain.basic.modules.Ledger.vo.SalesLedgerVO;
import org.hust.cse.supplychain.basic.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-ledger")
public class SalesLedgerController {
    
    @Autowired
    private SalesLedgerService salesLedgerService;
    
    @PostMapping("/create")
    public Result<Void> createLedger(@RequestBody SalesLedgerDTO ledgerDTO, @RequestParam String tenantType) {
        try {
            salesLedgerService.createLedger(ledgerDTO, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("创建销售台账失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/update")
    public Result<Void> updateLedger(@RequestBody SalesLedgerDTO ledgerDTO, @RequestParam String tenantType) {
        try {
            salesLedgerService.updateLedger(ledgerDTO, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新销售台账失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/delete")
    public Result<Void> deleteLedger(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.deleteLedger(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除销售台账失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/detail")
    public Result<SalesLedgerVO> getLedgerDetail(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            SalesLedgerVO ledgerVO = salesLedgerService.getLedgerDetail(id, tenantType);
            return Result.success(ledgerVO);
        } catch (Exception e) {
            return Result.error("获取销售台账详情失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/all")
    public Result<List<SalesLedgerVO>> getAllLedgers(@RequestParam String tenantType) {
        try {
            List<SalesLedgerVO> ledgers = salesLedgerService.getAllLedgers(tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("获取所有销售台账失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/query")
    public Result<List<SalesLedgerVO>> getLedgersByField(@RequestParam String fieldName, @RequestParam String fieldValue, @RequestParam String tenantType) {
        try {
            List<SalesLedgerVO> ledgers = salesLedgerService.getLedgersByField(fieldName, fieldValue, tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("查询销售台账失败：" + e.getMessage());
        }
    }
    
    // 设置关联信息
    @PostMapping("/setContract")
    public Result<Void> setContractId(@RequestParam Long id, @RequestParam String contractId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setContractId(id, contractId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置合同ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setQualityNote")
    public Result<Void> setQualityNoteId(@RequestParam Long id, @RequestParam String qualityNoteId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setQualityNoteId(id, qualityNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置质检单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setTransportNote")
    public Result<Void> setTransportNoteId(@RequestParam Long id, @RequestParam String transportNoteId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setTransportNoteId(id, transportNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置运单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setWeightNote")
    public Result<Void> setWeightNoteId(@RequestParam Long id, @RequestParam String weightNoteId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setWeightNoteId(id, weightNoteId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置磅单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setWeightNoteNo")
    public Result<Void> setWeightNoteNo(@RequestParam Long id, @RequestParam String weightNoteNo, @RequestParam String tenantType) {
        try {
            salesLedgerService.setWeightNoteNo(id, weightNoteNo, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置磅单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setQualityNoteNo")
    public Result<Void> setQualityNoteNo(@RequestParam Long id, @RequestParam String qualityNoteNo, @RequestParam String tenantType) {
        try {
            salesLedgerService.setQualityNoteNo(id, qualityNoteNo, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置质检单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setSettlement")
    public Result<Void> setSettlementId(@RequestParam Long id, @RequestParam String settlementId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setSettlementId(id, settlementId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置结算单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setInvoice")
    public Result<Void> setInvoiceId(@RequestParam Long id, @RequestParam String invoiceId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setInvoiceId(id, invoiceId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置发票ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/setPayment")
    public Result<Void> setPaymentId(@RequestParam Long id, @RequestParam String paymentId, @RequestParam String tenantType) {
        try {
            salesLedgerService.setPaymentId(id, paymentId, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("设置收款记录ID失败：" + e.getMessage());
        }
    }
    
    // 清除关联信息
    @PostMapping("/clearContract")
    public Result<Void> clearContractId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearContractId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除合同ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearQualityNote")
    public Result<Void> clearQualityNoteId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearQualityNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除质检单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearTransportNote")
    public Result<Void> clearTransportNoteId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearTransportNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除运单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearWeightNote")
    public Result<Void> clearWeightNoteId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearWeightNoteId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除磅单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearWeightNoteNo")
    public Result<Void> clearWeightNoteNo(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearWeightNoteNo(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除磅单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearQualityNoteNo")
    public Result<Void> clearQualityNoteNo(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearQualityNoteNo(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除质检单计量号失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearSettlement")
    public Result<Void> clearSettlementId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearSettlementId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除结算单ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearInvoice")
    public Result<Void> clearInvoiceId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearInvoiceId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除发票ID失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/clearPayment")
    public Result<Void> clearPaymentId(@RequestParam Long id, @RequestParam String tenantType) {
        try {
            salesLedgerService.clearPaymentId(id, tenantType);
            return Result.success();
        } catch (Exception e) {
            return Result.error("清除收款记录ID失败：" + e.getMessage());
        }
    }
    
    // 根据销售时间范围查询台账
    @GetMapping("/queryByTimeRange")
    public Result<List<SalesLedgerVO>> getLedgersByTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam String tenantType) {
        try {
            List<SalesLedgerVO> ledgers = salesLedgerService.getLedgersBySaleTimeRange(startTime, endTime, tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("按时间范围查询销售台账失败：" + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result<List<SalesLedgerVO>> searchLedgers(@RequestParam String keyword, @RequestParam String tenantType) {
        try {
            List<SalesLedgerVO> ledgers = salesLedgerService.searchLedgers(keyword, tenantType);
            return Result.success(ledgers);
        } catch (Exception e) {
            return Result.error("搜索销售台账失败：" + e.getMessage());
        }
    }

    @GetMapping("/groupByField")
    public Result<Map<String, List<SalesLedgerVO>>> getLedgersGroupByField(
            @RequestParam String fieldName,
            @RequestParam String tenantType) {
        try {
            Map<String, List<SalesLedgerVO>> result = salesLedgerService.getLedgersGroupByField(fieldName, tenantType);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("按字段分组查询销售台账失败：" + e.getMessage());
        }
    }
} 