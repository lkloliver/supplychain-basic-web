export interface TransportContract {
  id: string;
  contractNo: string;
  carrier: string;  // 承运方信息
  shipper: string;  // 托运方信息
  startTime: string;  // 合同起始时间
  endTime: string;  // 合同截止时间
  signDate: string;  // 签订日期
  handler: string;  // 经办人
  transportMethod: string;  // 运输方式
  transportDetails: {
    startLocation: string;
    endLocation: string;
    category: string;
    unitPrice: number;
  }[];
  status: 'active' | 'expired' | 'completed';
}

export const mockTransportContracts: TransportContract[] = [
  {
    id: '1',
    contractNo: 'YS-2024-001',
    carrier: '快达物流有限公司',
    shipper: '某某贸易有限公司',
    startTime: '2024-01-01',
    endTime: '2024-12-31',
    signDate: '2023-12-25',
    handler: '张三',
    transportMethod: '公路运输',
    transportDetails: [
      {
        startLocation: '上海仓库',
        endLocation: '北京配送中心',
        category: '普通货物',
        unitPrice: 2500
      },
      {
        startLocation: '广州仓库',
        endLocation: '深圳配送中心',
        category: '冷链货物',
        unitPrice: 3500
      }
    ],
    status: 'active'
  },
  {
    id: '2',
    contractNo: 'YS-2024-002',
    carrier: '安达运输公司',
    shipper: '某某制造有限公司',
    startTime: '2024-02-01',
    endTime: '2024-12-31',
    signDate: '2024-01-15',
    handler: '李四',
    transportMethod: '铁路运输',
    transportDetails: [
      {
        startLocation: '武汉仓库',
        endLocation: '成都配送中心',
        category: '大宗货物',
        unitPrice: 4200
      }
    ],
    status: 'active'
  },
  {
    id: '3',
    contractNo: 'TC-2023003',
    carrier: '山东物流发展有限公司',
    shipper: '某科技公司',
    startTime: '2023-03-01',
    endTime: '2023-06-30',
    signDate: '2023-03-10',
    handler: '王五',
    transportMethod: '公路运输',
    transportDetails: [
      {
        startLocation: '济南',
        endLocation: '武汉',
        category: '精密设备',
        unitPrice: 4.2
      }
    ],
    status: 'completed'
  }
];

// 模拟AI识别结果
export const mockTransportContractRecognition = {
  carrier: '某物流公司',
  shipper: '我方公司',
  startTime: '2023-07-01',
  endTime: '2024-06-30',
  signDate: '2023-06-25',
  handler: '李四',
  transportMethod: '公路运输',
  transportDetails: [
    {
      startLocation: '深圳',
      endLocation: '武汉',
      category: '普通货物',
      unitPrice: 2500
    }
  ]
};

console.log('Mock data:', mockTransportContracts);

export const fetchTransportContracts = async (page: number) => {
  const response = {
    data: mockTransportContracts,
    total: mockTransportContracts.length,
    pageSize: 10,
    success: true
  };
  console.log('Fetched data:', response);
  return response;
};

// 模拟删除操作
export const deleteTransportContract = async (id: string) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { success: true };
}; 