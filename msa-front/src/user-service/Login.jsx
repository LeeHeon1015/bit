import { Link } from "react-router-dom";
function Login() {
  return (
    <>
      <h3> 로그인 </h3>

      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
    </>
  );
}
export default Login;
