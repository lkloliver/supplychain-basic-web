// 销售合同模拟数据
export interface SalesContract {
  id: string;
  contractNo: string;
  signDate: string;
  buyer: string;
  seller: string;
  paymentAccount: string;
  receivingAccount: string;
  taxRate: string;
  invoiceType: string;
  paymentDate: string;
  paymentMethod: string;
  performancePeriod: string;
  performanceLocation: string;
  performanceMethod: string;
  handler: string;
  totalAmount: number;
  remarks?: string;
  goods: {
    id?: number;
    name: string;
    specification: string;
    quantity: number;
    price: number;
  }[];
}

export const mockSalesContracts: SalesContract[] = [
  {
    id: '1',
    contractNo: 'SC-2023001',
    signDate: '2023-01-15',
    buyer: '上海食品有限公司',
    seller: '我方公司',
    paymentAccount: '622848203910392832',
    receivingAccount: '622848203910392833',
    taxRate: '13%',
    invoiceType: '增值税专用发票',
    paymentDate: '2023-02-15',
    paymentMethod: '银行转账',
    performancePeriod: '2023-01-15至2023-12-31',
    performanceLocation: '上海市浦东新区',
    performanceMethod: '分批交付',
    handler: '张三',
    totalAmount: 3200,
    remarks: '年度销售合同',
    goods: [
      {
        id: 1,
        name: '小麦',
        specification: '优质',
        quantity: 1000,
        price: 3.2
      }
    ]
  },
  {
    id: '2',
    contractNo: 'SC-2023002',
    signDate: '2023-02-20',
    buyer: '北京餐饮集团',
    seller: '我方公司',
    paymentAccount: '622848203910392834',
    receivingAccount: '622848203910392835',
    taxRate: '13%',
    invoiceType: '增值税专用发票',
    paymentDate: '2023-03-20',
    paymentMethod: '银行转账',
    performancePeriod: '2023-02-20至2023-12-31',
    performanceLocation: '北京市朝阳区',
    performanceMethod: '一次性交付',
    handler: '李四',
    totalAmount: 3750,
    remarks: '季度销售合同',
    goods: [
      {
        id: 2,
        name: '玉米',
        specification: '标准',
        quantity: 1500,
        price: 2.5
      }
    ]
  }
];

// 模拟API请求
export const fetchSalesContracts = async (page = 1, pageSize = 10) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  const data = mockSalesContracts.slice(start, end);
  
  return {
    data,
    total: mockSalesContracts.length,
    page,
    pageSize
  };
};

// 模拟删除操作
export const deleteSalesContract = async (id: string) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { success: true };
};

// 模拟AI识别结果
export const mockSalesContractRecognition = {
  contractNo: 'SC-2023-002',
  signDate: '2023-06-01',
  buyer: '上海客户B有限公司',
  seller: '我方公司',
  paymentAccount: '622848203910392834',
  receivingAccount: '622848203910392835',
  taxRate: '13%',
  invoiceType: '增值税专用发票',
  paymentDate: '2023-07-01',
  paymentMethod: '银行转账',
  performancePeriod: '2023-06-01至2023-12-31',
  performanceLocation: '上海市浦东新区某某路456号',
  performanceMethod: '一次性交付',
  handler: '李四',
  totalAmount: 180000,
  remarks: '季度销售合同',
  goods: [
    {
      name: '产品C',
      specification: '高端型',
      quantity: 80,
      price: 1500
    }
  ]
}; 