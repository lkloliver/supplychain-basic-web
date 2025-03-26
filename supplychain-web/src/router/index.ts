import { createRouter, createWebHistory } from "vue-router"
import { useUserStore } from "@/stores/user"

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/auth/LoginRegister.vue"),
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: () => import("@/views/dashboard/DashboardLayout.vue"),
    meta: { requiresAuth: true },
    children: [
      {
        path: "",
        name: "DashboardHome",
        component: () => import("@/views/dashboard/DashboardHome.vue"),
      },
      // 合同模块
      {
        path: "contract",
        children: [
          {
            path: "purchase",
            name: "PurchaseContract",
            component: () => import("@/views/modules/contract/PurchaseContract.vue"),
            redirect: { name: "PurchaseContractUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseContractUpload",
                component: () => import("@/views/modules/contract/PurchaseContractUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseContractManage",
                component: () => import("@/views/modules/contract/PurchaseContractManage.vue"),
              },
            ],
          },
          {
            path: "sales",
            name: "SalesContract",
            component: () => import("@/views/modules/contract/SalesContract.vue"),
            redirect: { name: "SalesContractUpload" },
            children: [
              {
                path: "upload",
                name: "SalesContractUpload",
                component: () => import("@/views/modules/contract/SalesContractUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesContractManage",
                component: () => import("@/views/modules/contract/SalesContractManage.vue"),
              },
            ],
          },
          {
            path: "transport",
            name: "TransportContract",
            component: () => import("@/views/modules/contract/TransportContract.vue"),
            redirect: { name: "TransportContractUpload" },
            children: [
              {
                path: "upload",
                name: "TransportContractUpload",
                component: () => import("@/views/modules/contract/TransportContractUpload.vue"),
              },
              {
                path: "manage",
                name: "TransportContractManage",
                component: () => import("@/views/modules/contract/TransportContractManage.vue"),
              },
            ],
          },
        ],
      },
      // 货物模块 - 基地版
      {
        path: "goods/base",
        children: [
          {
            path: "inbound-weight",
            name: "InboundWeight",
            component: () => import("@/views/modules/goods/base/InboundWeight.vue"),
            redirect: { name: "InboundWeightUpload" },
            children: [
              {
                path: "upload",
                name: "InboundWeightUpload",
                component: () => import("@/views/modules/goods/base/InboundWeightUpload.vue"),
              },
              {
                path: "manage",
                name: "InboundWeightManage",
                component: () => import("@/views/modules/goods/base/InboundWeightManage.vue"),
              },
            ],
          },
          {
            path: "inbound-quality",
            name: "InboundQuality",
            component: () => import("@/views/modules/goods/base/InboundQuality.vue"),
            redirect: { name: "InboundQualityUpload" },
            children: [
              {
                path: "upload",
                name: "InboundQualityUpload",
                component: () => import("@/views/modules/goods/base/InboundQualityUpload.vue"),
              },
              {
                path: "manage",
                name: "InboundQualityManage",
                component: () => import("@/views/modules/goods/base/InboundQualityManage.vue"),
              },
            ],
          },
          {
            path: "inbound-image",
            name: "InboundImage",
            component: () => import("@/views/modules/goods/base/InboundImage.vue"),
            redirect: { name: "InboundImageUpload" },
            children: [
              {
                path: "upload",
                name: "InboundImageUpload",
                component: () => import("@/views/modules/goods/base/InboundImageUpload.vue"),
              },
              {
                path: "manage",
                name: "InboundImageManage",
                component: () => import("@/views/modules/goods/base/InboundImageManage.vue"),
              },
            ],
          },
          {
            path: "outbound-weight",
            name: "OutboundWeight",
            component: () => import("@/views/modules/goods/base/OutboundWeight.vue"),
            redirect: { name: "OutboundWeightUpload" },
            children: [
              {
                path: "upload",
                name: "OutboundWeightUpload",
                component: () => import("@/views/modules/goods/base/OutboundWeightUpload.vue"),
              },
              {
                path: "manage",
                name: "OutboundWeightManage",
                component: () => import("@/views/modules/goods/base/OutboundWeightManage.vue"),
              },
            ],
          },
          {
            path: "outbound-quality",
            name: "OutboundQuality",
            component: () => import("@/views/modules/goods/base/OutboundQuality.vue"),
            redirect: { name: "OutboundQualityUpload" },
            children: [
              {
                path: "upload",
                name: "OutboundQualityUpload",
                component: () => import("@/views/modules/goods/base/OutboundQualityUpload.vue"),
              },
              {
                path: "manage",
                name: "OutboundQualityManage",
                component: () => import("@/views/modules/goods/base/OutboundQualityManage.vue"),
              },
            ],
          },
          {
            path: "outbound-image",
            name: "OutboundImage",
            component: () => import("@/views/modules/goods/base/OutboundImage.vue"),
            redirect: { name: "OutboundImageUpload" },
            children: [
              {
                path: "upload",
                name: "OutboundImageUpload",
                component: () => import("@/views/modules/goods/base/OutboundImageUpload.vue"),
              },
              {
                path: "manage",
                name: "OutboundImageManage",
                component: () => import("@/views/modules/goods/base/OutboundImageManage.vue"),
              },
            ],
          },
          {
            path: "purchase-waybill",
            name: "PurchaseWaybill",
            component: () => import("@/views/modules/goods/base/PurchaseWaybill.vue"),
            redirect: { name: "PurchaseWaybillUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseWaybillUpload",
                component: () => import("@/views/modules/goods/base/PurchaseWaybillUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseWaybillManage",
                component: () => import("@/views/modules/goods/base/PurchaseWaybillManage.vue"),
              },
            ],
          },
          {
            path: "sales-waybill",
            name: "SalesWaybill",
            component: () => import("@/views/modules/goods/base/SalesWaybill.vue"),
            redirect: { name: "SalesWaybillUpload" },
            children: [
              {
                path: "upload",
                name: "SalesWaybillUpload",
                component: () => import("@/views/modules/goods/base/SalesWaybillUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesWaybillManage",
                component: () => import("@/views/modules/goods/base/SalesWaybillManage.vue"),
              },
            ],
          },
        ],
      },
      // 货物模块 - 非基地版
      {
        path: "goods/non-base",
        children: [
          {
            path: "factory-weight",
            name: "FactoryWeight",
            component: () => import("@/views/modules/goods/non-base/FactoryWeight.vue"),
            redirect: { name: "FactoryWeightUpload" },
            children: [
              {
                path: "upload",
                name: "FactoryWeightUpload",
                component: () => import("@/views/modules/goods/non-base/FactoryWeightUpload.vue"),
              },
              {
                path: "manage",
                name: "FactoryWeightManage",
                component: () => import("@/views/modules/goods/non-base/FactoryWeightManage.vue"),
              },
            ],
          },
          {
            path: "factory-quality",
            name: "FactoryQuality",
            component: () => import("@/views/modules/goods/non-base/FactoryQuality.vue"),
            redirect: { name: "FactoryQualityUpload" },
            children: [
              {
                path: "upload",
                name: "FactoryQualityUpload",
                component: () => import("@/views/modules/goods/non-base/FactoryQualityUpload.vue"),
              },
              {
                path: "manage",
                name: "FactoryQualityManage",
                component: () => import("@/views/modules/goods/non-base/FactoryQualityManage.vue"),
              },
            ],
          },
          {
            path: "factory-image",
            name: "FactoryImage",
            component: () => import("@/views/modules/goods/non-base/FactoryImage.vue"),
            redirect: { name: "FactoryImageUpload" },
            children: [
              {
                path: "upload",
                name: "FactoryImageUpload",
                component: () => import("@/views/modules/goods/non-base/FactoryImageUpload.vue"),
              },
              {
                path: "manage",
                name: "FactoryImageManage",
                component: () => import("@/views/modules/goods/non-base/FactoryImageManage.vue"),
              },
            ],
          },
          {
            path: "waybill",
            name: "Waybill",
            component: () => import("@/views/modules/goods/non-base/Waybill.vue"),
            redirect: { name: "WaybillUpload" },
            children: [
              {
                path: "upload",
                name: "WaybillUpload",
                component: () => import("@/views/modules/goods/non-base/WaybillUpload.vue"),
              },
              {
                path: "manage",
                name: "WaybillManage",
                component: () => import("@/views/modules/goods/non-base/WaybillManage.vue"),
              },
            ],
          },
        ],
      },
      // 结算模块
      {
        path: "settlement",
        children: [
          {
            path: "purchase",
            name: "PurchaseSettlement",
            component: () => import("@/views/modules/settlement/PurchaseSettlement.vue"),
            redirect: { name: "PurchaseSettlementUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseSettlementUpload",
                component: () => import("@/views/modules/settlement/PurchaseSettlementUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseSettlementManage",
                component: () => import("@/views/modules/settlement/PurchaseSettlementManage.vue"),
              },
            ],
          },
          {
            path: "sales",
            name: "SalesSettlement",
            component: () => import("@/views/modules/settlement/SalesSettlement.vue"),
            redirect: { name: "SalesSettlementUpload" },
            children: [
              {
                path: "upload",
                name: "SalesSettlementUpload",
                component: () => import("@/views/modules/settlement/SalesSettlementUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesSettlementManage",
                component: () => import("@/views/modules/settlement/SalesSettlementManage.vue"),
              },
            ],
          },
        ],
      },
      // 资金模块
      {
        path: "finance",
        children: [
          {
            path: "purchase-payment",
            name: "PurchasePayment",
            component: () => import("@/views/modules/finance/PurchasePayment.vue"),
            redirect: { name: "PurchasePaymentUpload" },
            children: [
              {
                path: "upload",
                name: "PurchasePaymentUpload",
                component: () => import("@/views/modules/finance/PurchasePaymentUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchasePaymentManage",
                component: () => import("@/views/modules/finance/PurchasePaymentManage.vue"),
              },
            ],
          },
          {
            path: "sales-receipt",
            name: "SalesReceipt",
            component: () => import("@/views/modules/finance/SalesReceipt.vue"),
            redirect: { name: "SalesReceiptUpload" },
            children: [
              {
                path: "upload",
                name: "SalesReceiptUpload",
                component: () => import("@/views/modules/finance/SalesReceiptUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesReceiptManage",
                component: () => import("@/views/modules/finance/SalesReceiptManage.vue"),
              },
            ],
          },
          {
            path: "transport-payment",
            name: "TransportPayment",
            component: () => import("@/views/modules/finance/TransportPayment.vue"),
            redirect: { name: "TransportPaymentUpload" },
            children: [
              {
                path: "upload",
                name: "TransportPaymentUpload",
                component: () => import("@/views/modules/finance/TransportPaymentUpload.vue"),
              },
              {
                path: "manage",
                name: "TransportPaymentManage",
                component: () => import("@/views/modules/finance/TransportPaymentManage.vue"),
              },
            ],
          },
        ],
      },
      // 发票模块
      {
        path: "invoice",
        children: [
          {
            path: "purchase-in",
            name: "PurchaseInvoiceIn",
            component: () => import("@/views/modules/invoice/PurchaseInvoiceIn.vue"),
            redirect: { name: "PurchaseInvoiceInUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseInvoiceInUpload",
                component: () => import("@/views/modules/invoice/PurchaseInvoiceInUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseInvoiceInManage",
                component: () => import("@/views/modules/invoice/PurchaseInvoiceInManage.vue"),
              },
            ],
          },
          {
            path: "purchase-out",
            name: "PurchaseInvoiceOut",
            component: () => import("@/views/modules/invoice/PurchaseInvoiceOut.vue"),
            redirect: { name: "PurchaseInvoiceOutUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseInvoiceOutUpload",
                component: () => import("@/views/modules/invoice/PurchaseInvoiceOutUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseInvoiceOutManage",
                component: () => import("@/views/modules/invoice/PurchaseInvoiceOutManage.vue"),
              },
            ],
          },
          {
            path: "sales",
            name: "SalesInvoice",
            component: () => import("@/views/modules/invoice/SalesInvoice.vue"),
            redirect: { name: "SalesInvoiceUpload" },
            children: [
              {
                path: "upload",
                name: "SalesInvoiceUpload",
                component: () => import("@/views/modules/invoice/SalesInvoiceUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesInvoiceManage",
                component: () => import("@/views/modules/invoice/SalesInvoiceManage.vue"),
              },
            ],
          },
          {
            path: "transport",
            name: "TransportInvoice",
            component: () => import("@/views/modules/invoice/TransportInvoice.vue"),
            redirect: { name: "TransportInvoiceUpload" },
            children: [
              {
                path: "upload",
                name: "TransportInvoiceUpload",
                component: () => import("@/views/modules/invoice/TransportInvoiceUpload.vue"),
              },
              {
                path: "manage",
                name: "TransportInvoiceManage",
                component: () => import("@/views/modules/invoice/TransportInvoiceManage.vue"),
              },
            ],
          },
        ],
      },
      // 台账模块
      {
        path: "ledger",
        children: [
          {
            path: "purchase",
            name: "PurchaseLedger",
            component: () => import("@/views/modules/ledger/PurchaseLedger.vue"),
            redirect: { name: "PurchaseLedgerUpload" },
            children: [
              {
                path: "upload",
                name: "PurchaseLedgerUpload",
                component: () => import("@/views/modules/ledger/PurchaseLedgerUpload.vue"),
              },
              {
                path: "manage",
                name: "PurchaseLedgerManage",
                component: () => import("@/views/modules/ledger/PurchaseLedgerManage.vue"),
              },
            ],
          },
          {
            path: "sales",
            name: "SalesLedger",
            component: () => import("@/views/modules/ledger/SalesLedger.vue"),
            redirect: { name: "SalesLedgerUpload" },
            children: [
              {
                path: "upload",
                name: "SalesLedgerUpload",
                component: () => import("@/views/modules/ledger/SalesLedgerUpload.vue"),
              },
              {
                path: "manage",
                name: "SalesLedgerManage",
                component: () => import("@/views/modules/ledger/SalesLedgerManage.vue"),
              },
            ],
          },
        ],
      },
      // 个人中心
      {
        path: "user-center",
        name: "UserCenter",
        component: () => import("@/views/user/UserCenter.vue"),
        redirect: { name: "BlockchainAccount" },
        children: [
          {
            path: "blockchain-account",
            name: "BlockchainAccount",
            component: () => import("@/views/user/BlockchainAccount.vue"),
          },
          {
            path: "role-permission",
            name: "UserRolePermission",
            component: () => import("@/views/user/UserRolePermission.vue"),
          },
          {
            path: "authentication",
            name: "AuthenticationCenter",
            component: () => import("@/views/user/AuthenticationCenter.vue"),
          },
        ],
      },
      // 访问控制模块
      {
        path: "access",
        meta: { requiresAdmin: true },
        children: [
          {
            path: "user",
            name: "UserManagement",
            component: () => import("@/views/access/UserManagement.vue"),
          },
          {
            path: "department",
            name: "DepartmentManagement",
            component: () => import("@/views/access/DepartmentManagement.vue"),
          },
          {
            path: "role",
            name: "RoleManagement",
            component: () => import("@/views/access/RoleManagement.vue"),
          },
          {
            path: "permission",
            name: "PermissionManagement",
            component: () => import("@/views/access/PermissionManagement.vue"),
          },
        ],
      },
      // 区块链存证模块
      {
        path: "blockchain",
        children: [
          {
            path: "record",
            name: "BlockchainRecord",
            component: () => import("@/views/modules/blockchain/BlockchainRecord.vue"),
          },
          {
            path: "verify",
            name: "BlockchainVerify",
            component: () => import("@/views/modules/blockchain/BlockchainVerify.vue"),
          },
        ],
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 检查路由是否需要认证
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // 如果需要认证但用户未登录，重定向到登录页面
    if (!userStore.isLoggedIn) {
      next({ name: "Login" })
    } else {
      next()
    }
  } else {
    // 如果用户已登录且尝试访问登录页面，重定向到仪表盘
    if (userStore.isLoggedIn && to.name === "Login") {
      next({ name: "DashboardHome" })
    } else {
      next()
    }
  }
})

export default router

