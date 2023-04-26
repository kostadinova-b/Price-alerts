import React from "react";
import { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
  
    const handleUsernameChange = (event) => {
      setUsername(event.target.value);
    };
  
    const handlePasswordChange = (event) => {
      setPassword(event.target.value);
    };
  
    const handleSubmit = (event) => {
      event.preventDefault();
      console.log(`Logging in with username ${username} and password ${password}`);
    };

    const handleClick = () => {
      localStorage.setItem('id', username);
      navigate("/stocks");
      console.log(sessionStorage.getItem('id'));
    }
  
    return (
        <div style={{backgroundColor:"black"}}>
          <Modal centered show>
          <Modal.Header >
          <Modal.Title style={{textAlign: "center"
, justifyContent: "center"}}>Notification System</Modal.Title>
        </Modal.Header>
          <Form className="bg-dark p-4" >
            <Form.Group className="mb-3">
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={handleUsernameChange}
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Control
                type="password"
                placeholder="Enter password"
                value={password}
                onChange={handlePasswordChange}
              />
            </Form.Group>
            <Button variant="dark" type="submit" onClick={handleClick}>
              Login
            </Button>
          </Form>
          </Modal>
      </div>
    )

}

export default Login;