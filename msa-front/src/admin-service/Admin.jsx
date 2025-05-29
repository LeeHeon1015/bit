import { Link } from "react-router-dom";
function Admin() {
  return (
    <>
      <h3> 회원목록 </h3>
      <br />
      <br />
      <Link to="/">
        {" "}
        <button>메인</button>{" "}
      </Link>
    </>
  );
}
export default Admin;
