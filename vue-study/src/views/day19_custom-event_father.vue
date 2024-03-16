<script setup lang="ts" name="father">
import Child from "@/views/day19_custom-event_child.vue";
import {ref} from "vue";

let toy = ref();
// 用于保存子传递过来的玩具
function saveChildToy(childToy:string) {
    toy.value=childToy
}
</script>

<template>
    <div class="father">
        <h3>父组件</h3>
        <h5 v-show="toy ">子给父的玩具：{{ toy }}</h5>
        <!-- 给子组件Child 绑定事件 -->
        <Child @send-toy="saveChildToy"/>
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
