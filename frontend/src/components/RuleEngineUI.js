import React, { useState } from 'react';
import {Card, CardContent, CardHeader, CardTitle} from './ui/Card';
import Button from './ui/Button';
import Input from './ui/Input';
import Label from './ui/Label';
import Textarea from './ui/Textarea';
import { Sliders, Play, Plus, Code } from 'lucide-react';

const RuleEngine = () => {
  const [newRule, setNewRule] = useState('');
  const [rules, setRules] = useState([]);
  const [userData, setUserData] = useState('{"age": 35, "department": "Sales", "salary": 60000}');
  const [combinedRule, setCombinedRule] = useState('');
  const [evaluationResult, setEvaluationResult] = useState('');

  const addRule = () => {
    if (newRule) {
      setRules([...rules, newRule]);
      setNewRule('');
    }
  };

  const combineRules = () => {
    setCombinedRule(rules.join(' AND '));
  };

  const parseRule = (ruleString) => {
    const tokens = ruleString.match(/\S+/g) || [];
    let index = 0;

    const parseExpression = () => {
      if (index >= tokens.length) return null;

      if (tokens[index] === 'AND' || tokens[index] === 'OR') {
        const operator = tokens[index++];
        const left = parseExpression();
        const right = parseExpression();
        return { type: 'operator', value: operator, left, right };
      } else if (tokens[index] === 'NOT') {
        index++;
        const operand = parseExpression();
        return { type: 'operator', value: 'NOT', operand };
      } else {
        const left = tokens[index++];
        const operator = tokens[index++];
        const right = tokens[index++];
        return { type: 'comparison', left, operator, right };
      }
    };

    return parseExpression();
  };

  const evaluateNode = (node, data) => {
    if (node.type === 'operator') {
      switch (node.value) {
        case 'AND':
          return evaluateNode(node.left, data) && evaluateNode(node.right, data);
        case 'OR':
          return evaluateNode(node.left, data) || evaluateNode(node.right, data);
        case 'NOT':
          return !evaluateNode(node.operand, data);
      }
    } else if (node.type === 'comparison') {
      const left = isNaN(node.left) ? data[node.left] : parseFloat(node.left);
      const right = isNaN(node.right) ? data[node.right] : parseFloat(node.right);
      switch (node.operator) {
        case '>': return left > right;
        case '<': return left < right;
        case '>=': return left >= right;
        case '<=': return left <= right;
        case '=': return left === right;
        default: return false;
      }
    }
    return false;
  };

  const evaluateRule = () => {
    try {
      const data = JSON.parse(userData);
      const ruleToEvaluate = combinedRule || rules[rules.length - 1];
      const parsedRule = parseRule(ruleToEvaluate);
      const result = evaluateNode(parsedRule, data);
      setEvaluationResult(result ? 'Rule evaluation: True' : 'Rule evaluation: False');
    } catch (error) {
      setEvaluationResult(`Error: ${error.message}`);
    }
  };

   return (
     <div className="p-4 max-w-2xl mx-auto">
       <h1 className="text-2xl font-bold mb-4">Rule Engine</h1>

       <div className="mb-4">
         <h2 className="text-xl mb-2">Create New Rule</h2>
         <Input
           placeholder="Enter rule (e.g., age > 30 AND department = 'Sales')"
           value={newRule}
           onChange={(e) => setNewRule(e.target.value)}
           className="mb-2"
         />
         <Button onClick={addRule}>Add Rule</Button>
       </div>

       {rules.length > 0 && (
         <div className="mb-4">
           <h2 className="text-xl mb-2">Current Rules</h2>
           <ul className="list-disc pl-5 mb-2">
             {rules.map((rule, index) => (
               <li key={index}>{rule}</li>
             ))}
           </ul>
           <Button onClick={combineRules}>Combine Rules</Button>
         </div>
       )}

       {combinedRule && (
         <div className="mb-4">
           <h2 className="text-xl mb-2">Combined Rule</h2>
           <p>{combinedRule}</p>
         </div>
       )}

       <div className="mb-4">
         <h2 className="text-xl mb-2">User Data (JSON format)</h2>
         <Textarea
           value={userData}
           onChange={(e) => setUserData(e.target.value)}
           rows={4}
           className="mb-2"
         />
       </div>

       <Button onClick={evaluateRule}>Evaluate Rule</Button>

       {evaluationResult && (
         <div className="mt-4">
           <h2 className="text-xl mb-2">Evaluation Result</h2>
           <p>{evaluationResult}</p>
         </div>
       )}
     </div>
   );
 };

 export default RuleEngine;