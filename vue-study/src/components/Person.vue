<script lang="ts" setup name="Person5">
import {reactive} from "vue";
// 数据
let car = reactive({brand: 'BMW', price: 100})
let moves = reactive([
    {id:'abc1',name:'阿Q正传'},
    {id:'abc2',name:'肖恩克的救赎'}
])

function changePrice() {
    car.price += 10
}

function addMove() {
    moves.push({id:'abc3',name:'笑傲江湖'})
}
</script>

<template>
    <div>
        <h1>一辆{{ car.brand }}车，价值{{ car.price }}</h1>
        <button @click="changePrice">修改汽车价格</button>
        <h1>电影列表</h1>
        <button @click="addMove">添加一部</button>
        <ul>
            <li v-for="m in moves" :key="m.id">{{m.name}}</li>
        </ul>
    </div>
</template>

<style scoped>

</style>
