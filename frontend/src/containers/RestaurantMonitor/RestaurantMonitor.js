// jshint esversion: 6

import React, {Component} from 'react';
import axios from 'axios';
import TheMenu from '../../components/TheMenu/TheMenu';

// import Modal from '../../components/UI/Modal/Modal';
import StatusMonitor from '../../components/Monitors/StatusMonitor';

class RestaurantMonitor extends Component {
  constructor(props) {
    console.log('CellarMonitor constructor');

    super(props);
    this.state = this.getInitialState();
  }

  getInitialState = () => {
    let state = {
      connected : false,
      connecting : false,
      error : null,
      menu : null,
    }
    return state;
  }

  componentDidMount = () => {
    //  Get the menu from the server
    axios.get( 'http://localhost:8000/restaurant/menu')
      .then( (response) => {
        this.setState( {menu: response.data});
      });

    console.log('RestaurantMonitor componentDidMount');

    setTimeout( () => {
      this.connectToRestaurant();
    }, 1000);

  }

  connectToRestaurant = () => {
    console.log('Connecting to Restaurant...');
    this.setState( {connecting: true });

  }

  render () {
    return (
      <React.Fragment>
        {/* <Monitors currentState={this.state}/> */}
        <TheMenu menu={this.state.menu}/>

      </React.Fragment>
    );
  }
}

export default RestaurantMonitor;
