<!--
watch
作用：监视数据的变化
特点：只能监视以下四种数据
    1. ref 定义的数据
    2. reactive 定义的数据
    3. 函数返回一个值（getter 函数）
    4. 一个包含上述内容的数组
-->
<script lang="ts" setup name="day3">

import {reactive, ref, watch} from "vue";

// 情况一：监视【ref】定义的【基本类型】数据
// ref 定义基本类型数据
let sum = ref(0);

function changeSum() {
    sum.value += 1;
}
// watch 监视基本类型数据
const stopWatch = watch(sum,(newValue,oldValue)=>{
    console.log('sum 变化了',oldValue,newValue);
    if (newValue >= 10) {
        // 停止监视
        stopWatch();
    }
})

// 情况二：监视【ref】定义的【对象类型】数据
// ref 定义对象类型的数据
let person = ref({
    name:'张三',
    age:18
})

function changeName() {
    person.value.name += "*";
}
function changeAge() {
    person.value.age += 1
}

function changePerson() {
    person.value={name: '李四', age: 40}
}

/*
    监视【ref】定义的【对象类型】，监视的是对象的地址值
    若想监视对象内部属性的变化，需要手动开启深度监视
 */
watch(person, (newValue, oldValue) => {
    console.log("person 变化了",oldValue,newValue)
},{deep:true});

// 情况三：监视 reactive 定义的【对象类型】数据
let teacher = reactive({
    name:'张老师',
    age:35
})
function changeTeacherName() {
    teacher.name += "#";
}
function changeTeacherAge() {
    teacher.age += 1;
}
function changeTeacher() {
    Object.assign(teacher,{name:'李老师',age:40})
}

/*
    监视 情况三【reactive】定义的【对象类型】，且默认是开启深度监视的
    且深度监视无法关闭
 */
watch(teacher, (newValue, oldValue) => {
    console.log("teacher 变化了",oldValue,newValue)
});
</script>
<template>
    <div class="day3">
        <h1>情况一：监视【ref】定义的【基本类型】数据</h1>
        <h2>当前求和为：{{sum}}</h2>
        <button @click="changeSum">加一</button>

        <h1>情况二：监视【ref】定义的【对象类型】数据</h1>
        <h2>姓名：{{person.name}}</h2>
        <h2>年龄：{{person.age}}</h2>
        <button @click="changeName">修改名字</button>
        <button @click="changeAge">修改年龄</button>
        <button @click="changePerson">换个人 </button>

        <h1>情况三：监视 reactive 定义的【对象类型】数据</h1>
        <h2>老师姓名：{{teacher.name}}</h2>
        <h2>老师年龄：{{teacher.age}}</h2>
        <button @click="changeTeacherName">修改老师名字</button>
        <button @click="changeTeacherAge">修改老师年龄</button>
        <button @click="changeTeacher">换个老师人 </button>
    </div>
</template>

<style scoped>

</style>
