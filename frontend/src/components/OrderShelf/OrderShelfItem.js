import React from 'react';
import classes from './OrderShelfItem.module.css';

// An OrderShelfItem is used to display each active order that's currently
// sitting on a shelf.  These come an go as orders are created and show the
// order number, name of the item and the current normalized decay that's updated
// periodically on the server.
// The props sent in are the order item - as an example:
//    {"name":"Frosty", "shelfLife":135, "decayRate":0.52, "shelfName":"frozen", "deliveryShelf":"FROZEN"},"orderDate":1566228298944,
//     "orderPreparedDate":1566228300398, "orderNumber":173, "normalizedDecay":1.0,
//     "orderStatus":"PREPARED", "itemName":"Frosty"}

const OrderShelfItem = (props) => {

    return (
        <React.Fragment>
        <div className={classes.OrderShelfItem}>
            <p className={classes.OrderShelfItemName}>{props.item.orderNumber} : {props.item.itemName}</p>
            <p>Decay: {props.item.normalizedDecay.toFixed(2)}</p>
        </div>
        </React.Fragment>
    )

};

export default OrderShelfItem;
