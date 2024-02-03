<script setup lang="ts">

defineProps(['id','title','content'])
</script>

<template>
    <ul>
        <li>编号：{{ id }}</li>
        <li>标题：{{ title }}</li>
        <li>内容：{{ content }}</li>
    </ul>
</template>

<style scoped>
ul li {
    list-style-type: none;
    padding: 0;
    text-align: left;
}
</style>
