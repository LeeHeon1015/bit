import { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
function Order() {
  const [formData, setFormData] = useState({ id: "" });
  const [userInfo, setUserInfo] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const handlerChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };
  const handlerSubmit = (event) => {
    event.preventDefault();
    const userId = formData.id;
    if (userId) {
      axios
        .get(
          `http://localhost:8080/order/${userId}` // api-gateway로 요청
        )
        .then((res) => {
          setUserInfo(res.data);
          setErrorMessage("");
          setFormData({ id: "" });
        })
        .catch((error) => {
          setErrorMessage(error.message);
          setUserInfo(null);
          setFormData({ id: "" });
        });
    } else {
      setErrorMessage("아이디를 입력하세요");
    }
  };
  const handlerUser = (event) => {
    const token = localStorage.getItem("jwtToken");
    axios
      .get("http://localhost:8080/order", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => console.log("요청 성공"))
      .catch((error) => setErrorMessage(error.errorMessage));
  };
  const handlerAdmin = (event) => {
    const token = localStorage.getItem("jwtToken");
    axios
      .get("http://localhost:8080/admin", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => console.log("요청 성공"))
      .catch();
  };
  return (
    <>
      <h3> 제품 구매 </h3>
      <br />
      <form onSubmit={handlerSubmit}>
        <table border="1">
          <tbody>
            <tr>
              <th> 아이디 </th>
              <td>
                {" "}
                <input
                  type="text"
                  name="id"
                  value={formData.id}
                  onChange={handlerChange}
                />{" "}
              </td>
            </tr>
            <tr>
              <th colSpan="2">
                <input type="submit" value="확인" />
              </th>
            </tr>
          </tbody>
        </table>
      </form>
      <br />
      <br />
      {errorMessage && <p style={{ color: "red" }}> {errorMessage} </p>}
      {userInfo && (
        <div>
          <h4> 구매자 정보 </h4>
          {userInfo.id && <p> 아이디 : {userInfo.id} </p>}
          {userInfo.passwd && <p> 비밀번호 : {userInfo.passwd} </p>}
          {userInfo.name && <p> 이름 : {userInfo.name} </p>}
          {userInfo.tel && <p> 전화번호 : {userInfo.tel} </p>}
          {userInfo.role && <p> 아이디 : {userInfo.role} </p>}
        </div>
      )}
      <br />
      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
      <button onClick={handlerUser}> 사용자 </button>
      <button onClick={handlerAdmin}> 관리자 </button>
    </>
  );
}
export default Order;
