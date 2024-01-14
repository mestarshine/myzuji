<script setup lang="ts">

</script>

<template>
    <div class="day11">
        <h2>路由器工作模式</h2>
        <p>1. history 模式</p>>
        <p>vue2 : mode:'history'</p>
        <p>vue3 : history:createWebHistory()</p>
        <p>
            优点：URL 更加美观，不带有#，更接近传统的网站URL
            缺点：后期项目上线，需要服务端配合处理路径问题，否则刷新会有404错误
        </p>
        <br/>
        <p>2. hash 模式</p>
        <p>vue3 : history:createHashHistory()</p>
        <p>
            优点：兼容性好，因为不需要服务器端处理路径
            缺点：URL 带有 # 不太美观，且在 SEO 优化方面相对较差
        </p>
    </div>
</template>

<style scoped>

</style>
