<script setup lang="ts">
import {useRoute} from "vue-router";
import router from "../router";
import {toRefs} from "vue";

let  route = useRoute()
//利用解构
let {query} = toRefs(route)
</script>

<template>
    <ul>
        <li>编号：{{ query.id }}</li>
        <li>标题：{{ query.title }}</li>
        <li>内容：{{ query.content }}</li>
    </ul>
</template>

<style scoped>
ul li {
    list-style-type: none;
    padding: 0;
    text-align: left;
}
</style>
