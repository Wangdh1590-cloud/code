<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <h2>后台管理</h2>
      <el-menu :default-active="$route.path" router>
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>仪表盘
        </el-menu-item>
        <el-menu-item index="/admin/products">
          <el-icon><Goods /></el-icon>商品管理
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><List /></el-icon>订单管理
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><Grid /></el-icon>分类管理
        </el-menu-item>
      </el-menu>
      <div class="sidebar-footer">
        <el-button @click="$router.push('/')">返回前台</el-button>
      </div>
    </aside>
    <main class="admin-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.admin-layout { display: flex; min-height: calc(100vh - 140px); }
.admin-sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
}
.admin-sidebar h2 {
  padding: 0 20px 20px;
  font-size: 18px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 10px;
}
.sidebar-footer { margin-top: auto; padding: 20px; }
.admin-content {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  min-width: 0;
}
</style>
