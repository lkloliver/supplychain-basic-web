// 采购合同模拟数据
export const mockPurchaseContracts = [
  {
    id: 1,
    contractNo: 'CT-2023-001',
    signDate: '2023-05-15',
    buyer: '我方公司',
    seller: '北京供应商A有限公司',
    paymentAccount: '622848203910392832',
    receivingAccount: '622848203910392833',
    taxRate: '13%',
    invoiceType: '增值税专用发票',
    paymentDate: '2023-06-15',
    paymentMethod: '银行转账',
    performancePeriod: '2023-05-15至2023-12-31',
    performanceLocation: '北京市朝阳区某某路123号',
    performanceMethod: '分批交付',
    handler: '张三',
    totalAmount: 125000,
    remarks: '年度采购合同',
    goods: [
      { 
        id: 101, 
        name: '原材料A', 
        specification: '标准型', 
        quantity: 100, 
        price: 800
      },
      { 
        id: 102, 
        name: '原材料B', 
        specification: '高纯度', 
        quantity: 50, 
        price: 900
      }
    ]
  },
  {
    id: 2,
    contractNo: 'CT-2023-002',
    signDate: '2023-05-20',
    buyer: '我方公司',
    seller: '上海供应商B有限公司',
    paymentAccount: '622848203910392834',
    receivingAccount: '622848203910392835',
    taxRate: '13%',
    invoiceType: '增值税专用发票',
    paymentDate: '2023-07-20',
    paymentMethod: '银行转账',
    performancePeriod: '2023-05-20至2023-09-30',
    performanceLocation: '上海市浦东新区某某路456号',
    performanceMethod: '一次性交付',
    handler: '李四',
    totalAmount: 86000,
    remarks: '季度采购合同',
    goods: [
      { 
        id: 201, 
        name: '零部件A', 
        specification: '标准型', 
        quantity: 200, 
        price: 150
      },
      { 
        id: 202, 
        name: '零部件B', 
        specification: '加强型', 
        quantity: 100, 
        price: 200
      },
      { 
        id: 203, 
        name: '零部件C', 
        specification: '特殊型', 
        quantity: 120, 
        price: 300
      }
    ]
  }
];

// 模拟API请求
export const fetchPurchaseContracts = async (page = 1, pageSize = 10) => {
  // 模拟API请求延迟
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  const data = mockPurchaseContracts.slice(start, end);
  
  return {
    data,
    total: mockPurchaseContracts.length,
    page,
    pageSize
  };
};

// 模拟删除操作
export const deletePurchaseContract = async (id: number) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  return { success: true };
}; 