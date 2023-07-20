import React, { useEffect, useState } from "react";
import "../../styles/ManagerPage/ManagerViewDetails.css";
import axiosInstance from "../../components/axiosInstance";
import '../../styles/CommonFactor/MngRadio.css';

const MngViewDetails = ({ selectTime, session, selectDate,isSuccess,setIsSuccess, schedule,stadium }) => {
    const [scoreA, setScoreA] = useState("");
    const [scoreB, setScoreB] = useState("");
    const [etc, setEtc] = useState("");
    const [statusMsg, setStatusMsg] = useState("");
    const [matchData, setMatchData] = useState([]);
    const [mode, setMode]=useState(true);

    useEffect(() => {
        axiosInstance
            .post("/matchGetIt/manager/getMatchDetails", null, {
                params: { userId: session.userId, date: selectDate, time: selectTime },
            })
            .then((res) => {
                console.log("매치 매니저 데이터");
                console.log(res.data);
                setMatchData(res.data);
            })
            .catch((err) => {
                console.log("서버 오류");
            });
    }, []);

    const submitMatchData = () => {
        if ((scoreA == null && scoreB == null) || (scoreA != null && scoreB != null)) {
            // 경기 이행x
            axiosInstance
                .post("/matchGetIt/manager/matchEnd", null, {
                    params: { userId: session.userId, date: selectDate, time: selectTime, score: scoreA + ":" + scoreB, etc: etc },
                })
                .then((res) => {
                    setStatusMsg("성공");
                    setTimeout(() => {
                        setStatusMsg("");
                        setIsSuccess(false);
                    }, 3000);
                });
        } else {
            setStatusMsg("점수 기입이 제대로 되어있지 않습니다.");
            setTimeout(() => {
                setStatusMsg("");
            }, 3000);
        }
    };

    const handlePopupCloseClick = () => {
        setIsSuccess(false);
    };
    const handelMode = (value) =>{
        setMode(value === "complete");
    }
    const renderTime = (time) => {
        let renderTime = '';
        switch (time) {
            case 'A':
                renderTime = '10시 ~ 12시';
                break;
            case 'B':
                renderTime = '12시 ~ 14시';
                break;
            case 'C':
                renderTime = '14시 ~ 16시';
                break;
            case 'D':
                renderTime = '16시 ~ 18시';
                break;
            case 'E':
                renderTime = '18시 ~ 20시';
                break;
            case 'F':
                renderTime = '20시 ~ 22시';
                break;
            default:
                renderTime = '';
                break;
        }
        return renderTime;
    };

    return (
        <div className="details-wrap" style={{ display: isSuccess ? "block" : "none" }}>
            <div className="details-box">
                <div className="details-title">경기 상세정보</div>
                <div className="detailsContainer">
                    <div className="match-information">
                        <div className="match-data">{selectDate} {renderTime(selectTime)} {stadium.stdName}</div>
                    </div>
                    <div className="radioCon">
                        <div className="tabs">
                            <input
                                type="radio"
                                className="mode"
                                id="completeMatch"
                                name="tabs"
                                value="complete"
                                defaultChecked
                                onChange={(e) => handelMode(e.target.value)}
                            />
                            <label className="tab" htmlFor="completeMatch">Complete</label>
                            <input
                                type="radio"
                                className="mode"
                                id="cancelMatch"
                                name="tabs"
                                value="cancel"
                                onChange={(e) => handelMode(e.target.value)}
                            />
                            <label className="tab" htmlFor="cancelMatch">Cancel</label>
                            <span className="glider"></span>
                        </div>
                    </div>
                    {mode?(
                    <div className="Team-information">
                        <div className="Team-name" style={{ textAlign: "center"}}>
                            A팀
                            <input
                                type="text"
                                className="input_score"
                                placeholder="점수"
                                value={scoreA}
                                onChange={(e) => setScoreA(e.target.value)}
                            ></input>
                            <div className="Team_A" id="Team_A">
                                {matchData.filter((e) => e.team === "A").map((m, index) => (
                                    <div key={index}>
                                        <p>
                                            {m.member.name}({m.member.prfcn})
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <div className="Team-name" style={{ textAlign: "center" }}>
                            B팀
                            <input
                                type="text"
                                className="input_score"
                                placeholder="점수"
                                value={scoreB}
                                onChange={(e) => setScoreB(e.target.value)}
                            ></input>
                            <div className="Team_B" id="Team_B">
                                {matchData.filter((e) => e.team === "B").map((m, index) => (
                                    <div key={index}>
                                        <p>
                                            {m.member.name}({m.member.prfcn})
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                    ):(
                    <div className="match_note_input">
                <textarea
                    rows="4"
                    cols="50"
                    className="input_note"
                    placeholder="취소 사유"
                    value={etc}
                    onChange={(e) => setEtc(e.target.value)}
                ></textarea>
                    </div>

                    )}
                    <div className="button_container">
                        <button className="enter_button" onClick={submitMatchData}>
                            완료
                        </button>
                        <p>{statusMsg}</p>
                    </div>
                </div>
                <div className="closeBtn3" onClick={handlePopupCloseClick}>
                    <div className="inner">
                        <label>Back</label>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MngViewDetails;