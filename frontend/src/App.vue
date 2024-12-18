<script setup lang="ts">
import { computed, ref } from 'vue';
import Board from './components/Board.vue';
import Controller from './components/Controller.vue';

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

const height = computed(() => board.value.length);
const setHeight = (height: number) => {
  if (height <= 0) {
    alert('Height must be greater than 0');
    return;
  }
  if (height <= board.value.length) {
    board.value = board.value.slice(0, height);
  } else {
    board.value = [
      ...board.value,
      ...Array.from({ length: height - board.value.length }, () =>
        Array.from({ length: board.value[0].length }, () => false),
      ),
    ];
  }
};

const width = computed(() => board.value[0].length);
const setWidth = (width: number) => {
  if (width <= 0) {
    alert('Width must be greater than 0');
    return;
  }
  if (width <= board.value[0].length) {
    board.value = board.value.map((row) => row.slice(0, width));
  } else {
    board.value = board.value.map((row) => [
      ...row,
      ...Array.from({ length: width - row.length }, () => false),
    ]);
  }
};
</script>

<template>
  <div class="app">
    <Board :board :toggleCell />
    <Controller
      :height="height"
      :setHeight="setHeight"
      :width="width"
      :setWidth="setWidth"
    />
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 40px;
  padding: 40px;
}
</style>
