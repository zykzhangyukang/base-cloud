<template>
  <div class="page-container">
    控制台
  </div>
</template>

<script>

</script>

<style lang="scss" scoped>
.module {
  margin-bottom: 20px;
}

.info-wrap {
  display: flex;
  align-items: center;
  color: #999999;
  font-size: 14px;
  .avatar {
    margin-right: 10px;
  }
  .info {
    .title {
      color: #333333;
      font-size: 18px;
      margin-bottom: 6px;
      font-weight: bold;
    }
    a {
      color: var(--primary-color);
      cursor: pointer;
    }
  }
  .star {
    width: 50px;
    height: 50px;
    padding-left: 20px;
    flex-shrink: 0;
    img {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
