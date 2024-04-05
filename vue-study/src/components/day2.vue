<script setup lang="ts" name="day2">
import {ref, computed} from "vue";

let firstName = ref('张');
let lastName = ref('三');

// 计算属性 这种写法只可读不可写
let fullName = computed(() => {
    return firstName.value.slice(0, 1).toUpperCase() + firstName.value.slice(1) + "_" + lastName.value
})

// 计算属性 这种写法可读可写
let fullName2 = computed({
    get() {
        return firstName.value.slice(0, 1).toUpperCase() + firstName.value.slice(1) + "_" + lastName.value
    },
    set(val) {
        const [str1, str2] = val.split('-');
        firstName.value = str1;
        lastName.value = str2;
    }
})


function changeFullName() {
    fullName2.value = "li-si";
}
</script>

<template>
    <div id="day2">
        姓：<input type="text" v-model="firstName"/><br/>
        名：<input type="text" v-model="lastName"/><br/>
        <button @click="changeFullName">将全名改为li-si</button>
        全名：<span>{{ fullName }}</span><br/>
    </div>
</template>

<style scoped>
#day2 {
    background-color: #4b3d25;
}
</style>
