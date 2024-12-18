<script setup lang="ts">
defineProps<{
  incrementStep: () => void;
  decrementStep: () => void;
  firstStep: () => void;
  lastStep: () => void;
}>();
</script>

<template>
  <div class="steps">
    <div class="button" @click="firstStep">First</div>
    <div class="button" @click="decrementStep">Previous</div>
    <div class="button" @click="incrementStep">Next</div>
    <div class="button" @click="lastStep">Last</div>
  </div>
</template>

<style scoped>
.steps {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
.button {
  padding: 10px;
  margin: 10px;
  border: 1px solid black;
  border-radius: 5px;
  cursor: pointer;
}
</style>
