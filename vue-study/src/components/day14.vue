<script setup lang="ts">

import {useRoute} from "vue-router";
import {toRefs} from "vue";

let route = useRoute()
let {params} = toRefs(route)
</script>

<template>
    <ul>
        <li>编号：{{ params.id }}</li>
        <li>标题：{{ params.title }}</li>
        <li>内容：{{ route.params.content }}</li>
    </ul>
</template>

<style scoped>
ul li {
    list-style-type: none;
    padding: 0;
    text-align: left;
}
</style>
