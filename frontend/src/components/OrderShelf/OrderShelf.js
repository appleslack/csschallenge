import React from 'react';
import classes from './OrderShelf.module.css';

const OrderShelf = (props) => {

    let shelfStats = [];
    if( props.shelfStats != null ) {
        shelfStats = props.shelfStats.map( (stat) => {
            return <p>{stat.item.name}</p>
        });
    }

    return (
        <React.Fragment>
        <div className={classes.OrderShelf}>
            {props.type}
            {shelfStats}
        </div>
        </React.Fragment>
    )

};

export default OrderShelf;
