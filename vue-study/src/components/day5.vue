<script setup lang="ts" name="day5">

import {ref} from "vue";

/*
    标签的 【ref】属性
    用于普通【DOM】标签上，获取的是【DOM】节点
    用于组件标签上，获取的是组件实例对象
 */
let test = ref();

function showLog(){
    console.log(test);
}
</script>

<template>
    <div class="day5">
        <h2 ref="test">测试Ref</h2>
        <button @click="showLog">打印内容</button>
    </div>
</template>

<style scoped>

</style>
