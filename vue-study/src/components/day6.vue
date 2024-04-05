<script setup lang="ts" name="day6">
import {type PersonInter,type Persons} from "@/types/day6";

let person:PersonInter = {id: "1sdf", name: "张三", age: 30}

let personList: Persons = [
    {id: "1sdf1", name: "张三", age: 30},
    {id: "1sdf2", name: "李三", age: 50},
    {id: "1sdf3", name: "王三", age: 10}

];
</script>

<template>
    <div class="day6">
        <h2>{{person.name}}</h2>
    </div>
</template>

<style scoped>

</style>
