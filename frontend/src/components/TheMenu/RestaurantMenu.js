import React, {Component} from 'react';
import RestaurantMenuItem from './RestaurantMenuItem';

import classes from './RestaurantMenu.module.css';

class RestaurantMenu extends Component {

    menuGrid_Grid() {
        if( this.props.menu == null ) {
            return [];
        }
        const items = this.props.menu.map( (menuItem) => {
            return(<RestaurantMenuItem item={menuItem}/>);
        });
        return items;
    }
    
    render() {
        const menugrid_Grid = this.menuGrid_Grid();

        return (
            <React.Fragment>
                <h4> Our Menu</h4>
                {/* <Container className={classes.Container}> */}
                <div className={classes.menuGridContainer}>
                    {menugrid_Grid}
                </div>
            </React.Fragment>
        );
    }
}

export default RestaurantMenu;
