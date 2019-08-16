import React, {Component} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import classes from './TheMenu.module.css';

class TheMenu extends Component {

    menuGrid() {
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
        const menugrid = this.menuGrid();

        return (
            <React.Fragment>
                <Container className={classes.Container}>

                {menugrid}

                </Container>

            </React.Fragment>
        );
    }
}

export default TheMenu;
