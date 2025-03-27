package org.hust.cse.supplychain.basic.modules.Ledger.service.impl;

import org.hust.cse.supplychain.basic.modules.Ledger.dao.SalesLedgerDAO;
import org.hust.cse.supplychain.basic.modules.Ledger.dto.SalesLedgerDTO;
import org.hust.cse.supplychain.basic.modules.Ledger.po.SalesLedgerPO;
import org.hust.cse.supplychain.basic.modules.Ledger.service.SalesLedgerService;
import org.hust.cse.supplychain.basic.modules.Ledger.vo.SalesLedgerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Field;

@Service
public class SalesLedgerServiceImpl implements SalesLedgerService {
    
    @Autowired
    private SalesLedgerDAO salesLedgerDAO;
    
    @Override
    @Transactional
    public void createLedger(SalesLedgerDTO ledgerDTO, String tenantType) {
        SalesLedgerPO ledgerPO = new SalesLedgerPO();
        BeanUtils.copyProperties(ledgerDTO, ledgerPO);
        ledgerPO.setTenantType(tenantType);
        // 计算税额
        ledgerPO.setTaxAmount(calculateTaxAmount(ledgerDTO.getPriceWithoutTax(), ledgerDTO.getTaxRate()));
        salesLedgerDAO.save(ledgerPO);
    }
    
    @Override
    @Transactional
    public void updateLedger(SalesLedgerDTO ledgerDTO, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(ledgerDTO.getId(), tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            BeanUtils.copyProperties(ledgerDTO, ledgerPO);
            // 计算税额
            ledgerPO.setTaxAmount(calculateTaxAmount(ledgerDTO.getPriceWithoutTax(), ledgerDTO.getTaxRate()));
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void deleteLedger(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            salesLedgerDAO.deleteById(id);
        }
    }
    
    @Override
    public SalesLedgerVO getLedgerDetail(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerVO ledgerVO = new SalesLedgerVO();
            BeanUtils.copyProperties(optionalLedgerPO.get(), ledgerVO);
            return ledgerVO;
        }
        return null;
    }
    
    @Override
    public List<SalesLedgerVO> getAllLedgers(String tenantType) {
        List<SalesLedgerPO> ledgerPOs = salesLedgerDAO.findByTenantType(tenantType);
        return ledgerPOs.stream().map(ledgerPO -> {
            SalesLedgerVO ledgerVO = new SalesLedgerVO();
            BeanUtils.copyProperties(ledgerPO, ledgerVO);
            return ledgerVO;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<SalesLedgerVO> getLedgersByField(String fieldName, String fieldValue, String tenantType) {
        List<SalesLedgerPO> ledgerPOs = salesLedgerDAO.findByField(fieldName, fieldValue, tenantType);
        return ledgerPOs.stream().map(ledgerPO -> {
            SalesLedgerVO ledgerVO = new SalesLedgerVO();
            BeanUtils.copyProperties(ledgerPO, ledgerVO);
            return ledgerVO;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<SalesLedgerVO> getLedgersBySaleTimeRange(LocalDateTime startTime, LocalDateTime endTime, String tenantType) {
        List<SalesLedgerPO> ledgerPOs = salesLedgerDAO.findBySaleTimeBetweenAndTenantType(startTime, endTime, tenantType);
        return ledgerPOs.stream().map(ledgerPO -> {
            SalesLedgerVO ledgerVO = new SalesLedgerVO();
            BeanUtils.copyProperties(ledgerPO, ledgerVO);
            return ledgerVO;
        }).collect(Collectors.toList());
    }
    
    // 设置关联信息
    @Override
    @Transactional
    public void setContractId(Long id, String contractId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setContractId(contractId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setQualityNoteId(Long id, String qualityNoteId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteId(qualityNoteId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setTransportNoteId(Long id, String transportNoteId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setTransportNoteId(transportNoteId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setWeightNoteId(Long id, String weightNoteId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteId(weightNoteId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setWeightNoteNo(Long id, String weightNoteNo, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteNo(weightNoteNo);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setQualityNoteNo(Long id, String qualityNoteNo, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteNo(qualityNoteNo);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setSettlementId(Long id, String settlementId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setSettlementId(settlementId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setInvoiceId(Long id, String invoiceId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setInvoiceId(invoiceId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setPaymentId(Long id, String paymentId, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setPaymentId(paymentId);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    // 清除关联信息
    @Override
    @Transactional
    public void clearContractId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setContractId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearQualityNoteId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearTransportNoteId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setTransportNoteId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearWeightNoteId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearWeightNoteNo(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteNo(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearQualityNoteNo(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteNo(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearSettlementId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setSettlementId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearInvoiceId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setInvoiceId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearPaymentId(Long id, String tenantType) {
        Optional<SalesLedgerPO> optionalLedgerPO = salesLedgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            SalesLedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setPaymentId(null);
            salesLedgerDAO.save(ledgerPO);
        }
    }
    
    // 计算税额
    @Override
    public BigDecimal calculateTaxAmount(BigDecimal priceWithoutTax, BigDecimal taxRate) {
        if (priceWithoutTax == null || taxRate == null) {
            return BigDecimal.ZERO;
        }
        return priceWithoutTax.multiply(taxRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
    
    @Override
    public List<SalesLedgerVO> searchLedgers(String keyword, String tenantType) {
        List<SalesLedgerPO> ledgerPOs = salesLedgerDAO.searchByKeyword(keyword, tenantType);
        return ledgerPOs.stream()
                .map(po -> {
                    SalesLedgerVO vo = new SalesLedgerVO();
                    BeanUtils.copyProperties(po, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, List<SalesLedgerVO>> getLedgersGroupByField(String fieldName, String tenantType) {
        List<SalesLedgerPO> ledgerPOs = salesLedgerDAO.findByTenantType(tenantType);
        
        // 如果是计量号字段，需要同时从磅单计量号和质检单计量号中查找
        if ("measurementNo".equals(fieldName)) {
            Map<String, List<SalesLedgerVO>> result = new HashMap<>();
            
            // 处理磅单计量号
            ledgerPOs.stream()
                .filter(po -> po.getWeightNoteNo() != null && !po.getWeightNoteNo().isEmpty())
                .forEach(po -> {
                    List<SalesLedgerVO> list = result.computeIfAbsent(po.getWeightNoteNo(), k -> new ArrayList<>());
                    SalesLedgerVO vo = new SalesLedgerVO();
                    BeanUtils.copyProperties(po, vo);
                    list.add(vo);
                });
            
            // 处理质检单计量号
            ledgerPOs.stream()
                .filter(po -> po.getQualityNoteNo() != null && !po.getQualityNoteNo().isEmpty())
                .forEach(po -> {
                    List<SalesLedgerVO> list = result.computeIfAbsent(po.getQualityNoteNo(), k -> new ArrayList<>());
                    SalesLedgerVO vo = new SalesLedgerVO();
                    BeanUtils.copyProperties(po, vo);
                    list.add(vo);
                });
            
            return result;
        }
        
        // 其他字段的处理
        return ledgerPOs.stream()
            .collect(Collectors.groupingBy(
                po -> {
                    try {
                        Field field = SalesLedgerPO.class.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(po);
                        return value != null ? value.toString() : "null";
                    } catch (Exception e) {
                        return "error";
                    }
                },
                Collectors.mapping(
                    po -> {
                        SalesLedgerVO vo = new SalesLedgerVO();
                        BeanUtils.copyProperties(po, vo);
                        return vo;
                    },
                    Collectors.toList()
                )
            ));
    }
} 