<script setup lang="ts">
/*
    vue2组件的生命周期
                     对应的函数
    创建（前、后）      beforeCreate   created
    挂载（前、后）      beforeMount    mounted
    更新（前、后）      beforeUpdate   updated
    销毁（前、后）      beforeDestroy  destroyed

     vue3组件的生命周期
                     对应的函数
    创建（前、后）      setup
    挂载（前、后）      onBeforeMount    onMounted
    更新（前、后）      onBeforeUpdate   onUpdated
    卸载（前、后）      onBeforeUnmount  onUnmounted
*/
import {onBeforeMount, onBeforeUnmount, onBeforeUpdate, onMounted, onUnmounted, onUpdated} from "vue";
console.log("创建")
//挂载 前
onBeforeMount(() => {
    console.log("子---挂载前")
});
//挂载 完毕
onMounted(() => {
    console.log("子---挂载完毕")
});
// 更新 前
onBeforeUpdate(() => {
    console.log("子---更新前");
});
// 更新 完毕
onUpdated(() => {
    console.log("子---更新完毕")
});
// 卸载 前
onBeforeUnmount(() => {
    console.log("子---卸载前");
});
//卸载 完毕
onUnmounted(()=>{
    console.log("子---卸载完毕");
})

</script>

<template>

</template>

<style scoped>

</style>
