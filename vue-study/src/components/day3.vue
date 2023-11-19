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

//情况四：监视 一个包含上述内容的数组
let student = reactive({
    name: "张三",
    age: 20,
    car:{
        c1: "特斯拉",
        c2: "宝马",
    }
})

function changeStudentName() {
    student.name += "*";
}
function changeStudentAge() {
    student.age += 1;
}
function changeStudentC1() {
    student.car.c1 ="奥迪"
}
function changeStudentC2() {
    student.car.c2 ="大众"

}
function changeStudentCar() {
    student.car = {c1: "雅迪", c2: "爱玛"};
}
/*
    情况四 监视 【ref、reactive】定义的【对象类型】数据中的【某个属性】
    1. 该属性是基本类型，需要写成函数形式
*/
watch(()=>{return student.name}, (newValue, oldValue) => {
    console.log("student.name 变化了", oldValue, newValue);
});
/*
    情况四 监视 【ref、reactive】定义的【对象类型】数据中的【某个属性】
    2. 该属性是对象类型，可直接写，也能写函数，推荐写成函数形式

    监视 对象的属性，最好写成函数式，若要监视对象内部，需要手动开启深度监视

*/
watch(()=>student.car, (newValue, oldValue) => {
    console.log("student.car 变化了", oldValue, newValue);
},{deep: true});
</script>
<template>
    <div class="day3">
        <h1>情况一：监视【ref】定义的【基本类型】数据</h1>
        <h2>当前求和为：{{sum}}</h2>
        <button @click="changeSum">加一</button>
        <br/>
        <br/>

        <h1>情况二：监视【ref】定义的【对象类型】数据</h1>
        <h2>姓名：{{person.name}}</h2>
        <h2>年龄：{{person.age}}</h2>
        <button @click="changeName">修改名字</button>
        <button @click="changeAge">修改年龄</button>
        <button @click="changePerson">换个人 </button>
        <br/>
        <br/>

        <h1>情况三：监视 reactive 定义的【对象类型】数据</h1>
        <h2>老师姓名：{{teacher.name}}</h2>
        <h2>老师年龄：{{teacher.age}}</h2>
        <button @click="changeTeacherName">修改老师名字</button>
        <button @click="changeTeacherAge">修改老师年龄</button>
        <button @click="changeTeacher">换个老师人 </button>
        <br/>
        <br/>

        <h1>情况四：监视 一个包含上述内容的数组</h1>
        <h2>学生姓名：{{student.name}}</h2>
        <h2>学生年龄：{{student.age}}</h2>
        <h2>汽车：{{student.car.c1}}、{{student.car.c2}}</h2>
        <button @click="changeStudentName">修改学生名字</button>
        <button @click="changeStudentAge">修改学生年龄</button>
        <button @click="changeStudentC1">修改第一台车</button>
        <button @click="changeStudentC2">修改第二台车</button>
        <button @click="changeStudentCar">修改车</button>
    </div>
</template>

<style scoped>

</style>
