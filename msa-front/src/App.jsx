import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "./user-service/Main";
import Login from "./user-service/Login";
import Input from "./user-service/Input";
import User from "./user-service/User";
import NotFound from "./user-service/NotFound";
import Order from "./order-service/Order";
import Admin from "./admin-service/Admin";

function App() {
  return (
    <>
      <h2> MSA Front </h2>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Main />}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/input" element={<Input />}></Route>
          <Route path="/user" element={<User />}></Route>
          <Route path="*" element={<NotFound />}></Route>
          <Route path="/order" element={<Order />}></Route>
          <Route path="/admin" element={<Admin />}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
