import React, {Component} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
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

    menuGrid_Bootstrap() {
        if( this.props.menu == null ) {
            return [];
        }

        const items = this.props.menu.map( (item) => {
            return(item.name);
        });

        // Break up first
        const numCols = 10;
        let numRows = this.props.menu.length;
        const rowitems = [];
        let start = 0;
        let end = numCols;
        // console.log(this.props.menu.slice(0,5));

        const cols = [];
        for (let i = 0; i < numRows; i++) {
            const row = items.slice(start, end);
            console.log(row);
            cols[i] = row.map( (item) => {
                return (<Col  className={classes.Col}>{item}</Col>);
            });
            
            start+=numCols;
            end+=numCols;
        }
        for( let i = 0; i < numRows; i++ ) {
            rowitems[i] = <Row className={classes.Row}>{cols[i]}</Row>;
        }
        return rowitems;
    }
    
    render() {
        const menugrid_Bootstrap = this.menuGrid_Bootstrap();
        const menugrid_Grid = this.menuGrid_Grid();

        return (
            <React.Fragment>
                <h4> Our Menu</h4>
                {/* <Container className={classes.Container}> */}
                <div className={classes.menuGridContainer}>
                    {menugrid_Grid}
                    {/* <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 2"/>
                    <RestaurantMenuItem name="Item 3"/>
                    <RestaurantMenuItem name="Item 4"/>
                    <RestaurantMenuItem name="Item 5"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 10"/>
                    <RestaurantMenuItem name="Item 11"/>
                    <RestaurantMenuItem name="Item 12"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
                    <RestaurantMenuItem name="Item 1"/>
 */}
                </div>
            </React.Fragment>
        );
    }
}

export default RestaurantMenu;
