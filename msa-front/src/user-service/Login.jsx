import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
function Login() {
  const [formData, setFormData] = useState({
    id: "",
    passwd: "",
  });
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();
  const handlerChange = (event) => {
    const { name, value } = event.target;
    setFormData((formData) => ({ ...formData, [name]: value }));
  };
  const handlerSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    if (!formData.id || !formData.passwd) {
      setErrorMessage("아이디 비밀번호는 필수항목입니다");
      return;
    }
    const result = await axios
      .post(
        "http://localhost:8080/user/login",
        {
          id: formData.id,
          passwd: formData.passwd,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
          widthCredentials: true, // CORS 필요시
        }
      )
      .then((res) => {
        (res) => {
          if (res.data && res.data.token) {
            const { token, id, name, role } = res.data;
            localStorage.setItem("jwtToken", token);
            localStorage.setItem("id", id);
            localStorage.setItem("name", name);
            localStorage.setItem("role", role);
            alert("로그인했습니다");
            navigate("/"); // Main.jsx
          } else {
            setErrorMessage("로그인 성공했으나 토큰을 할당받지 못했습니다.");
          }
        };
      })
      .catch((error) => {
        setErrorMessage(error.message);
      })
      .finally(
        setFormData({
          id: "",
          passwd: "",
        })
      );
  };
  return (
    <>
      <h3> 로그인 </h3>
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
                  required
                />{" "}
              </td>
            </tr>
            <tr>
              <th> 비밀번호 </th>
              <td>
                {" "}
                <input
                  type="password"
                  name="passwd"
                  value={formData.passwd}
                  onChange={handlerChange}
                  required
                />{" "}
              </td>
            </tr>
            <tr>
              <th colSpan="2">
                <input type="submit" value="로그인" />
              </th>
            </tr>
          </tbody>
        </table>
      </form>
      <br />
      <br />
      {errorMessage && <p style={{ color: "red" }}> {errorMessage} </p>}
      <br />
      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
    </>
  );
}
export default Login;
