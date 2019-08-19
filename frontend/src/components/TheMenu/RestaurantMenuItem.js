import React, {Component} from 'react';
import classes from './RestaurantMenuItem.module.css';
import axios from 'axios';

class restaurantMenuItem extends Component {
    onClick = () => {

        axios.get( 'http://localhost:8000/restaurant/orderItem?itemName='+this.props.item.name )
        .then( (response) => {
          this.setState( {shelfStats: response.data});
        });
    }
    
    render() {
        return (
            <div onClick={this.onClick} 
                className={[classes.RestaurantMenuItem, classes[this.props.temp]].join(' ')}>
                {this.props.item.name}
            </div>
        );
    }
}

export default restaurantMenuItem;