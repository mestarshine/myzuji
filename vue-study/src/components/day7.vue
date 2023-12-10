<script setup lang="ts">
import type {Persons} from "@/types/day6";
import {reactive} from "vue";
import Day7_1 from "@/components/day7_1.vue";

let personList = reactive<Persons>([
    {id: "1sdf1", name: "张三", age: 30},
    {id: "1sdf2", name: "李三", age: 50},
    {id: "1sdf3", name: "王三", age: 10}
]);
</script>

<template>

    <Day7_1 :list="personList"/>
</template>

<style scoped>

</style>
