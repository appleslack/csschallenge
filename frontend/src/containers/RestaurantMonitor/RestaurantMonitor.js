// jshint esversion: 6

import React, {Component} from 'react';
import axios from 'axios';
import RestaurantMenu from '../../components/TheMenu/RestaurantMenu';
import OrderShelf from '../../components/OrderShelf/OrderShelf';
import classes from '../../components/OrderShelf/OrderShelf.module.css';

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
      shelfStats: {hot:null, cold:null, frozen:null},
    }
    return state;
  }

  componentDidMount = () => {
    //  Get the menu from the server
    axios.get( 'http://localhost:8000/restaurant/menu')
      .then( (response) => {
        this.setState( {menu: response.data});
      });

    axios.get( 'http://localhost:8000/restaurant/shelfStats')
      .then( (response) => {
        console.log( "Cold Shelf Stats: " + response.data.cold);
        console.log( "Hot Shelf Stats: " + response.data.hot);
        console.log( "Frozen Shelf Stats: " + response.data.frozen);
        if( response.data.frozen.length != 0 ) {
          response.data.frozen.map( (item) => {
            console.log(item.item.name);
          });
        }
        this.setState( {shelfStats: response.data});
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
        <RestaurantMenu menu={this.state.menu}/>
        <div className={classes.OrderShelfArea}>
          {/* Note:  Break up shelfes into type */}
          <OrderShelf className={classes.OrderShelf} type="Hot Shelf" shelfStats={this.state.shelfStats.hot}/>
          <OrderShelf className={classes.OrderShelf} type="Cold Shelf" shelfStats={this.state.shelfStats.cold}/>
          <OrderShelf className={classes.OrderShelf} type="Frozen" shelfStats={this.state.shelfStats.frozen}/>
        </div>
  
      </React.Fragment>
    );
  }
}

export default RestaurantMonitor;
