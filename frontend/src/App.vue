<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import Board from './components/Board.vue';
import Controller from './components/Controller.vue';
import Steps from './components/Steps.vue';

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
  if (constraint.trim() === '') {
    rowConstraints.value[index] = [];
    return;
  }
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
  if (constraint.trim() === '') {
    colConstraints.value[index] = [];
    return;
  }
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

export type Step = {
  isRow: boolean;
  index: number;
  board: ('X' | boolean)[][];
};

const solution = ref<Step[] | null>(null);
const stepIndex = ref<number>(0);
const currentStep = computed(() => solution.value?.[stepIndex.value]);

watch(
  () => currentStep.value,
  (newValue) => {
    if (newValue === undefined) {
      board.value = board.value.map((row) => row.map(() => false));
    } else {
      board.value = newValue.board;
    }
  },
);

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
      rowConstraints: rowConstraints.value,
      colConstraints: colConstraints.value,
    }),
  })
    .then((res) => res.json())
    .then((res) => {
      if (!res.success) {
        setTimeout(() => alert(res.error));
        return;
      }
      solution.value = res.steps.map((step: any) => ({
        isRow: step.constraintInfo.isRow,
        index: step.constraintInfo.index,
        board: step.board.map((row: (0 | 1 | 2)[]) =>
          row.map((cell) => (cell === 0 ? false : cell === 1 ? 'X' : true)),
        ),
      }));
    });
};

const incrementStep = () => {
  if (stepIndex.value < solution.value!.length - 1) {
    stepIndex.value++;
  }
};

const decrementStep = () => {
  if (stepIndex.value > 0) {
    stepIndex.value--;
  }
};

const firstStep = () => {
  stepIndex.value = 0;
};

const lastStep = () => {
  stepIndex.value = solution.value!.length - 1;
};
</script>

<template>
  <div class="app">
    <div class="board-container">
      <Board
        :board="board"
        :toggleCell="toggleCell"
        :rowConstraints="rowConstraints"
        :colConstraints="colConstraints"
        :setRowConstraint="setRowConstraint"
        :setColConstraint="setColConstraint"
        :currentStep="currentStep"
      />
      <Steps
        v-if="solution"
        :incrementStep
        :decrementStep
        :firstStep
        :lastStep
      />
    </div>
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
.board-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
