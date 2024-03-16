<script setup lang="ts">

import {onUnmounted, ref} from "vue";
import emitter from "@/utils/emitter";

let computer = ref("小米");

let toy = ref();
// 给 emitter 绑定事件
emitter.on("send-toy",(value)=>{
    toy.value = value;
})
// 在组件卸载时解绑 send-toy 事件
onUnmounted(()=>{
    emitter.off("send-toy");
})
</script>

<template>
<div class="child2">
    <h3>子组件2-弟弟</h3>
    <h5>电脑：{{ computer }}</h5>
    <h5 v-show="toy ">哥哥给的玩具：{{ toy }}</h5>
</div>
</template>

<style scoped>
.child2 {
    background-color: lightpink;
    margin-top: 30px;
    padding: 10px;
    box-shadow: 0 0 10px black;
    border-radius: 10px;
}
</style>
