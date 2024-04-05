<script setup lang="ts">

import {ref} from "vue";

let toy = ref("奥特曼");
// 声明接收 props
defineProps(["car","sendToy"])

</script>

<template>
<div class="child">
    <h3>子组件</h3>
    <h5>玩具：{{ toy }}</h5>
    <h5>父给的车：{{ car }}</h5>
    <button @click="sendToy(toy)">把玩具给父亲</button>
</div>
</template>

<style scoped>
.child {
    background-color: lightblue;
    padding: 10px;
    box-shadow: 0 0 10px black;
    border-radius: 10px;
}
</style>
