<script setup lang="ts">
// https://dog.ceo/api/breed/pembroke/images/random

import {reactive, ref} from "vue";
import axios from "axios";

let sum = ref(0);
let dogList = reactive(["https://images.dog.ceo/breeds/pembroke/n02113023_1825.jpg"]);

function add(){
    sum.value += 1;
}
async function getDog(){
    try {
        let result = await axios.get("https://dog.ceo/api/breed/pembroke/images/random");
        dogList.push(result.data.message)
    } catch (error){
        alert("请求失败"+ error);
    }
}
</script>

<template>
    <div class="day9">
        <h2>当前求和为：{{sum}}</h2>
        <button @click="add">点我加一</button>
        <br/>
        <button @click="getDog">召唤小狗</button>
        <br/>
        <img v-for="(dog,index) in dogList" :src="dog" :key="index" alt="图片"/>
    </div>
</template>

<style scoped>
img{
    margin: auto 5px;
}
</style>
