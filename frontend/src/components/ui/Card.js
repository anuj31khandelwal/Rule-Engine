// src/components/ui/Card.js
import React from 'react';

const Card = ({ children, className = '' }) => {
  return (
    <div className={`bg-white shadow-md rounded-lg overflow-hidden ${className}`}>
      {children}
    </div>
  );
};

const CardHeader = ({ children, className = '' }) => {
  return <div className={`px-4 py-5 border-b border-gray-200 ${className}`}>{children}</div>;
};

const CardContent = ({ children, className = '' }) => {
  return <div className={`p-4 ${className}`}>{children}</div>;
};

const CardTitle = ({ children, className = '' }) => {
  return <h3 className={`text-lg font-medium text-gray-900 ${className}`}>{children}</h3>;
};

export { Card, CardHeader, CardContent, CardTitle };