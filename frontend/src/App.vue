<script setup lang="ts">
import { computed, ref } from 'vue';
import Board from './components/Board.vue';
import Controller from './components/Controller.vue';

const board = ref<('X' | boolean)[][]>([
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

const rowConstraints = ref<number[][]>([[1], [1], [1], [1], [1]]);
const setRowConstraint = (index: number, constraint: string) => {
  const parts = constraint
    .trim()
    .split(' ')
    .map((part) => parseInt(part));
  for (const part of parts) {
    if (isNaN(part)) {
      setTimeout(() => alert('Invalid row constraint!'));
      return;
    }
  }
  rowConstraints.value[index] = parts;
};

const colConstraints = ref<number[][]>([[1], [1], [1], [1], [1]]);
const setColConstraint = (index: number, constraint: string) => {
  const parts = constraint
    .trim()
    .split(' ')
    .map((part) => parseInt(part));
  for (const part of parts) {
    if (isNaN(part)) {
      setTimeout(() => alert('Invalid column constraint!'));
      return;
    }
  }
  colConstraints.value[index] = parts;
};

const height = computed(() => board.value.length);
const setHeight = (height: number) => {
  if (height <= 0) {
    setTimeout(() => alert('Height must be greater than 0'));
    return;
  }
  if (height <= board.value.length) {
    board.value = board.value.slice(0, height);
    rowConstraints.value = rowConstraints.value.slice(0, height);
  } else {
    board.value = [
      ...board.value,
      ...Array.from({ length: height - board.value.length }, () =>
        Array.from({ length: board.value[0].length }, () => false),
      ),
    ];
    rowConstraints.value = [
      ...rowConstraints.value,
      ...Array.from({ length: height - rowConstraints.value.length }, () => [
        1,
      ]),
    ];
  }
};

const width = computed(() => board.value[0].length);
const setWidth = (width: number) => {
  if (width <= 0) {
    setTimeout(() => alert('Width must be greater than 0'));
    return;
  }
  if (width <= board.value[0].length) {
    board.value = board.value.map((row) => row.slice(0, width));
    colConstraints.value = colConstraints.value.slice(0, width);
  } else {
    board.value = board.value.map((row) => [
      ...row,
      ...Array.from({ length: width - row.length }, () => false),
    ]);
    colConstraints.value = [
      ...colConstraints.value,
      ...Array.from({ length: width - colConstraints.value.length }, () => [1]),
    ];
  }
};

type Step = {
  isRow: boolean;
  index: number;
  board: ('X' | boolean)[][];
};

const solution = ref<Step[] | null>(null);

const getSolution = () => {
  fetch(import.meta.env.VITE_BACKEND_URL + '/solve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      board: board.value.map((row) =>
        row.map((cell) => (cell === 'X' ? true : false)),
      ),
    }),
  })
    .then((res) => res.json())
    .then((res) => {
      console.log(res);
    });
};
</script>

<template>
  <div class="app">
    <Board
      :board
      :toggleCell
      :rowConstraints
      :colConstraints
      :setRowConstraint
      :setColConstraint
    />
    <Controller :height :setHeight :width :setWidth :getSolution />
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
