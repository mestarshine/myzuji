<script setup lang="ts" name="day20_input">

defineProps(["ming","mima"])
let emit = defineEmits(["update:ming","update:mima"])
</script>

<template>
    <input type="text" :value="ming" @input="emit('update:ming',(<HTMLInputElement>$event.target).value)">
    <hr>
    <input type="text" :value="mima" @input="emit('update:mima',(<HTMLInputElement>$event.target).value)">
</template>

<style scoped>
input {
    border: 2px solid black;
    background-image: linear-gradient(45deg, red, yellow, green);
    height: 30px;
    font-size: 20px;
    color: white;
}
</style>
