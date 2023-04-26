import React, { useEffect } from "react";
import { Table, Image, Pagination } from "react-bootstrap";
import image1 from "../img/stock.png";
import image2 from "../img/stock1.jpg";
import image3 from "../img/stock2.jpg";
import image4 from "../img/stock3.jpg";
import image5 from "../img/stock0.png";
import image6 from "../img/stock4.jpg";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import './style.css';


const StockList = () => {
  const [ids, setIds] = useState([1, 2, 3, 4, 5, 6, 7,8,9,10]);
  const currencies = ['USD', 'EUR', 'GBP', 'NZD', 'BGN'];
  const markets = ['NYSE', 'NASDAQ', 'LSE', 'TSE', 'SSE', 'HKSE', 'ASX', 'FSE'];
  const imgs = [image1, image2, image3, image4, image5, image6];
  const [currentPage, setCurrentPage] = useState(1);
  const imgArray = [];
  const currArray = [];
  const marketArray = [];
  const navigate = useNavigate();




  const getRandom = (arr) => {
    return Math.floor(Math.random() * arr.length);
  }


  for(let i = 0; i < 10; i++){
    imgArray.push(imgs[getRandom(imgs)]);
    currArray.push(currencies[getRandom(currencies)]);
    marketArray.push(markets[getRandom(markets)]);
  }

  const changePage = (i) => {
    const updatedArray = ids.map((value, index) => {
      return (i-1)*10 + (index + 1);
    });
    setIds(updatedArray);
    setCurrentPage(i);

  }


  const renderPaginationItems = () => {
    const items = [];
    for (let i = 1; i <= 10; i++) {
      items.push(
        <Pagination.Item  key={i} active={i === currentPage} onClick={() => changePage(i)}>
          {i}
        </Pagination.Item>
      );
    }
    return items;
  };


  return (
      <div style={{paddingLeft:"18vw", paddingRight:"18vw", paddingTop: "5vh"}}>
        <Table size="sm" variant="white" borderless className="table-hover" >
          <thead>
            <tr>
              <th>Stock</th>
              <th></th>
              <th className="text-center">Currency</th>
              <th className="text-center">Market</th>
            </tr>
          </thead>
          <tbody >
            {ids.map((item, index) =>
            <tr key={ids[index]} className="border-top border-bottom" onClick={() => navigate(`/stocks/s?id=${ids[index]}&img=${imgArray[index]}&currency=${currArray[index]}&market=${marketArray[index]}`)  
            }>
            <td style={{ width: 60, height: 60 }}>
              <Image src={imgArray[index]} roundedCircle width={60} height={60} />
  
            </td>
            <td className="text-left align-middle">{'#instrument_'+ids[index]}</td>
            <td className="text-center align-middle">{currArray[index]}</td>
            <td className="text-center align-middle">{marketArray[index]}</td>
            </tr>)}
          </tbody>
    
        </Table>

        <Pagination style={{paddingTop:"3vh"}} className="justify-content-center">{renderPaginationItems()}</Pagination>

      </div>
  );
};

export default StockList;
