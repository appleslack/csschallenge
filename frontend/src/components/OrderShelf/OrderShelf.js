import React from 'react';
import classes from './OrderShelf.module.css';

const OrderShelf = (props) => {

    return (
        <React.Fragment>
        <div className={classes.OrderShelf}>
            {props.type}
        </div>
        </React.Fragment>
    )

};

export default OrderShelf;
