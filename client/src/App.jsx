import React from "react";
import StockList from "./components/StockList";
import { Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Instrument from "./pages/Instrument";

const App = () => {
    return(
        <div>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/stocks" element={<Home/>}/>
                <Route path="/stocks/s" element={<Instrument />}/>
            </Routes>
        </div>

    );
}

export default App;