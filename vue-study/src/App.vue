<script lang="ts" setup name="App">

</script>

<template>
    <div class="app">
        <h2>路由测试</h2>
        <!-- 导航区 -->
        <div class="navigate">
            <RouterLink to="/home" active-class="active">首页</RouterLink>
            <RouterLink to="/news" active-class="active">新闻</RouterLink>
            <RouterLink to="/about" active-class="active">关于</RouterLink>
        </div>
        <!-- 展示区 -->
        <div class="main-content">
            <RouterView/>
        </div>

    </div>
</template>

<style scoped>
/* 样式 */
.app{
    text-align: center;
    height: 200px; /* 示例高度 */
}
.navigate{
    display: block;
    height: 30px;
    margin: 50px 100px 10px;
}
.navigate a{
    margin: 50px 100px 10px;
}
.main-content{
    border: #222222 solid 1px;
    height: 260px;
}
</style>
