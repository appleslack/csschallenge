// jshint esversion:6

import React from 'react';
import './App.css';
import Layout from './components/Layout/Layout';
import RestaurantMonitor from './containers/RestaurantMonitor/RestaurantMonitor';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Layout>
          <RestaurantMonitor/>
        </Layout>
      </header>
    </div>
  );
}

export default App;
