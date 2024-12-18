<script setup lang="ts">
defineProps<{
  incrementStep: () => void;
  decrementStep: () => void;
  firstStep: () => void;
  lastStep: () => void;
  maxStep: number;
  stepIndex: number;
  jumpToStep: (index: number) => void;
}>();
</script>

<template>
  <div class="steps-container">
    <input
      type="range"
      min="0"
      :max="maxStep"
      :value="stepIndex + 1"
      @input="
        (event) => jumpToStep((event.target as HTMLInputElement).valueAsNumber)
      "
    />
    <div class="steps">
      <div class="button" @click="firstStep">First</div>
      <div class="button" @click="decrementStep">Previous</div>
      <div class="button" @click="incrementStep">Next</div>
      <div class="button" @click="lastStep">Last</div>
    </div>
  </div>
</template>

<style scoped>
.steps-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}
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
