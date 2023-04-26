import React, { useState, useEffect } from "react";
import {
  ToggleButtonGroup,
  ToggleButton,
  Button,
  InputGroup,
  Tooltip,
  OverlayTrigger,
  Image,
  Form,
  Container,
  Row,
  Col,
  Dropdown,
  DropdownButton,
  FormControl,
  Table,
} from "react-bootstrap";
import { useLocation } from "react-router-dom";
import { getUserToken } from "../js/firebase.js";

const Stock = () => {
  const [pushEnabled, setPushEnabled] = useState(false);
  const [pushDisabled, setPushDisabled] = useState(false);
  const [isTokenFound, setTokenFound] = useState(false);

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const id = params.get("id");
  const img = params.get("img");
  const currency = params.get("currency");
  const market = params.get("market");

  const [stock, setStock] = useState({ id: 0, buy: 23.1, sell: 11.2 });

  const [dto, setDto] = useState({
    userId: localStorage.getItem("id"),
    stockId: `${id}`,
    sType: "CUSTOM",
    pType: "BUY",
    price: "",
    threshold: ""
  });


  useEffect(() => {
    getStock();

    const interval = setInterval(() => {
      getStock();
    }, 1000);
    return () => clearInterval(interval);

  }, []);

  useEffect(() => {
    if (Notification.permission === "granted") {
      setPushEnabled(true);
    } else if (Notification.permission === "denied") {
      setPushDisabled(true);
    }
  }, []);

  const sendToken = (token) => {
    console.log("sending token to server");
    fetch("http://localhost:8081/tokens", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({id: dto.userId, token: token}),
    })
      .then((response) => response.json())
      .catch((error) => console.error(error));
  };
  


  const getStock = async () => {
    //console.log(id);
    try {
      const response = await fetch(`http://localhost:8080/stocks/${id}`);
      const json = await response.json();
      const { buy, sell } = json;
   //   console.log(buy + " " + sell);
      setStock({ id, buy, sell });
    } catch (error) {
      //console.error(error);
    }
  };

  const askPermission = async() => {
    Notification.requestPermission().then(async (permission) => {
      if (permission === "granted") {
        setPushEnabled(true);
        console.log("regitering service worker")
        navigator.serviceWorker.register('../firebase-messaging-sw.js')
        try{
          const currentToken = await getUserToken(setTokenFound);
          sessionStorage.setItem('token', currentToken);
          //setToken(currentToken);
          //console.log("sending token: "+currentToken);
          sendToken(currentToken);

        } catch (err) {
          console.error('An error occurred while retrieving the token:', err);
        }
      } else {
        setPushDisabled(true);
        console.log("Push disabled");
      }
  });
  };

  const subscribe = () => {
    let updatedDto = {...dto};
    if(dto.sType === "THRESHOLD"){
      updatedDto.price = (dto.pType === "BUY") ? stock.buy.toString() : stock.sell.toString();
    }

    console.log(JSON.stringify(updatedDto));

    fetch("http://localhost:8080/subscriptions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedDto)
    })
      .catch((error) => console.error(error));
  };

  return (
    <div>
      <Container className="mt-5">
        <Row>
          <Col className="d-flex align-items-center justify-content-center">
            <h1>#instrument_{id}</h1>
          </Col>
        </Row>
        <Row className="mt-5"></Row>
        <Row>
          <Col className="d-flex align-items-center justify-content-start">
            <Image src={img} roundedCircle width={90} height={90} />
            <Table>
              <thead>
                <tr>
                  <th>Buy</th>
                  <th>Sell</th>
                  <th>Currency</th>
                  <th>Market</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td style={{ minWidth: '100px' }}>{stock.buy}</td>
                  <td style={{ minWidth: '100px' }}>{stock.sell}</td>
                  <td>{currency}</td>
                  <td>{market}</td>
                </tr>
              </tbody>
            </Table>
          </Col>
        </Row>

        <Row className="mt-5">
          <hr></hr>
          <Col></Col>
          <Col>
            <ToggleButtonGroup type="checkbox" value={dto.sType}>
              <ToggleButton
                variant="outline-dark"
                value="CUSTOM"
                onClick={() => setDto((prev) => ({ ...prev, sType: "CUSTOM" }))}
              >
                Custom
              </ToggleButton>
              <ToggleButton
                variant="outline-dark"
                value="THRESHOLD"
                onClick={() =>
                  setDto((prev) => ({ ...prev, sType: "THRESHOLD" }))
                }
              >
                Threshold
              </ToggleButton>
            </ToggleButtonGroup>
          </Col>
          <Col>
            {dto.sType === "CUSTOM" ? (
              <InputGroup className="mb-3">
                <InputGroup.Text>$</InputGroup.Text>
                <Form.Control
                  type="text"
                  value={dto.price}
                  onChange={(event) =>
                    setDto({ ...dto, price:event.target.value })
                  }
                />
                <InputGroup.Text>.00</InputGroup.Text>
              </InputGroup>
            )

            : (
              <InputGroup className="mb-3">
                <Form.Control
                  type="text"
                  value={dto.threshold}
                  onChange={(event) =>
                    setDto({...dto, threshold:event.target.value })
                  }
                />
                <InputGroup.Text>%</InputGroup.Text>
              </InputGroup>
            )}
          </Col>
          <Col>
            <InputGroup>
              <FormControl
                type="text"
                placeholder="Select price"
                value={dto.pType}
                readOnly
              />
              <DropdownButton variant="dark" title="" drop="start" >
                <Dropdown.Item
                  eventKey="1"
                  onClick={() => setDto((prev) => ({ ...prev, pType: "BUY" }))}
                >
                  BUY
                </Dropdown.Item>
                <Dropdown.Item
                  eventKey="2"
                  onClick={() => setDto((prev) => ({ ...prev, pType: "SELL" }))}
                >
                  SELL
                </Dropdown.Item>
              </DropdownButton>
            </InputGroup>
          </Col>

          <Col>
            {pushEnabled ? (
              <Button variant="dark" onClick={subscribe}>
                Subscribe
              </Button>
            ) : (
              <OverlayTrigger
                placement="bottom"
                overlay={
                  <Tooltip>
                    You need to allow push notifications in order to Subscribe!
                  </Tooltip>
                }
              >
                <Button variant="outline-dark" onClick={askPermission}>
                  Subscribe
                </Button>
              </OverlayTrigger>
            )}
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Stock;
