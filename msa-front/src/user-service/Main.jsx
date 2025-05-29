import { Link } from "react-router-dom";
function Main() {
  return (
    <>
      <h3> 메인 페이지 </h3>
      <br />
      <Link to="/input">
        <button>회원가입</button>
      </Link>
      <Link to="/user">
        <button>회원정보</button>
      </Link>
      <Link to="/login">
        <button>로그인</button>
      </Link>
      <Link to="/logout">
        <button>로그아웃</button>
      </Link>
      <br />
      <Link to="/order">
        <button>구매</button>
      </Link>
      <Link to="/admin">
        <button>관리자</button>
      </Link>
    </>
  );
}
export default Main;
