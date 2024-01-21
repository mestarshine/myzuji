<script setup lang="ts">
import {useRoute} from "vue-router";
import router from "../router";

let  route = useRoute()
</script>

<template>
    <ul>
        <li>编号：{{route.query.id}}</li>
        <li>标题：{{ route.query.title }}</li>
        <li>内容：{{ route.query.content }}</li>
    </ul>
</template>

<style scoped>
ul li {
    list-style-type: none;
    padding: 0;
    text-align: left;
}
</style>
