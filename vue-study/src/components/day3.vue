<!--
watch
作用：监视数据的变化
特点：只能监视以下四种数据
    1. ref 定义的数据
    2. reactive 定义的数据
    3. 函数返回一个值（getter 函数）
    4. 一个包含上述内容的数组
-->
<script lang="ts" setup name="day3">

import {ref, watch} from "vue";

// ref 定义基本类型数据
let sum = ref(0);

function changeSum() {
    sum.value += 1;
}
// watch 监视基本类型数据
const stopWatch = watch(sum,(newValue,oldValue)=>{
    console.log('sum 变化了',oldValue,newValue);
    if (newValue >= 10) {
        // 停止监视
        stopWatch();
    }
})
</script>
<template>
    <div class="day3">
        <h1>情况一：监视【ref】定义的【基本类型】数据</h1>
        <h2>当前求和为：{{sum}}</h2>
        <button @click="changeSum">加一</button>
    </div>
</template>

<style scoped>

</style>
