<script setup lang="ts" name="father">
import Child1 from "@/views/day19_mitt_child1.vue";
import Child2 from "@/views/day19_mitt_child2.vue";
import {onUnmounted, ref} from "vue";
import emitter from "@/utils/emitter";

let toy = ref();
// 给 emitter 绑定事件
emitter.on("send-toy",(value)=>{
    toy.value = value;
})
// 在组件卸载时解绑 send-toy 事件
onUnmounted(()=>{
    emitter.off("send-toy");
})
</script>

<template>
    <div class="father">
        <h3>父组件</h3>
        <h5 v-show="toy ">哥哥给的玩具：{{ toy }}</h5>
        <Child1/>
        <Child2/>
    </div>
</template>

<style scoped>
.father {
    background-color: lightgreen;
    padding: 20px;
    border-radius: 10px;
}

.father button {
    margin: 10px auto 20px;
}
</style>
