<script setup lang="ts">
import useSum from "@/hooks/useSum";
import useDog from "@/hooks/useDog";

let {sum,add} = useSum()
let {dogList,getDog} = useDog()
</script>

<template>
    <div class="day9">
        <h2>当前求和为：{{sum}}</h2>
        <button @click="add">点我加一</button>
        <br/>
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
