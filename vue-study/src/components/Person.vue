<script lang="ts" setup name="Person5">
import {ref} from "vue";
//数据
let userName = ref("张三"); // 不是响应式的
let age = ref(18);
let tel = '133338888';

//方法
function changeName() {
    userName.value = "李四";
}

function changeAge() {
    age.value += 1;
}

function showTel() {
    alert(tel);
}

</script>

<template>
    <div>
        <h2>姓名：{{ userName }}</h2>
        <h2>年龄：{{ age }}</h2>
        <button @click="changeName">修改名字</button>
        <button @click="changeAge">修改年龄</button>
        <button @click="showTel">查看联系方式</button>
    </div>
</template>

<style scoped>

</style>
