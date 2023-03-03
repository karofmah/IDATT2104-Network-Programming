<script setup>
import { ref } from "vue";
import axios from 'axios';

let sourceCode = "print('Hello World')"
let output=ref("");



function runAndCompile(){
  const postObject={
   sourceCode: sourceCode,
}
  console.log(sourceCode)
axios.post("http://localhost:8080/post",postObject).then(response=>{
  output.value=response.data;
  console.log(response.data)
})  
}

</script>

<template>
  <div class="content"> 
  <header>
    <h1>Code compiler </h1>
  </header>

  <main class="main">
    <textarea id="inputField" type="text" placeholder="Write code" class="main" v-model="sourceCode">print('Hello World')</textarea>
    <button id="button" class="main" @click="runAndCompile()"> Run and compile</button>
    <div class="main">
      <p>
        Output:
      </p>
      <textarea  id="outputField" type="text" class="main" disabled v-model="output"></textarea>
  </div>
  </main>

</div>
</template>


<style scoped>
#inputField{
  height: 200px;
  width:500px;
  border: solid white
}
#outputField{
  height: 200px;
  width: 500px;
}
.main{
  display: block;
}
#button{
  margin-top:50px;
  margin-bottom: 50px;
}
header {
  line-height: 1.5;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  
}
</style>
