<script setup lang="ts" name="father">
import {ref} from "vue";
import Day20_input from "@/views/day20_input.vue";

let username = ref("zhangsan");
let password = ref("123456");
</script>

<template>
<!--
    $event 什么时候能 .target
    对于原生事件，$event 就是事件对象 ===> 能 .target
    对于自定义事件，$event 就是触发事件时，所传递的数据 ===> 不能 .target
-->
    <div class="father">
        <h3>父组件</h3>
        <h5>{{ username }}-{{ password }}</h5>
        <!-- v-model 用在 html 标签上 -->
<!--         <input type="text" v-model="username">-->
        <!-- v-model 用在 组件 标签上 -->
<!--        <day20_input v-model="username"/>-->
<!--        <day20_input :modelValue="username" @update:modelValue="username=$event"></day20_input>-->
        <day20_input v-model:ming="username" v-model:mima="password"/>
    </div>
</template>

<style scoped>
.father {
    background-color: lightgreen;
    padding: 20px;
    border-radius: 10px;
}

.father button {
    margin: 10px auto 20px;
}
</style>
