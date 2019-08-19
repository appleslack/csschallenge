// jshint esversion: 6

import React, {Component} from 'react';
import Logo from '../../../components/Logo/Logo'
import axios from 'axios';

import classes from './Toolbar.module.css';

class Toolbar extends Component {
  
  constructor( props ) {
    super(props);

    this.state = {
      label: "Start Auto Ordering",
      autoOrdering: false
    };
  }

  onButtonClick = () => {
    if( this.state.autoOrdering === false ) {
      axios.get( 'http://localhost:8000/restaurant/startPeriodicOrdering')
      .then( (response) => {
        if( response.data === "Started" ) {
          this.setState( {label: "Stop Auto Ordering", autoOrdering:true});
          console.log('Started Auto Ordering!');
        }
        else {
          console.log('Something when wrong starting Auto Ordering');
        }
      });
    }
    else {
      axios.get( 'http://localhost:8000/restaurant/stopPeriodicOrdering')
      .then( (response) => {
        if( response.data === "Stopped" ) {
          this.setState( {label: "Start Auto Ordering", autoOrdering:false});
          console.log('Stopped Auto Ordering!');
        }
        else {
          console.log('Something when wrong stopping Auto Ordering');
          
        }
      });
    }

  }
  
  render() {
      return (
        <header className={classes.Toolbar}>
          <Logo />
          <nav>
            <button onClick={this.onButtonClick} >{this.state.label}</button>
          </nav>
          {/* <nav>
            <NavigationItems />
          </nav> */}
        </header>
      );
    }
  }
  

export default Toolbar;
