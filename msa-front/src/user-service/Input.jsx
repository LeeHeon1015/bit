import { Link } from "react-router-dom";
function Input() {
  return (
    <>
      <h3> 회원가입 </h3>

      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
    </>
  );
}
export default Input;
