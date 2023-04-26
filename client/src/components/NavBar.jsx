import React from "react";
import Navbar from 'react-bootstrap/Navbar';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import InputGroup from 'react-bootstrap/InputGroup';
import { useNavigate } from "react-router-dom";

const NavBar = () => {

  const navigate = useNavigate();

    return(
        <Navbar bg="dark" variant="dark" expand="lg">
      <Container fluid>
        <Navbar.Brand href="#">Notification</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="justify-content-end flex-grow-1 pe-3"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link onClick={() => navigate("/stocks")} >Stocks</Nav.Link>
            <Nav.Link onClick={() => navigate("/")} >LogOut</Nav.Link>
          
          </Nav>
          
          {/* <Form className="d-flex">
          <InputGroup>
            <Form.Control
              type="search"
              //placeholder="Instrument name..."
              //className="me-2"
              aria-label="Search"
            />
            <Button variant="outline-secondary">Search</Button>
            </InputGroup>
          </Form> */}
        </Navbar.Collapse>
      </Container>
    </Navbar>
        
    );

}

export default NavBar;