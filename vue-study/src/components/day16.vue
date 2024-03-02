<script setup lang="ts" name="day16">

import {ref} from "vue";

let sum = ref(1); // 当前求和
let n = ref(1); // 用户选择的数字
function add() {
    sum.value += n.value
}

function minus() {
    sum.value-=n.value
}
</script>

<template>
    <div class="count">
        <h2>当前求和为：{{ sum}}</h2>
        <select v-model.number="n">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
        </select>
        <button @click="add">加</button>
        <button @click="minus">减</button>
    </div>
</template>

<style scoped>
.count {
    background-color: skyblue;
    padding: 10px;
    border-radius: 10px;
    box-shadow: 0 0 10px;
}
select,button {
    margin: 0 5px;
    height: 25px;
}
</style>
