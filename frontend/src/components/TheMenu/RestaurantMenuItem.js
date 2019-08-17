import React from 'react';
import classes from './RestaurantMenuItem.module.css';

const restaurantMenuItem = (props) => {
    return (
        
        <div className={[classes.RestaurantMenuItem, classes[props.temp]].join(' ')}>
            {props.item.name}
        </div>
    );
 
}

export default restaurantMenuItem;