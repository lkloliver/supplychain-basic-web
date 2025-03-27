// 销售台账 API 服务
const salesLedgerApi = {
    // 创建销售台账
    createLedger: async (ledgerData, tenantType) => {
        try {
            const response = await fetch('/api/sales-ledger/create', {
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
            return { success: false, message: '创建销售台账失败：' + error.message };
        }
    },

    // 更新销售台账
    updateLedger: async (ledgerData, tenantType) => {
        try {
            const response = await fetch('/api/sales-ledger/update', {
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
            return { success: false, message: '更新销售台账失败：' + error.message };
        }
    },

    // 删除销售台账
    deleteLedger: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/delete?id=${id}`, {
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
            return { success: false, message: '删除销售台账失败：' + error.message };
        }
    },

    // 获取销售台账详情
    getLedgerDetail: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/detail?id=${id}`, {
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
            return { success: false, message: '获取销售台账详情失败：' + error.message };
        }
    },

    // 获取所有销售台账
    getAllLedgers: async (tenantType) => {
        try {
            const response = await fetch('/api/sales-ledger/all', {
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
            return { success: false, message: '获取所有销售台账失败：' + error.message };
        }
    },

    // 根据字段查询销售台账
    getLedgersByField: async (fieldName, fieldValue, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/query?fieldName=${fieldName}&fieldValue=${fieldValue}`, {
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
            return { success: false, message: '查询销售台账失败：' + error.message };
        }
    },

    // 根据字段值分组查询销售台账
    getLedgersGroupByField: async (fieldName, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/groupByField?fieldName=${fieldName}`, {
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
            return { success: false, message: '按字段分组查询销售台账失败：' + error.message };
        }
    },

    // 设置关联信息
    setContractId: async (id, contractId, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/setContract?id=${id}&contractId=${contractId}`, {
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
            const response = await fetch(`/api/sales-ledger/setWeightNote?id=${id}&weightNoteId=${weightNoteId}`, {
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
            const response = await fetch(`/api/sales-ledger/setQualityNoteNo?id=${id}&qualityNoteNo=${qualityNoteNo}`, {
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
            const response = await fetch(`/api/sales-ledger/setTransportNote?id=${id}&transportNoteId=${transportNoteId}`, {
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
            const response = await fetch(`/api/sales-ledger/setQualityNote?id=${id}&qualityNoteId=${qualityNoteId}`, {
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
            const response = await fetch(`/api/sales-ledger/setSettlement?id=${id}&settlementId=${settlementId}`, {
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
            const response = await fetch(`/api/sales-ledger/setInvoice?id=${id}&invoiceId=${invoiceId}`, {
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
            const response = await fetch(`/api/sales-ledger/setPayment?id=${id}&paymentId=${paymentId}`, {
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
            return { success: false, message: '设置收款记录ID失败：' + error.message };
        }
    },

    // 清除关联信息
    clearContractId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/clearContract?id=${id}`, {
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
            const response = await fetch(`/api/sales-ledger/clearWeightNote?id=${id}`, {
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

    // 清除质检单ID
    clearQualityNoteId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/clearQualityNote?id=${id}`, {
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

    clearSettlementId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/clearSettlement?id=${id}`, {
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
            const response = await fetch(`/api/sales-ledger/clearInvoice?id=${id}`, {
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
            const response = await fetch(`/api/sales-ledger/clearPayment?id=${id}`, {
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
            return { success: false, message: '清除收款记录ID失败：' + error.message };
        }
    },

    // 根据销售时间范围查询台账
    getLedgersByTimeRange: async (startTime, endTime, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/queryByTimeRange?startTime=${startTime}&endTime=${endTime}`, {
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
            return { success: false, message: '按时间范围查询销售台账失败：' + error.message };
        }
    },

    // 模糊查询销售台账
    searchLedgers: async (keyword, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/search?keyword=${encodeURIComponent(keyword)}`, {
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
            return { success: false, message: '模糊查询销售台账失败：' + error.message };
        }
    },

    // 清除运输单ID
    clearTransportNoteId: async (id, tenantType) => {
        try {
            const response = await fetch(`/api/sales-ledger/clearTransportNote?id=${id}`, {
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
            const response = await fetch(`/api/sales-ledger/setTransportNoteNo?id=${id}&transportNoteNo=${transportNoteNo}`, {
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
            const response = await fetch(`/api/sales-ledger/clearTransportNoteNo?id=${id}`, {
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
            const response = await fetch(`/api/sales-ledger/clearQualityNoteNo?id=${id}`, {
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
export default salesLedgerApi; 