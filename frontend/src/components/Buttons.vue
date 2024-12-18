<script setup lang="ts">
defineProps<{
  getSolution: () => void;
  eraseSolution: () => void;
  editable: boolean;
}>();
</script>

<template>
  <div class="buttons">
    <button @click="getSolution" :disabled="!editable">Get Solution</button>
    <button class="danger" v-if="!editable" @click="eraseSolution">
      Erase Solution
    </button>
  </div>
</template>

<style scoped>
.buttons {
  display: flex;
  flex-direction: column;
  gap: 30px;
  align-items: flex-end;
}
button {
  padding: 10px;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid black;
  background-color: white;
  cursor: pointer;
}
.danger {
  background-color: #f44336;
  color: white;
}
</style>
