import { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
function Input() {
  const [formData, setFormData] = useState({
    id: "",
    passwd: "",
    name: "",
    tel: "",
    role: "Role_User",
  });
  const [success, setSuccess] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const handlerChange = (event) => {
    const { name, value } = event.target;
    setFormData((formData) => ({ ...formData, [name]: value }));
  };
  const handlerSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setSuccess(false);
    if (!formData.id || !formData.passwd || !formData.name) {
      setErrorMessage("아이디, 비밀번호, 이름은 필수 입력사항입니다.");
      return;
    }
    const result = await axios
      .post("http://localhost:8080/user/input", formData)
      .then((res) => {
        setSuccess(true);
      })
      .catch((error) => {
        setErrorMessage(error.message);
      })
      .finally(
        setFormData({
          id: "",
          passwd: "",
          name: "",
          tel: "",
          role: "Role_User",
        })
      );
  };
  return (
    <>
      <h3> 회원가입 </h3>
      <br />
      <form onSubmit={handlerSubmit}>
        <table border="1">
          <tbody>
            <tr>
              <th> 아이디 </th>
              <td>
                <input
                  type="text"
                  name="id"
                  value={formData.id}
                  onChange={handlerChange}
                  required
                />
              </td>
            </tr>
            <tr>
              <th> 비밀번호 </th>
              <td>
                <input
                  type="password"
                  name="passwd"
                  value={formData.passwd}
                  onChange={handlerChange}
                  required
                />
              </td>
            </tr>
            <tr>
              <th> 이름 </th>
              <td>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handlerChange}
                  required
                />
              </td>
            </tr>
            <tr>
              <th> 전화번호 </th>
              <td>
                <input
                  type="text"
                  name="tel"
                  value={formData.tel}
                  onChange={handlerChange}
                />
              </td>
            </tr>
            <tr>
              <th> 권한 </th>
              <td>
                <select
                  name="role"
                  value={formData.role}
                  onChange={handlerChange}
                >
                  <option value="ROLE_USER">사용자</option>
                  <option value="ROLE_ADMIN">관리자</option>
                </select>
              </td>
            </tr>
            <tr>
              <th colSpan="2">
                <input type="submit" value="가입" />
              </th>
            </tr>
          </tbody>
        </table>
      </form>
      <br />
      <br />
      {success && <p style={{ color: "green" }}> 회원가입이 완료됐습니다. </p>}
      {errorMessage && <p style={{ color: "red" }}> {errorMessage} </p>}
      <br />
      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
    </>
  );
}
export default Input;
