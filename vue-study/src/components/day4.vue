<script setup lang="ts" name="day4">
import {ref, watch, watchEffect} from "vue";

let temperature = ref(20);
let amount = ref(1);

function changeTemperature() {
    temperature.value += 3;
}
function changeAmount(){
    amount.value += 1;
}

/*
watch([temperature, amount], (value) => {
    //从value 解构出最新水温与水量
    let [newTemp, newAmount] = value
    console.log("当前水温与水量", newTemp, newAmount);
    if (newTemp >= 60 || newAmount >= 10) {
        console.log("告警：注意水温水量");
    }
});


    watchEffect 会立即运行，同时响应式的追踪其依赖，并在依赖更改时重新执行该函数
    watch 与 watchEffect 的对比
    1. 都能监听响应式数据的变化，不同的是监听数据变化的方式不同
    2. watch 要明确指出监视的数据
    3. watchEffect 不用明确指出监视的数据（函数中用到哪引些属性，就监视哪些属性）
*/
watchEffect(()=>{
    if (temperature.value >= 60 || amount.value >= 10) {
        console.log("告警：注意水温水量");
    }
})
</script>

<template>
    <div class="day4">
        <h1>当水温达到60度时，水量达到10L，发出告警</h1>
        <h2>当前水温：{{temperature}}度</h2>
        <h2>当前水量：{{amount}}L</h2>
        <button @click="changeTemperature">点我加热</button>
        <button @click="changeAmount">点我加水</button>
    </div>
</template>

<style scoped>

</style>
