import React, { useState } from 'react';
import {Card, CardContent, CardHeader, CardTitle} from './ui/Card';
import Button from './ui/Button';
import Input from './ui/Input';
import Label from './ui/Label';
import Textarea from './ui/Textarea';
import { Sliders, Play, Plus, Code } from 'lucide-react';


const RuleEngine = () => {
  const [rule, setRule] = useState('');
  const [userData, setUserData] = useState('{"age": 35, "department": "Sales", "salary": 60000}');

  const handleAddRule = () => {
    console.log('Rule added:', rule);
    setRule('');
  };

  const handleEvaluateRule = () => {
    console.log('Evaluating rule with data:', userData);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-700 via-blue-600 to-teal-500 p-8 flex items-center justify-center">
      <Card className="w-full max-w-4xl bg-white/90 backdrop-blur-sm shadow-2xl">
        <CardHeader className="bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-t-lg">
          <CardTitle className="text-4xl font-bold text-center flex items-center justify-center py-4">
            <Sliders className="mr-3 h-8 w-8" />
            Rule Engine
          </CardTitle>
        </CardHeader>
        <CardContent className="p-6 space-y-8">
          <div className="space-y-4">
            <h2 className="text-2xl font-semibold text-gray-800 flex items-center">
              <Plus className="mr-2 h-5 w-5 text-purple-500" />
              Create New Rule
            </h2>
            <div className="flex space-x-2">
              <Input
                placeholder="Enter rule (e.g., age > 30 AND department = 'Sales')"
                value={rule}
                onChange={(e) => setRule(e.target.value)}
                className="flex-grow text-lg"
              />
              <Button onClick={handleAddRule} className="bg-purple-500 hover:bg-purple-600 text-white">
                Add Rule
              </Button>
            </div>
          </div>

          <div className="space-y-4">
            <h2 className="text-2xl font-semibold text-gray-800 flex items-center">
              <Code className="mr-2 h-5 w-5 text-blue-500" />
              User Data (JSON format)
            </h2>
            <Textarea
              value={userData}
              onChange={(e) => setUserData(e.target.value)}
              rows={4}
              className="w-full text-lg"
            />
          </div>

          <Button onClick={handleEvaluateRule} className="w-full bg-gradient-to-r from-purple-500 to-blue-500 hover:from-purple-600 hover:to-blue-600 text-white text-lg py-6">
            <Play className="mr-2 h-4 w-4" /> Evaluate Rule
          </Button>
        </CardContent>
      </Card>
    </div>
  );
};

export default RuleEngine;