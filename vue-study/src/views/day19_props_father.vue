<script setup lang="ts" name="father">
import Child from "@/views/day19_props_child.vue";
import {ref} from "vue";

let car = ref("奔驰");
let toy = ref();
function getToy(value:string) {
    console.log("父", value);
    toy.value=value
}
</script>

<template>
<!--
    props 是使用频率最高的一种通信方式 常用于 父 <===> 子
    若 父传子 属性值是非函数
    若 子传父 属性值是函数
-->
    <div class="father">
        <h3>父组件</h3>
        <h5>汽车：{{ car }}</h5>
        <h5 v-show="toy ">子给父的玩具：{{ toy }}</h5>
        <Child :car="car" :sendToy="getToy"/>
    </div>
</template>

<style scoped>
.father {
    background-color: lightgreen;
    padding: 20px;
    border-radius: 10px;
}
</style>
