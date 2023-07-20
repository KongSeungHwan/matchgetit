import React, { useState } from 'react';
import '../../styles/LogIn/SearchAccount.css';
import axios from "axios";


function FindId() {
    const [name, setName] = useState('');
    const [pn, setPhoneNum] = useState('');
    const [value, setValue] = useState('');
    const [emailVisible, setEmailVisible] = useState(false);

    const handleFormSubmit = (e) => {
        e.preventDefault();

        axios
            .post('/matchGetIt/find/Id', {
                name,
                pn
            })
            .then((response) => {
                setValue(response.data);
                console.log(response.data);
                // 아이디찾기 성공시 처리
            })
            .catch((error) => {
                console.error(error.response.data);
                // 아이디찾기 실패시 처리
            });
    }
    return (
        <div className="find-container" id="phone-menu">
            <div className="findInputdata">
                <label>이름</label>
                <input
                    type="text"
                    className="find_input"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </div>
            <div className="findInput">
                <label>휴대폰 번호</label>
                <input
                    type="tel"
                    className="find_input"
                    value={pn}
                    id="pNum"
                    onChange={(e) => setPhoneNum(e.target.value)}
                />
            </div>
            <button
                type="submit"
                className="button_check"
                onClick={handleFormSubmit}
            >
                확인
            </button>
            <p>{value}</p>
        </div>
    );
}
export default FindId;
