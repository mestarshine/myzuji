<script setup lang="ts">
import type {Persons} from "@/types/day6";

//只接收
// defineProps(["list"]);

//接收list + 限制类型
// defineProps<{list:Persons}>()

//接收list + 限制类型 + 限制必要性 + 指定默认值
withDefaults(defineProps<{ list?: Persons }>(), {
    list: () => [{id: "1sdf5", name: "默认值", age: 30}]
})
</script>

<template>
    <div class="day7-1">
        <ul>
            <li v-for="p in list" :key="p.id">
                {{ p.name }} -- {{ p.age }}
            </li>
        </ul>
    </div>
</template>

<style scoped>

</style>
