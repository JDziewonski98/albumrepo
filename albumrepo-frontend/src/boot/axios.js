// src/boot/axios.js

import Vue from 'vue'
import axios from 'axios'

// Axios config
const backendUrl = 'http://127.0.0.1:8080/api/v1/'

Vue.prototype.$axios = axios.create({
  baseURL: backendUrl,
  headers: {
    'Content-Type': 'application/json'
  }
})
