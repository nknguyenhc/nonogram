<script setup lang="ts">
import { ref } from 'vue';
import Board from './components/Board.vue';

const board = ref<(string | boolean)[][]>([
  [false, false, false, false, false],
  [false, false, false, false, false],
  [false, false, false, false, false],
  [false, false, false, false, false],
  [false, false, false, false, false],
]);

const toggleCell = (row: number, col: number) => {
  if (board.value[row][col] === false) {
    board.value[row][col] = 'X';
  } else {
    board.value[row][col] = false;
  }
};
</script>

<template>
  <div>
    <Board :board :toggleCell />
  </div>
</template>

<style scoped></style>
