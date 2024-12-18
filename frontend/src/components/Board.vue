<script setup lang="ts">
import Cell from './Cell.vue';

const props = defineProps<{
  board: (string | boolean)[][];
  toggleCell: (row: number, col: number) => void;
}>();
</script>

<template>
  <div class="board">
    <div class="row" v-for="(row, rowIndex) in props.board" :key="rowIndex">
      <Cell
        v-for="(cell, cellIndex) in row"
        :key="cellIndex"
        :data="cell"
        :toggleCell="() => toggleCell(rowIndex, cellIndex)"
      />
    </div>
  </div>
</template>

<style scoped>
.board {
  display: flex;
  flex-direction: column;
  padding: 60px;
}
.row {
  display: flex;
  flex-direction: row;
}
</style>
