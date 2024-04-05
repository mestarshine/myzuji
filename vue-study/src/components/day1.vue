<script lang="ts" setup name="day1">
import {reactive} from "vue";
import {ref} from "vue";
/*
reactive 与 ref 使用原则
1. 基本类型的响应式数据必须使用ref
2. 层级不深的响应式数据 ref 与 reactive 都可以
3. 层级较深，推荐使用 reactive
4. 表单数据收集 推荐使用 reactive
 */
// 数据
let car = reactive({brand: 'BMW', price: 100})
let moves = ref([
    {id:'abc1',name:'阿Q正传'},
    {id:'abc2',name:'肖恩克的救赎'}
])

function changePrice() {
    car.price += 10
}

function changeCar() {
    // car = {brand: '奥拓',price: 2} //reactive这么写页面不更新
    Object.assign(car,{brand: '奥拓',price: 2})
}

function addMove() {
    moves.value.push({id:'abc3',name:'笑傲江湖'})
}
</script>

<template>
    <div>
        <h1>一辆{{ car.brand }}车，价值{{ car.price }}</h1>
        <button @click="changePrice">修改汽车价格</button>
        <button @click="changeCar">修改汽车</button>
        <h1>电影列表</h1>
        <button @click="addMove">添加一部</button>
        <ul>
            <li v-for="m in moves" :key="m.id">{{m.name}}</li>
        </ul>
    </div>
</template>

<style scoped>

</style>
