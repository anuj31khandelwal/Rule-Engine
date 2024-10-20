import axios from 'axios';

   const API_BASE_URL = 'http://localhost:8080/api';

   const api = axios.create({
     baseURL: API_BASE_URL,
   });

   export const createRule = (rule) => api.post('/rules/create', rule);
      export const combineRules = (rules) => api.post('/rules/combine', rules);
      export const evaluateRule = (data) => api.post('/rules/evaluate', data);