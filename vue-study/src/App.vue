<script lang="ts" setup name="App">
// JS 或 TS
import day from '@/components/day8.vue';
import {onMounted} from "vue";

//挂载 完毕
onMounted(() => {
    console.log("父---挂载完毕")
});
</script>

<template>
    <day/>
</template>

<style>
/* 样式 */
#app { background-color: rgb(92, 166, 185)}
</style>
