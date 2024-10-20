// src/components/ui/Input.js
import React from 'react';

const Input = ({ id, value, onChange, placeholder, className = '' }) => {
  return (
    <input
      id={id}
      type="text"
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
    />
  );
};

export default Input;