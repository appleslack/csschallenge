// jshint esversion: 6

import React, {Component} from 'react';
import axios from 'axios';
import RestaurantMenu from '../../components/TheMenu/RestaurantMenu';
import OrderShelf from '../../components/OrderShelf/OrderShelf';
import classes from '../../components/OrderShelf/OrderShelf.module.css';

class RestaurantMonitor extends Component {
  
  constructor(props) {

    super(props);
    this.state = this.getInitialState();
    this.eventSource = null;
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
        this.setState( {shelfStats: response.data});
      });
      
      // Support Server-Send Events (SSE) to get periodic updates of the shelf stats
      if( this.eventSource == null ) {
        const eventSource = new EventSource('http://localhost:8000/restaurant/sse/shelfStats'); 
        // eventSource.onopen = (event) => console.log('Async Open Received'); 
        eventSource.onmessage = (event) => {
          console.log( "SSE message received!");
          const stats = JSON.parse(event.data); 
          this.setState({shelfStats: stats});
  
          // this.state.profiles.push(profile);
          // this.setState({profiles: this.state.profiles}); 
        };
        // eventSource.onerror = (event) => console.log('error', event);
      }
  
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
          <OrderShelf className={classes.OrderShelf} type="Frozen Shelf" shelfStats={this.state.shelfStats.frozen}/>
        </div>
        <div className={classes.OrderShelfArea}>
          <OrderShelf className={classes.OrderShelf} type="Overflow Shelf" shelfStats={this.state.shelfStats.overflow}/>
        </div>
      </React.Fragment>
    );
  }
}

export default RestaurantMonitor;
