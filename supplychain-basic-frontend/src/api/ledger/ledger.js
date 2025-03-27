// 采购台账 API 服务
const ledgerApi = {
    // 创建采购台账
    createLedger: async (ledgerData, tenantType) => {
        try {
            const response = await fetch('/api/ledger/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Tenant-Type': tenantType
                },
                body: JSON.stringify(ledgerData)
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '创建采购台账失败：' + error.message };
        }
    },

    // 更新采购台账
    updateLedger: async (ledgerData, tenantType) => {
        try {
            const response = await fetch('/api/ledger/update', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Tenant-Type': tenantType
                },
                body: JSON.stringify(ledgerData)
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '更新采购台账失败：' + error.message };
        }
    },

    // 删除采购台账
    deleteLedger: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/delete?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '删除采购台账失败：' + error.message };
        }
    },

    // 获取采购台账详情
    getLedgerDetail: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/detail?id=${id}`, {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '获取采购台账详情失败：' + error.message };
        }
    },

    // 获取所有采购台账
    getAllLedgers: async (tenantType) => {
        try {
            const response = await fetch('/api/ledger/all', {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '获取所有采购台账失败：' + error.message };
        }
    },

    // 根据字段查询采购台账
    getLedgersByField: async (fieldName, fieldValue, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/query?fieldName=${fieldName}&fieldValue=${fieldValue}`, {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '查询采购台账失败：' + error.message };
        }
    },

    // 根据字段值分组查询采购台账
    getLedgersGroupByField: async (fieldName, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/groupByField?fieldName=${fieldName}`, {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '按字段分组查询采购台账失败：' + error.message };
        }
    },

    // 设置关联信息
    setContractId: async (id, contractId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setContract?id=${id}&contractId=${contractId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置合同ID失败：' + error.message };
        }
    },

    setWeightNoteId: async (id, weightNoteId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setWeightNote?id=${id}&weightNoteId=${weightNoteId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置磅单ID失败：' + error.message };
        }
    },

    // 设置质检单号
    setQualityNoteNo: async (id, qualityNoteNo, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setQualityNoteNo?id=${id}&qualityNoteNo=${qualityNoteNo}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置质检单号失败：' + error.message };
        }
    },

    // 设置运输单ID
    setTransportNoteId: async (id, transportNoteId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setTransportNote?id=${id}&transportNoteId=${transportNoteId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置运输单ID失败：' + error.message };
        }
    },

    // 设置质检单ID
    setQualityNoteId: async (id, qualityNoteId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setQualityNote?id=${id}&qualityNoteId=${qualityNoteId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置质检单ID失败：' + error.message };
        }
    },

    setSettlementId: async (id, settlementId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setSettlement?id=${id}&settlementId=${settlementId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置结算单ID失败：' + error.message };
        }
    },

    setInvoiceId: async (id, invoiceId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setInvoice?id=${id}&invoiceId=${invoiceId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置发票ID失败：' + error.message };
        }
    },

    setPaymentId: async (id, paymentId, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setPayment?id=${id}&paymentId=${paymentId}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置付款记录ID失败：' + error.message };
        }
    },

    // 清除关联信息
    clearContractId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearContract?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除合同ID失败：' + error.message };
        }
    },

    clearWeightNoteId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearWeightNote?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除磅单ID失败：' + error.message };
        }
    },

    clearSettlementId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearSettlement?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除结算单ID失败：' + error.message };
        }
    },

    clearInvoiceId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearInvoice?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除发票ID失败：' + error.message };
        }
    },

    clearPaymentId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearPayment?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除付款记录ID失败：' + error.message };
        }
    },

    // 根据采购时间范围查询台账
    getLedgersByTimeRange: async (startTime, endTime, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/queryByTimeRange?startTime=${startTime}&endTime=${endTime}`, {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '按时间范围查询采购台账失败：' + error.message };
        }
    },

    // 模糊查询采购台账
    searchLedgers: async (keyword, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/search?keyword=${encodeURIComponent(keyword)}`, {
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '模糊查询采购台账失败：' + error.message };
        }
    },

    // 清除质检单ID
    clearQualityNoteId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearQualityNote?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除质检单ID失败：' + error.message };
        }
    },

    // 清除运输单ID
    clearTransportNoteId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearTransportNote?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除运输单ID失败：' + error.message };
        }
    },

    // 设置运输单号
    setTransportNoteNo: async (id, transportNoteNo, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/setTransportNoteNo?id=${id}&transportNoteNo=${transportNoteNo}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '设置运输单号失败：' + error.message };
        }
    },

    // 清除运输单号
    clearTransportNoteNo: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearTransportNoteNo?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除运输单号失败：' + error.message };
        }
    },

    // 清除质检单号
    clearQualityNoteNo: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/ledger/clearQualityNoteNo?id=${id}`, {
                method: 'POST',
                headers: {
                    'X-Tenant-Type': tenantType
                }
            });
            const result = await response.json();
            if (result.code === 200) {
                return { success: true, data: result.data };
            } else {
                return { success: false, message: result.message };
            }
        } catch (error) {
            return { success: false, message: '清除质检单号失败：' + error.message };
        }
    }
};

// 导出 API 服务
export default ledgerApi; 