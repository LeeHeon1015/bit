import { Link } from "react-router-dom";
import axios from "axios";
function Admin() {
  const handlerUser = (event) => {
    const token = localStorage.getItem("jwtToken");
    axios
      .get("http://localhost:8080/order", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => console.log("요청 성공"))
      .catch();
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
      <h3> 회원목록 </h3>
      <br />
      <br />
      <Link to="/">
        {" "}
        <button> 메인 </button>
      </Link>
      <button onClick={handlerUser}> 사용자 </button>
      <button onClick={handlerAdmin}> 관리자 </button>
    </>
  );
}
export default Admin;
