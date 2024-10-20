// src/components/ui/Textarea.js
import React from 'react';

const Textarea = ({ id, value, onChange, placeholder, rows = 4, className = '' }) => {
  return (
    <textarea
      id={id}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      rows={rows}
      className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
    />
  );
};

export default Textarea;