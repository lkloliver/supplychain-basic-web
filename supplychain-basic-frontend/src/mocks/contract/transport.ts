// 定义运输合同接口
export interface TransportContract {
  id: string;
  contractNo: string;          // 合同编号
  carrier: string;            // 承运方名称
  carrierContact: string;     // 承运方联系人
  carrierPhone: string;       // 承运方联系电话
  shipper: string;            // 托运方名称
  shipperContact: string;     // 托运方联系人
  shipperPhone: string;       // 托运方联系电话
  startDate: string;          // 合同起始时间
  endDate: string;            // 合同截止时间
  transportMethod: string;     // 运输方式
  signDate: string;           // 签订日期
  handler: string;            // 经办人
  transportDetails: TransportDetail[]; // 运输明细
}

// 运输明细接口
export interface TransportDetail {
  startLocation: string;      // 起运地
  endLocation: string;        // 目的地
  goodsCategory: string;      // 名称/品类
  unitPrice: number;          // 含税运输单价
}

// 模拟数据
export const mockTransportContracts: TransportContract[] = [
  {
    id: "TC001",
    contractNo: "YS20240001",
    carrier: "安达物流有限公司",
    carrierContact: "李经理",
    carrierPhone: "13900139000",
    shipper: "上海贸易有限公司",
    shipperContact: "张经理",
    shipperPhone: "13800138000",
    startDate: "2024-01-01",
    endDate: "2024-12-31",
    transportMethod: "公路运输",
    signDate: "2023-12-25",
    handler: "王助理",
    transportDetails: [
      {
        startLocation: "上海市浦东新区",
        endLocation: "北京市朝阳区",
        goodsCategory: "电子产品",
        unitPrice: 2500.00
      },
      {
        startLocation: "上海市松江区",
        endLocation: "天津市滨海新区",
        goodsCategory: "机械设备",
        unitPrice: 3000.00
      }
    ]
  },
  {
    id: "TC002",
    contractNo: "YS20240002",
    carrier: "顺达物流有限公司",
    carrierContact: "赵经理",
    carrierPhone: "13700137000",
    shipper: "广州制造有限公司",
    shipperContact: "陈经理",
    shipperPhone: "13600136000",
    startDate: "2024-02-01",
    endDate: "2024-07-31",
    transportMethod: "铁路运输",
    signDate: "2024-01-15",
    handler: "刘助理",
    transportDetails: [
      {
        startLocation: "广州市黄埔区",
        endLocation: "武汉市江汉区",
        goodsCategory: "家用电器",
        unitPrice: 2000.00
      }
    ]
  }
];

// 模拟AI识别结果
export const mockTransportContractRecognition = {
  contractNo: "YS20240003",
  carrier: "快捷物流有限公司",
  carrierContact: "吴经理",
  carrierPhone: "13500135000",
  shipper: "深圳科技有限公司",
  shipperContact: "周经理",
  shipperPhone: "13400134000",
  startDate: "2024-03-01",
  endDate: "2024-12-31",
  transportMethod: "公路运输",
  signDate: "2024-02-20",
  handler: "孙助理",
  transportDetails: [
    {
      startLocation: "深圳市南山区",
      endLocation: "上海市浦东新区",
      goodsCategory: "电子元件",
      unitPrice: 2800.00
    }
  ]
};

// API函数
export async function fetchTransportContracts(page = 1, pageSize = 10) {
  await new Promise(resolve => setTimeout(resolve, 500));
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  return {
    data: mockTransportContracts.slice(start, end),
    total: mockTransportContracts.length,
    page,
    pageSize
  };
}

export async function deleteTransportContract(id: string) {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { success: true };
}

export async function createTransportContract(data: Partial<TransportContract>) {
  await new Promise(resolve => setTimeout(resolve, 500));
  return {
    success: true,
    data: {
      ...data,
      id: `TC${Date.now()}`,
      createTime: new Date().toISOString()
    }
  };
}

export async function updateTransportContract(id: string, data: Partial<TransportContract>) {
  await new Promise(resolve => setTimeout(resolve, 500));
  return {
    success: true,
    data: {
      ...data,
      id,
      updateTime: new Date().toISOString()
    }
  };
}

export async function getTransportContract(id: string) {
  await new Promise(resolve => setTimeout(resolve, 500));
  const contract = mockTransportContracts.find(c => c.id === id);
  return {
    success: true,
    data: contract || mockTransportContracts[0]
  };
}
