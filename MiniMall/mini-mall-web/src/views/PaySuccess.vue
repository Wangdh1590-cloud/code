<template>
  <div class="pay-success">
    <el-result icon="success" title="支付成功" sub-title="感谢您的购买！">
      <template #extra>
        <el-button type="primary" @click="$router.push(`/orders/${$route.params.id}`)">查看订单</el-button>
        <el-button @click="$router.push('/')">继续购物</el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
.pay-success {
  display: flex;
  justify-content: center;
  padding: 60px 0;
}
</style>
