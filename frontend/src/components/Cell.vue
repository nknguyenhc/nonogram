<script setup lang="ts">
import { ref } from 'vue';
import Cross from './Cross.vue';

const props = defineProps<{
  data: string | boolean;
  toggleCell: () => void;
}>();

const cellWidth = ref('50px');
</script>

<template>
  <div
    :class="{ chosen: props.data === true, cell: true }"
    @click="props.toggleCell"
  >
    <Cross v-if="props.data === 'X'" :width="cellWidth" />
  </div>
</template>

<style scoped>
.cell {
  display: inline-block;
  width: v-bind(cellWidth);
  height: v-bind(cellWidth);
  border: 1px solid black;
  text-align: center;
}
.chosen {
  background-color: lightblue;
}
</style>
