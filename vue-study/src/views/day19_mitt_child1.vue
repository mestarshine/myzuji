<script setup lang="ts">

import {ref} from "vue";
import emitter from "@/utils/emitter";

let toy = ref("奥特曼");
let computer = ref();
emitter.on("send-computer",(vl)=>{
    computer.value = vl;
})
</script>

<template>
    <div class="child1">
        <h3>子组件-哥哥</h3>
        <h5>玩具：{{ toy }}</h5>
        <h5 v-show="computer ">弟弟给的电脑：{{ computer }}</h5>
        <button @click="emitter.emit('send-toy',toy)">把玩具给弟弟</button>
    </div>
</template>

<style scoped>
.child1 {
    margin-top: 30px;
    background-color: lightblue;
    padding: 10px;
    box-shadow: 0 0 10px black;
    border-radius: 10px;
}

.child1 button {
    margin: 10px auto 20px;
}
</style>
