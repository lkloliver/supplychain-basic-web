package org.hust.cse.supplychain.basic.modules.Ledger.service.impl;

import org.hust.cse.supplychain.basic.modules.Ledger.dao.LedgerDAO;
import org.hust.cse.supplychain.basic.modules.Ledger.dto.LedgerDTO;
import org.hust.cse.supplychain.basic.modules.Ledger.po.LedgerPO;
import org.hust.cse.supplychain.basic.modules.Ledger.service.LedgerService;
import org.hust.cse.supplychain.basic.modules.Ledger.vo.LedgerVO;
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
public class LedgerServiceImpl implements LedgerService {
    
    @Autowired
    private LedgerDAO ledgerDAO;
    
    @Override
    @Transactional
    public void createLedger(LedgerDTO ledgerDTO, String tenantType) {
        LedgerPO ledgerPO = new LedgerPO();
        BeanUtils.copyProperties(ledgerDTO, ledgerPO);
        ledgerPO.setTenantType(tenantType);
        // 计算税额
        ledgerPO.setTaxAmount(calculateTaxAmount(ledgerDTO.getPriceWithoutTax(), ledgerDTO.getTaxRate()));
        ledgerDAO.save(ledgerPO);
    }
    
    @Override
    @Transactional
    public void updateLedger(LedgerDTO ledgerDTO, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(ledgerDTO.getId(), tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            BeanUtils.copyProperties(ledgerDTO, ledgerPO);
            // 计算税额
            ledgerPO.setTaxAmount(calculateTaxAmount(ledgerDTO.getPriceWithoutTax(), ledgerDTO.getTaxRate()));
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void deleteLedger(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            ledgerDAO.deleteById(id);
        }
    }
    
    @Override
    public LedgerVO getLedgerDetail(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            LedgerVO ledgerVO = new LedgerVO();
            BeanUtils.copyProperties(ledgerPO, ledgerVO);
            return ledgerVO;
        }
        return null;
    }
    
    @Override
    public List<LedgerVO> getAllLedgers(String tenantType) {
        List<LedgerPO> ledgerPOs = ledgerDAO.findByTenantType(tenantType);
        return ledgerPOs.stream()
            .map(ledgerPO -> {
                LedgerVO ledgerVO = new LedgerVO();
                BeanUtils.copyProperties(ledgerPO, ledgerVO);
                return ledgerVO;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LedgerVO> getLedgersByField(String fieldName, String fieldValue, String tenantType) {
        List<LedgerPO> ledgerPOs = ledgerDAO.findByField(fieldName, fieldValue, tenantType);
        return ledgerPOs.stream()
            .map(ledgerPO -> {
                LedgerVO ledgerVO = new LedgerVO();
                BeanUtils.copyProperties(ledgerPO, ledgerVO);
                return ledgerVO;
            })
            .collect(Collectors.toList());
    }
    
    // 设置关联信息
    @Override
    @Transactional
    public void setContractId(Long id, String contractId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setContractId(contractId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setQualityNoteId(Long id, String qualityNoteId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteId(qualityNoteId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setTransportNoteId(Long id, String transportNoteId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setTransportNoteId(transportNoteId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setWeightNoteId(Long id, String weightNoteId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteId(weightNoteId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setWeightNoteNo(Long id, String weightNoteNo, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteNo(weightNoteNo);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setQualityNoteNo(Long id, String qualityNoteNo, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteNo(qualityNoteNo);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setSettlementId(Long id, String settlementId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setSettlementId(settlementId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setInvoiceId(Long id, String invoiceId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setInvoiceId(invoiceId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void setPaymentId(Long id, String paymentId, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setPaymentId(paymentId);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    // 清除关联信息
    @Override
    @Transactional
    public void clearContractId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setContractId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearQualityNoteId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearTransportNoteId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setTransportNoteId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearWeightNoteId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearWeightNoteNo(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setWeightNoteNo(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearQualityNoteNo(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setQualityNoteNo(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearSettlementId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setSettlementId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearInvoiceId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setInvoiceId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    @Override
    @Transactional
    public void clearPaymentId(Long id, String tenantType) {
        Optional<LedgerPO> optionalLedgerPO = ledgerDAO.findByIdAndTenantType(id, tenantType);
        if (optionalLedgerPO.isPresent()) {
            LedgerPO ledgerPO = optionalLedgerPO.get();
            ledgerPO.setPaymentId(null);
            ledgerDAO.save(ledgerPO);
        }
    }
    
    // 根据采购时间范围查询台账
    @Override
    public List<LedgerVO> getLedgersByPurchaseTimeRange(LocalDateTime startTime, LocalDateTime endTime, String tenantType) {
        List<LedgerPO> ledgerPOs = ledgerDAO.findByPurchaseTimeBetweenAndTenantType(startTime, endTime, tenantType);
        return ledgerPOs.stream()
            .map(ledgerPO -> {
                LedgerVO ledgerVO = new LedgerVO();
                BeanUtils.copyProperties(ledgerPO, ledgerVO);
                return ledgerVO;
            })
            .collect(Collectors.toList());
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
    public List<LedgerVO> searchLedgers(String keyword, String tenantType) {
        List<LedgerPO> ledgerPOs = ledgerDAO.searchByKeyword(keyword, tenantType);
        return ledgerPOs.stream()
                .map(po -> {
                    LedgerVO vo = new LedgerVO();
                    BeanUtils.copyProperties(po, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    public Map<String, List<LedgerVO>> getLedgersGroupByField(String fieldName, String tenantType) {
        List<LedgerPO> ledgerPOs = ledgerDAO.findByTenantType(tenantType);
        
        // 如果是计量号字段，需要同时从磅单计量号和质检单计量号中查找
        if ("measurementNo".equals(fieldName)) {
            Map<String, List<LedgerVO>> result = new HashMap<>();
            
            // 处理磅单计量号
            ledgerPOs.stream()
                .filter(po -> po.getWeightNoteNo() != null && !po.getWeightNoteNo().isEmpty())
                .forEach(po -> {
                    List<LedgerVO> list = result.computeIfAbsent(po.getWeightNoteNo(), k -> new ArrayList<>());
                    LedgerVO vo = new LedgerVO();
                    BeanUtils.copyProperties(po, vo);
                    list.add(vo);
                });
            
            // 处理质检单计量号
            ledgerPOs.stream()
                .filter(po -> po.getQualityNoteNo() != null && !po.getQualityNoteNo().isEmpty())
                .forEach(po -> {
                    List<LedgerVO> list = result.computeIfAbsent(po.getQualityNoteNo(), k -> new ArrayList<>());
                    LedgerVO vo = new LedgerVO();
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
                        Field field = LedgerPO.class.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(po);
                        return value != null ? value.toString() : "null";
                    } catch (Exception e) {
                        return "error";
                    }
                },
                Collectors.mapping(
                    po -> {
                        LedgerVO vo = new LedgerVO();
                        BeanUtils.copyProperties(po, vo);
                        return vo;
                    },
                    Collectors.toList()
                )
            ));
    }
} 