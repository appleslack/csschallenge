import React from 'react';
import classes from './OrderShelf.module.css';
import OrderShelfItem from './OrderShelfItem';

const OrderShelf = (props) => {

    let shelfStats = [];
    if( props.shelfStats != null ) {
        shelfStats = props.shelfStats.map( (stat) => {
            // return <p>{stat.item.name}</p>
            return <OrderShelfItem item={stat}/>
        });
    }

    return (
        <React.Fragment>
        <div className={classes.OrderShelfContainer}>
            <p>{props.type}</p>
            <div className={classes.OrderShelf}>
                {shelfStats}
            </div>
        </div>
        </React.Fragment>
    )

};

export default OrderShelf;
