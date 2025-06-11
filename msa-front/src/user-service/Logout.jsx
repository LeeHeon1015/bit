import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

function Logout() {
    const navigate = useNavigate();
    useEffect( 
        () => {
            const logout = async () => {
                try {
                    await axios.post( 
                        "http://localhost:8080/user/logout",
                        {},
                        {
                            // withCredentials: true 
                        }
                    ).then(                        
                    ).catch();
                    console.log( "서버 로그아웃 성공" );
                } catch( error ) {
                    console.error( "서버 로그아웃 실패 : " + error.message );
                } finally {
                    localStorage.removeItem( "jwtToken" );
                    localStorage.removeItem( "id" );
                    localStorage.removeItem( "name" );
                    localStorage.removeItem( "role" );
                    alert( "로그아웃 했습니다" );
                    navigate( "/" );            // Main.jsx
                }   
            }
            logout();
        }, [navigate]  
    );  
    return(
        <>
            <h3> 로그아웃 </h3>
            <br/><br/>
            <p> 로그아웃 중입니다... </p>
        </>
    );
}
export default Logout;
