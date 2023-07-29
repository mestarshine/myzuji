<script setup lang="ts" name="day2">
import {ref,computed} from "vue";

let firstName =ref( '张');
let lastName = ref('三');

// 计算属性
let fullName = computed(()=>{
    return firstName.value.slice(0,1).toUpperCase() + firstName.value.slice(1)+"_"+lastName.value
})
</script>

<template>
<div id="day2">
    姓：<input type="text" v-model="firstName"/><br/>
    名：<input type="text" v-model="lastName"/><br/>
    全名：<span>{{ fullName}}</span><br/>
</div>
</template>

<style scoped>
#day2{
    background-color: #4b3d25;
}
</style>
