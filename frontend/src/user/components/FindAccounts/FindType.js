import React, {useState} from "react";
import '../../styles/LogIn/SearchAccount.css';
import FindId from './FindId';
import FindPw from './FindPw';

function FindType() {
    const [activeTab, setActiveTab] = useState("");
    const [isSuccess, setIsSuccess] = useState(false);
    const [isSearchActive, setIsSearchActive] = useState(true);

    const handleTabChange = (tab) => {
        setActiveTab(tab);
    };

    const handleIdButtonClick = () => {
        setIsSearchActive(false);
        setActiveTab("Id");
    };

    const handlePwButtonClick = () => {
        setIsSearchActive(false);
        setActiveTab("Pw");
    };
    const resetclose = () => {
        setIsSearchActive(true);
        setActiveTab("");
    }

    return (
        <div className="search-wrap" style={{display: isSuccess ? 'block' : 'none'}}>
            <div className="search-box">
                {isSearchActive ? (<div className="button-type">
                    <button onClick={() => handleTabChange("Id")} className="find-button">아이디 찾기</button>
                    <button onClick={() => handleTabChange("Pw")} className="find-button">비밀번호 찾기</button>
                </div>) : null}

                {activeTab === "Id" ? <FindId /> : null}
                {activeTab === "Pw" ? <FindPw /> : null}

                <div className="closeBtn2">
                    <div className="inner" onClick={resetclose}>
                        <label>Back</label>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default FindType;
