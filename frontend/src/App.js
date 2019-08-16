// jshint esversion:6

import React from 'react';
import logo from './logo.svg';
import './App.css';
import Button from 'react-bootstrap/Button';
import Badge from 'react-bootstrap/Badge';
import Layout from './components/Layout/Layout';
import RestaurantMonitor from './containers/RestaurantMonitor/RestaurantMonitor';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Layout>
          <RestaurantMonitor/>
        </Layout>
        <Button >Open Restaurant</Button>
      </header>
    </div>
  );
}

export default App;
