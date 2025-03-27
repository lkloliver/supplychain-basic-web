<template>
  <div class="ledger-upload">
    <!-- 进度条 -->
    <div class="progress-bar">
      <div class="progress-steps">
        <div 
          v-for="(step, index) in steps" 
          :key="index"
          :class="['step', { active: currentStep >= index }]"
          @click="goToStep(index)"
        >
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-label">{{ step }}</div>
        </div>
      </div>
      </div>
      
    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 采购合同选择 -->
      <div v-if="currentStep === 0" class="step-content">
        <h2 class="step-title">选择采购合同</h2>
        
        <!-- 搜索选择区 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">采购合同</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="contractSearchKey"
                  placeholder="搜索合同编号、名称"
                  class="search-input"
                  @keyup.enter="handleSearch('contract')"
                  @input="handleSearchInput('contract')"
                />
                <i 
                  v-if="contractSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('contract')"
                >
                  ✕
                </i>
          </div>
          </div>
      </div>
      
          <!-- 搜索结果表格 -->
          <div class="search-results" v-if="filteredContracts.length > 0">
            <table class="data-table">
              <thead>
                <tr>
                  <th @click="sortBy('contract', 'contractNo')">
                    合同编号
                    <span class="sort-icon">{{ getSortIcon('contract', 'contractNo') }}</span>
                  </th>
                  <th @click="sortBy('contract', 'contractName')">
                    合同名称
                    <span class="sort-icon">{{ getSortIcon('contract', 'contractName') }}</span>
                  </th>
                  <th @click="sortBy('contract', 'buyer')">
                    采购方
                    <span class="sort-icon">{{ getSortIcon('contract', 'buyer') }}</span>
                  </th>
                  <th @click="sortBy('contract', 'seller')">
                    销售方
                    <span class="sort-icon">{{ getSortIcon('contract', 'seller') }}</span>
                  </th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr 
                  v-for="contract in paginatedContracts" 
                  :key="contract.id"
                  :class="{ selected: selectedContract?.id === contract.id }"
                >
                  <td v-html="highlightText(contract.contractNo, contractSearchKey)"></td>
                  <td v-html="highlightText(contract.contractName, contractSearchKey)"></td>
                  <td v-html="highlightText(contract.buyer, contractSearchKey)"></td>
                  <td v-html="highlightText(contract.seller, contractSearchKey)"></td>
                  <td>
                    <button 
                      class="action-btn primary"
                      @click="selectContract(contract)"
                    >
                      选择
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- 分页 -->
            <div class="pagination">
              <button 
                class="page-btn"
                :disabled="currentPage === 1"
                @click="currentPage--"
              >
                上一页
              </button>
              <span class="page-info">
                第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
              </span>
              <button 
                class="page-btn"
                :disabled="currentPage === totalPages"
                @click="currentPage++"
              >
                下一页
              </button>
            </div>
          </div>
            </div>
          </div>
          
      <!-- 运输信息选择 -->
      <div v-if="currentStep === 1" class="step-content">
        <h2 class="step-title">选择运输信息</h2>
        
        <!-- 搜索选择区 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">过磅单</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="weightSearchKey"
                  placeholder="搜索过磅单"
                  class="search-input"
                  @keyup.enter="handleSearch('weight')"
                  @input="handleSearchInput('weight')"
                />
                <i 
                  v-if="weightSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('weight')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 过磅单列表 -->
          <div class="list-container">
            <div 
              v-for="weight in filteredWeightRecords" 
              :key="weight.id"
              :class="['list-item', { selected: selectedWeight?.id === weight.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">过磅单名称：</span>
                  <span class="value">{{ weight.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">计量号：</span>
                  <span class="value">{{ weight.measurementNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectWeight(weight)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="section">
          <div class="section-header">
            <h3 class="section-title">质检单</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="qualitySearchKey"
                  placeholder="搜索质检单"
                  class="search-input"
                  @keyup.enter="handleSearch('quality')"
                  @input="handleSearchInput('quality')"
                />
                <i 
                  v-if="qualitySearchKey" 
                  class="clear-icon"
                  @click="clearSearch('quality')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 质检单列表 -->
          <div class="list-container">
            <div 
              v-for="quality in filteredQualityRecords" 
              :key="quality.id"
              :class="['list-item', { selected: selectedQuality?.id === quality.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">质检单名称：</span>
                  <span class="value">{{ quality.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">计量号：</span>
                  <span class="value">{{ quality.measurementNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectQuality(quality)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="section">
          <div class="section-header">
            <h3 class="section-title">运单</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="waybillSearchKey"
                  placeholder="搜索运单"
                  class="search-input"
                  @keyup.enter="handleSearch('waybill')"
                  @input="handleSearchInput('waybill')"
                />
                <i 
                  v-if="waybillSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('waybill')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 运单列表 -->
          <div class="list-container">
            <div 
              v-for="waybill in filteredWaybills" 
              :key="waybill.id"
              :class="['list-item', { selected: selectedWaybill?.id === waybill.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">运单名称：</span>
                  <span class="value">{{ waybill.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">运输方式：</span>
                  <span class="value">{{ waybill.transportMethod }}</span>
                </div>
                <div class="item-row">
                  <span class="label">起始地：</span>
                  <span class="value">{{ waybill.origin }}</span>
                </div>
                <div class="item-row">
                  <span class="label">目的地：</span>
                  <span class="value">{{ waybill.destination }}</span>
                </div>
                <div class="item-row">
                  <span class="label">车牌号：</span>
                  <span class="value">{{ waybill.vehicleNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectWaybill(waybill)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 采购结算单选择 -->
      <div v-if="currentStep === 2" class="step-content">
        <h2 class="step-title">选择采购结算单</h2>
        
        <!-- 搜索选择区 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">采购结算单</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="settlementSearchKey"
                  placeholder="搜索采购结算单"
                  class="search-input"
                  @keyup.enter="handleSearch('settlement')"
                  @input="handleSearchInput('settlement')"
                />
                <i 
                  v-if="settlementSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('settlement')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 采购结算单列表 -->
          <div class="list-container">
            <div 
              v-for="settlement in filteredSettlements" 
              :key="settlement.id"
              :class="['list-item', { selected: selectedSettlement?.id === settlement.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">采购结算单名称：</span>
                  <span class="value">{{ settlement.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购方：</span>
                  <span class="value">{{ settlement.buyer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">销售方：</span>
                  <span class="value">{{ settlement.seller }}</span>
                </div>
                <div class="item-row">
                  <span class="label">结算金额：</span>
                  <span class="value">{{ settlement.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectSettlement(settlement)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 发票选择 -->
      <div v-if="currentStep === 3" class="step-content">
        <h2 class="step-title">选择发票</h2>
        
        <!-- 采购发票列表 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">采购发票</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="purchaseInvoiceSearchKey"
                  placeholder="搜索采购发票"
                  class="search-input"
                  @keyup.enter="handleSearch('purchaseInvoice')"
                  @input="handleSearchInput('purchaseInvoice')"
                />
                <i 
                  v-if="purchaseInvoiceSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('purchaseInvoice')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 采购发票列表 -->
          <div class="list-container">
            <div 
              v-for="invoice in filteredPurchaseInvoices" 
              :key="invoice.id"
              :class="['list-item', { selected: selectedPurchaseInvoice?.id === invoice.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">采购发票名称：</span>
                  <span class="value">{{ invoice.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购方：</span>
                  <span class="value">{{ invoice.buyer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">销售方：</span>
                  <span class="value">{{ invoice.seller }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ invoice.amount }}</span>
                </div>
                <div class="item-row">
                  <span class="label">税率：</span>
                  <span class="value">{{ invoice.taxRate }}</span>
                </div>
                <div class="item-row">
                  <span class="label">税额：</span>
                  <span class="value">{{ invoice.taxAmount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectPurchaseInvoice(invoice)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 运输发票列表 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">运输发票</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="transportInvoiceSearchKey"
                  placeholder="搜索运输发票"
                  class="search-input"
                  @keyup.enter="handleSearch('transportInvoice')"
                  @input="handleSearchInput('transportInvoice')"
                />
                <i 
                  v-if="transportInvoiceSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('transportInvoice')"
                >
                  ✕
                </i>
              </div>
            </div>
          </div>
          <!-- 运输发票列表 -->
          <div class="list-container">
            <div 
              v-for="invoice in filteredTransportInvoices" 
              :key="invoice.id"
              :class="['list-item', { selected: selectedTransportInvoice?.id === invoice.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">运输发票名称：</span>
                  <span class="value">{{ invoice.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">托运方：</span>
                  <span class="value">{{ invoice.shipper }}</span>
                </div>
                <div class="item-row">
                  <span class="label">承运方：</span>
                  <span class="value">{{ invoice.carrier }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ invoice.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectTransportInvoice(invoice)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 转账记录选择 -->
      <div v-if="currentStep === 4" class="step-content">
        <h2 class="step-title">选择转账记录</h2>
        
        <!-- 搜索选择区 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">转账记录</h3>
            <div class="search-box">
              <div class="search-input-wrapper">
                <i class="search-icon">🔍</i>
                <input 
                  type="text" 
                  v-model="transferRecordSearchKey"
                  placeholder="搜索转账记录"
                  class="search-input"
                  @keyup.enter="handleSearch('transferRecord')"
                  @input="handleSearchInput('transferRecord')"
                />
                <i 
                  v-if="transferRecordSearchKey" 
                  class="clear-icon"
                  @click="clearSearch('transferRecord')"
                >
                  ✕
                </i>
              </div>
              </div>
              </div>
          <!-- 转账记录列表 -->
          <div class="list-container">
            <div 
              v-for="record in filteredTransferRecords" 
              :key="record.id"
              :class="['list-item', { selected: selectedTransferRecord?.id === record.id }]"
            >
              <div class="item-content">
                <div class="item-row">
                  <span class="label">转账记录名称：</span>
                  <span class="value">{{ record.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">付款方：</span>
                  <span class="value">{{ record.payer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">收款方：</span>
                  <span class="value">{{ record.payee }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ record.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn primary"
                  @click="selectTransferRecord(record)"
                >
                  选择
                </button>
              </div>
            </div>
          </div>
            </div>
          </div>
          
      <!-- 已选信息展示区 -->
      <div class="selected-info" v-if="hasSelectedItems">
        <div class="selected-info-header" @click="toggleSelectedInfo">
          <h3 class="section-title">已选信息</h3>
          <i class="toggle-icon">{{ isSelectedInfoExpanded ? '▼' : '▶' }}</i>
        </div>
        <div class="selected-info-content" v-show="isSelectedInfoExpanded">
          <div class="list-container">
            <!-- 采购合同 -->
            <div v-if="selectedContract" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">采购合同编号：</span>
                  <span class="value">{{ selectedContract.contractNo }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购合同名称：</span>
                  <span class="value">{{ selectedContract.contractName }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购方：</span>
                  <span class="value">{{ selectedContract.buyer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">销售方：</span>
                  <span class="value">{{ selectedContract.seller }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectContract(null)"
                >
                  取消选择
            </button>
          </div>
        </div>

            <!-- 运输信息 -->
            <div v-if="selectedWeight" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">过磅单名称：</span>
                  <span class="value">{{ selectedWeight.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">计量号：</span>
                  <span class="value">{{ selectedWeight.measurementNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectWeight(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <div v-if="selectedQuality" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">质检单名称：</span>
                  <span class="value">{{ selectedQuality.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">计量号：</span>
                  <span class="value">{{ selectedQuality.measurementNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectQuality(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <div v-if="selectedWaybill" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">运单名称：</span>
                  <span class="value">{{ selectedWaybill.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">运输方式：</span>
                  <span class="value">{{ selectedWaybill.transportMethod }}</span>
                </div>
                <div class="item-row">
                  <span class="label">起始地：</span>
                  <span class="value">{{ selectedWaybill.origin }}</span>
                </div>
                <div class="item-row">
                  <span class="label">目的地：</span>
                  <span class="value">{{ selectedWaybill.destination }}</span>
                </div>
                <div class="item-row">
                  <span class="label">车牌号：</span>
                  <span class="value">{{ selectedWaybill.vehicleNo }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectWaybill(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <!-- 采购结算单 -->
            <div v-if="selectedSettlement" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">采购结算单名称：</span>
                  <span class="value">{{ selectedSettlement.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购方：</span>
                  <span class="value">{{ selectedSettlement.buyer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">销售方：</span>
                  <span class="value">{{ selectedSettlement.seller }}</span>
                </div>
                <div class="item-row">
                  <span class="label">结算金额：</span>
                  <span class="value">{{ selectedSettlement.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectSettlement(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <!-- 发票 -->
            <div v-if="selectedPurchaseInvoice" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">采购发票名称：</span>
                  <span class="value">{{ selectedPurchaseInvoice.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">采购方：</span>
                  <span class="value">{{ selectedPurchaseInvoice.buyer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">销售方：</span>
                  <span class="value">{{ selectedPurchaseInvoice.seller }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ selectedPurchaseInvoice.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectPurchaseInvoice(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <div v-if="selectedTransportInvoice" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">运输发票名称：</span>
                  <span class="value">{{ selectedTransportInvoice.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">托运方：</span>
                  <span class="value">{{ selectedTransportInvoice.shipper }}</span>
                </div>
                <div class="item-row">
                  <span class="label">承运方：</span>
                  <span class="value">{{ selectedTransportInvoice.carrier }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ selectedTransportInvoice.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectTransportInvoice(null)"
                >
                  取消选择
                </button>
              </div>
            </div>

            <!-- 转账记录 -->
            <div v-if="selectedTransferRecord" class="list-item selected">
              <div class="item-content">
                <div class="item-row">
                  <span class="label">转账记录名称：</span>
                  <span class="value">{{ selectedTransferRecord.name }}</span>
                </div>
                <div class="item-row">
                  <span class="label">付款方：</span>
                  <span class="value">{{ selectedTransferRecord.payer }}</span>
                </div>
                <div class="item-row">
                  <span class="label">收款方：</span>
                  <span class="value">{{ selectedTransferRecord.payee }}</span>
                </div>
                <div class="item-row">
                  <span class="label">金额：</span>
                  <span class="value">{{ selectedTransferRecord.amount }}</span>
                </div>
              </div>
              <div class="item-actions">
                <button 
                  class="action-btn secondary"
                  @click="selectTransferRecord(null)"
                >
                  取消选择
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="bottom-actions">
      <button 
        v-if="currentStep > 0" 
        class="action-btn secondary" 
        @click="prevStep"
      >
        上一步
      </button>
      <button 
        v-if="currentStep < steps.length - 1" 
        class="action-btn primary" 
        @click="nextStep"
      >
        下一步
      </button>
      <button 
        v-if="currentStep === steps.length - 1" 
        class="action-btn primary" 
        @click="handleSubmit"
      >
        提交
      </button>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import '@/assets/styles/form.css';
import '@/assets/styles/ledger.css';

// 步骤定义
const steps = [
  '选择采购合同',
  '选择运输信息',
  '选择采购结算单',
  '选择发票',
  '选择转账记录'
];

// 当前步骤
const currentStep = ref(0);

// 选择的数据
const selectedContract = ref<any>(null);
const selectedWeight = ref<any>(null);
const selectedQuality = ref<any>(null);
const selectedWaybill = ref<any>(null);
const selectedSettlement = ref<any>(null);
const selectedPurchaseInvoice = ref<any>(null);
const selectedTransportInvoice = ref<any>(null);
const selectedTransferRecord = ref<any>(null);

// 模拟数据
const purchaseContracts = ref([
  {
    id: 1,
    contractNo: 'PC-2024-001',
    contractName: '2024年第一季度采购合同',
    buyer: '公司A',
    seller: '供应商B'
  }
]);

const weightRecords = ref([
  {
    id: 1,
    name: '过磅单-2024-001',
    measurementNo: 'WB-2024-001'
  }
]);

const qualityRecords = ref([
  {
    id: 1,
    name: '质检单-2024-001',
    measurementNo: 'QZ-2024-001'
  }
]);

const waybills = ref([
  {
    id: 1,
    name: '运单-2024-001',
    transportMethod: '公路运输',
    origin: '北京',
    destination: '上海',
    vehicleNo: '京A12345'
  }
]);

const purchaseSettlements = ref([
  {
    id: 1,
    name: '采购结算单-2024-001',
    buyer: '公司A',
    seller: '供应商B',
    amount: '100000.00'
  }
]);

const purchaseInvoices = ref([
  {
    id: 1,
    name: '采购发票-2024-001',
    buyer: '公司A',
    seller: '供应商B',
    amount: '100000.00',
    taxRate: '13%',
    taxAmount: '13000.00'
  }
]);

const transportInvoices = ref([
  {
    id: 1,
    name: '运输发票-2024-001',
    shipper: '供应商B',
    carrier: '运输公司C',
    amount: '5000.00',
    taxRate: '9%',
    taxAmount: '450.00'
  }
]);

const transferRecords = ref([
  {
    id: 1,
    name: '转账记录-2024-001',
    payer: '公司A',
    payee: '供应商B',
    amount: '100000.00'
  }
]);

// 搜索关键词
const contractSearchKey = ref('');
const weightSearchKey = ref('');
const qualitySearchKey = ref('');
const waybillSearchKey = ref('');
const settlementSearchKey = ref('');
const purchaseInvoiceSearchKey = ref('');
const transportInvoiceSearchKey = ref('');
const transferRecordSearchKey = ref('');

// 下拉框显示状态
const showWeightDropdown = ref(false);
const showQualityDropdown = ref(false);
const showWaybillDropdown = ref(false);
const showSettlementDropdown = ref(false);
const showPurchaseInvoiceDropdown = ref(false);
const showTransportInvoiceDropdown = ref(false);
const showTransferRecordDropdown = ref(false);

// 过滤后的数据
const filteredContracts = computed(() => {
  if (!contractSearchKey.value) return purchaseContracts.value;
  const key = contractSearchKey.value.toLowerCase();
  return purchaseContracts.value.filter(contract => 
    contract.contractNo.toLowerCase().includes(key) ||
    contract.contractName.toLowerCase().includes(key) ||
    contract.buyer.toLowerCase().includes(key) ||
    contract.seller.toLowerCase().includes(key)
  );
});

const filteredWeightRecords = computed(() => {
  if (!weightSearchKey.value) return weightRecords.value;
  const key = weightSearchKey.value.toLowerCase();
  return weightRecords.value.filter(weight => 
    weight.name.toLowerCase().includes(key) ||
    weight.measurementNo.toLowerCase().includes(key)
  );
});

const filteredQualityRecords = computed(() => {
  if (!qualitySearchKey.value) return qualityRecords.value;
  const key = qualitySearchKey.value.toLowerCase();
  return qualityRecords.value.filter(quality => 
    quality.name.toLowerCase().includes(key) ||
    quality.measurementNo.toLowerCase().includes(key)
  );
});

const filteredWaybills = computed(() => {
  if (!waybillSearchKey.value) return waybills.value;
  const key = waybillSearchKey.value.toLowerCase();
  return waybills.value.filter(waybill => 
    waybill.name.toLowerCase().includes(key) ||
    waybill.vehicleNo.toLowerCase().includes(key) ||
    waybill.origin.toLowerCase().includes(key) ||
    waybill.destination.toLowerCase().includes(key)
  );
});

const filteredSettlements = computed(() => {
  if (!settlementSearchKey.value) return purchaseSettlements.value;
  const key = settlementSearchKey.value.toLowerCase();
  return purchaseSettlements.value.filter(settlement => 
    settlement.name.toLowerCase().includes(key) ||
    settlement.buyer.toLowerCase().includes(key) ||
    settlement.seller.toLowerCase().includes(key)
  );
});

const filteredPurchaseInvoices = computed(() => {
  if (!purchaseInvoiceSearchKey.value) return purchaseInvoices.value;
  const key = purchaseInvoiceSearchKey.value.toLowerCase();
  return purchaseInvoices.value.filter(invoice => 
    invoice.name.toLowerCase().includes(key) ||
    invoice.buyer.toLowerCase().includes(key) ||
    invoice.seller.toLowerCase().includes(key)
  );
});

const filteredTransportInvoices = computed(() => {
  if (!transportInvoiceSearchKey.value) return transportInvoices.value;
  const key = transportInvoiceSearchKey.value.toLowerCase();
  return transportInvoices.value.filter(invoice => 
    invoice.name.toLowerCase().includes(key) ||
    invoice.shipper.toLowerCase().includes(key) ||
    invoice.carrier.toLowerCase().includes(key)
  );
});

const filteredTransferRecords = computed(() => {
  if (!transferRecordSearchKey.value) return transferRecords.value;
  const key = transferRecordSearchKey.value.toLowerCase();
  return transferRecords.value.filter(record => 
    record.name.toLowerCase().includes(key) ||
    record.payer.toLowerCase().includes(key) ||
    record.payee.toLowerCase().includes(key)
  );
});

// 选择方法
const selectContract = (contract: any) => {
  selectedContract.value = contract;
};

const selectWeight = (weight: any) => {
  selectedWeight.value = weight;
};

const selectQuality = (quality: any) => {
  selectedQuality.value = quality;
};

const selectWaybill = (waybill: any) => {
  selectedWaybill.value = waybill;
};

const selectSettlement = (settlement: any) => {
  selectedSettlement.value = settlement;
};

const selectPurchaseInvoice = (invoice: any) => {
  selectedPurchaseInvoice.value = invoice;
};

const selectTransportInvoice = (invoice: any) => {
  selectedTransportInvoice.value = invoice;
};

const selectTransferRecord = (record: any) => {
  selectedTransferRecord.value = record;
};

// 步骤导航
const goToStep = (index: number) => {
  if (index <= currentStep.value) {
    currentStep.value = index;
    scrollToTop();
  }
};

const nextStep = () => {
  if (currentStep.value < steps.length - 1) {
    currentStep.value++;
    scrollToTop();
  }
};

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--;
    scrollToTop();
  }
};

// 提交
const handleSubmit = async () => {
  try {
    // TODO: 调用API保存台账信息
    console.log('提交台账信息：', {
      contract: selectedContract.value,
      weight: selectedWeight.value,
      quality: selectedQuality.value,
      waybill: selectedWaybill.value,
      settlement: selectedSettlement.value,
      purchaseInvoice: selectedPurchaseInvoice.value,
      transportInvoice: selectedTransportInvoice.value,
      transferRecord: selectedTransferRecord.value
    });
  } catch (error) {
    console.error('提交台账信息失败：', error);
  }
};

// 搜索方法
const handleSearch = (type: string) => {
  isSearching.value = true;
  currentPage.value = 1; // 重置页码
  
  // 模拟搜索延迟
  setTimeout(() => {
    isSearching.value = false;
  }, 500);
};

// 搜索输入处理（防抖）
const handleSearchInput = (type: string) => {
  currentPage.value = 1; // 重置页码
  // 可以在这里添加防抖处理
};

// 清除搜索
const clearSearch = (type: string) => {
  switch (type) {
    case 'contract':
      contractSearchKey.value = '';
      break;
    case 'weight':
      weightSearchKey.value = '';
      break;
    case 'quality':
      qualitySearchKey.value = '';
      break;
    case 'waybill':
      waybillSearchKey.value = '';
      break;
    case 'settlement':
      settlementSearchKey.value = '';
      break;
    case 'purchaseInvoice':
      purchaseInvoiceSearchKey.value = '';
      break;
    case 'transportInvoice':
      transportInvoiceSearchKey.value = '';
      break;
    case 'transferRecord':
      transferRecordSearchKey.value = '';
      break;
  }
  currentPage.value = 1;
};

// 点击外部关闭下拉框
const closeDropdowns = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest('.search-box')) {
    showWeightDropdown.value = false;
    showQualityDropdown.value = false;
    showWaybillDropdown.value = false;
    showSettlementDropdown.value = false;
    showPurchaseInvoiceDropdown.value = false;
    showTransportInvoiceDropdown.value = false;
    showTransferRecordDropdown.value = false;
  }
};

// 监听点击事件
onMounted(() => {
  document.addEventListener('click', closeDropdowns);
});

onUnmounted(() => {
  document.removeEventListener('click', closeDropdowns);
});

// 搜索状态
const isSearching = ref(false);
const currentPage = ref(1);
const pageSize = 10;

// 排序状态
const sortConfig = ref({
  contract: { key: '', direction: 'asc' },
  weight: { key: '', direction: 'asc' },
  quality: { key: '', direction: 'asc' },
  waybill: { key: '', direction: 'asc' },
  settlement: { key: '', direction: 'asc' },
  purchaseInvoice: { key: '', direction: 'asc' },
  transportInvoice: { key: '', direction: 'asc' },
  transferRecord: { key: '', direction: 'asc' }
});

// 分页计算
const paginatedContracts = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return filteredContracts.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(filteredContracts.value.length / pageSize);
});

// 排序方法
const sortBy = (type: string, key: string) => {
  const config = sortConfig.value[type];
  if (config.key === key) {
    config.direction = config.direction === 'asc' ? 'desc' : 'asc';
  } else {
    config.key = key;
    config.direction = 'asc';
  }
};

const getSortIcon = (type: string, key: string) => {
  const config = sortConfig.value[type];
  if (config.key !== key) return '↕';
  return config.direction === 'asc' ? '↑' : '↓';
};

// 高亮匹配文本
const highlightText = (text: string, searchKey: string) => {
  if (!searchKey) return text;
  const regex = new RegExp(`(${searchKey})`, 'gi');
  return text.replace(regex, '<mark>$1</mark>');
};

// 计算是否有已选项目
const hasSelectedItems = computed(() => {
  return selectedContract.value || 
         selectedWeight.value || 
         selectedQuality.value || 
         selectedWaybill.value || 
         selectedSettlement.value || 
         selectedPurchaseInvoice.value || 
         selectedTransportInvoice.value || 
         selectedTransferRecord.value;
});

// 已选信息展开状态
const isSelectedInfoExpanded = ref(false);

// 切换已选信息展开状态
const toggleSelectedInfo = () => {
  isSelectedInfoExpanded.value = !isSelectedInfoExpanded.value;
};

// 滚动到顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
};
</script>

<style scoped>
/* 移除所有样式，因为已经移到 ledger.css 中 */
  </style>
  
  