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
    this.pollTimer = null;
  }

  getInitialState = () => {
    let state = {
      connected : false,
      restaurantOpen : false,
      autoOrderingStarted: false,
      error : null,
      menu : null,
      shelfStats: {hot:null, cold:null, frozen:null},
    }
    return state;
  }

  // This is called when the restaurant is opened the first time.
  openRestaurant = (restaurantState) => {
    console.log( 'The restarant is openning');

    if( this.state.autoOrderingStarted !== restaurantState.autoOrderingStarted ) {
      this.setState( {autoOrderingStarted: restaurantState.autoOrderingStarted});
    }
    this.setState( {restaurantOpen: true});

    // I could have returned this in the state - but that'll be called multiple times.  This
    // only needs to be called once if we don't have a menu yet...
    if( this.state.menu == null ) {
      axios.get( 'http://localhost:8000/restaurant/menu')
      .then( (response) => {
        this.setState( {menu: response.data});
      });
    }

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
        };
        eventSource.onerror = (event) => console.log('error', event);
      }
  
  }

  retrieveRestaurantState = () => {
    console.log( "Checking restaurant state...");
    
      // Is the restaurant open?  If not, put up a splash (later) instead of doing
      // anything else...
      axios.get( 'http://localhost:8000/restaurant/state')
      .then( (response) => {
        let openState = response.data.restaurantOpen;
        if( openState === "true" && this.state.restaurantOpen === false ) {
          this.openRestaurant(response.data);
          // If there's a poll timer set, then cancel it now
          if( this.timer !== null ) {
            clearInterval(this.timer);
            this.timer = null;
          }
        }
      });
  }
  
  pollRestaurantStatePeriodically = () => {
  
    this.pollTimer = setInterval( () => {
      this.retrieveRestaurantState();
    }, 4000);
  }

  componentDidMount = () => {
    this.retrieveRestaurantState();

    // Check after a few seconds if the restaurant state is open.  If not, then periodically
    // poll the restaurant for it's state information and update the UI when it opens.
    setTimeout( () => {
      if( this.state.restaurantOpen === false ) {
        this.pollRestaurantStatePeriodically();
      }
    }, 1000);

  }


  render () {
    if( this.state.restaurantOpen === true ) {
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
    else {
      return (
        <h1>The Restaurant Is Closed!</h1>
      );
    }
  }
}

export default RestaurantMonitor;
