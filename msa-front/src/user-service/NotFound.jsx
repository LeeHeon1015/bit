import { Link } from "react-router-dom";
function NotFound() {
  return (
    <>
      <h3> 기본 페이지 </h3>
      페이지를 찾을 수 없습니다.
      <br />
      <Link to="/">
        <button>메인</button>
      </Link>
    </>
  );
}
export default NotFound;
