import React, {useState} from 'react';
import '../../styles/LogIn/SearchAccount.css';
import axios from "axios";

function FindPw({session}) {
    const [email, setEmail] = useState('');
    const [temporaryPw, setTemporaryPw] = useState('');
    const [errMsg , setErrMsg]=useState('');
    const handleConfirmationClick = () => {
        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }
        axios
            .post('/matchGetIt/find/sendMail', {
                email,
                temporaryPw
            })
            .then((response) => {
                console.log(response.data);
                window.alert(response.data);
                // 인증번호 확인 성공시 처리
            })
            .catch((error) => {
                setErrMsg(error.message);
                console.log(errMsg);
                // 인증번호 확인 실패시 처리
            });
    };

    return (
        <div className="find-container" id="email-menu">
            <div className="findInputdata">
                <label>아이디</label>
                <input
                    type="email"
                    className="find_input"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

            </div>
            <div className="findInputdata">
                <label>이메일</label>
                <input
                    type="email"
                    className="find_input"
                    value={temporaryPw}
                    onChange={(e) => setTemporaryPw(e.target.value)}
                />
            </div>
            <button type="button" value="인증번호 발급" className="button_check" onClick={handleConfirmationClick}>임시비밀번호
                발급
            </button>
            <p>{errMsg}</p>
        </div>
    )
}

export default FindPw;