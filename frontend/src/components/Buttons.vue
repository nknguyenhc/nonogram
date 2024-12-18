<script setup lang="ts">
defineProps<{
  getSolution: () => void;
}>();
</script>

<template>
  <div>
    <button @click="getSolution">Get Solution</button>
  </div>
</template>

<style scoped>
button {
  padding: 10px;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid black;
  background-color: white;
  cursor: pointer;
}
</style>
