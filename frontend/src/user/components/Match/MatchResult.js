import React from 'react';
import '../../styles/MatchingPage/Wait/waitingPage.css';
import '../../styles/MatchingPage/MatchingResult/matchingResult.css';
import '../../styles/CommonFactor/grade.css';
import axiosInstance from "../axiosInstance";

const MatchResult = ({ session, matchWaitData, setIsMatch, setMatchWaitData, party }) => {
    if (matchWaitData === null) {
        return (
            <>잘못된 접근입니다.</>
        );
    }

    // applicationTime에 따라 한글로 표현
    let applicationTimeText = '';
    switch (matchWaitData[0].party?.applicationTime) {
        case 'A':
            applicationTimeText = '오전 10시 ~ 오후 12시';
            break;
        case 'B':
            applicationTimeText = '오후 12시 ~ 오후 2시';
            break;
        case 'C':
            applicationTimeText = '오후 2시 ~ 오후 4시';
            break;
        case 'D':
            applicationTimeText = '오후 4시 ~ 오후 6시';
            break;
        case 'E':
            applicationTimeText = '오후 6시 ~ 오후 8시';
            break;
        case 'F':
            applicationTimeText = '오후 8시 ~ 오후 10시';
            break;
        default:
            applicationTimeText = '';
            break;
    }

    const cancelMatch = () => {
        axiosInstance.post("/matchGetIt/match/cancelMatchWait", null, { params: { id: session.userId } })
            .then(response => {
                console.log('취소 성공');
                window.location.reload();
            }).catch(error => {
            console.log('취소 실패');
        });
    };

    const renderTeamMembers = (team) => {
        const members = matchWaitData.filter(m => m.team === team);
        return members.map((item, index) => {
            let iconUrl = '';

            switch (item.member.prfcn) {
                case 'BEGINNER':
                    iconUrl = process.env.PUBLIC_URL + '/images/beginner.png';
                    break;
                case 'MIDDLE':
                    iconUrl = process.env.PUBLIC_URL + '/images/middle.png';
                    break;
                case 'ADVANCED':
                    iconUrl = process.env.PUBLIC_URL + '/images/advanced.png';
                    break;
                case 'PROFESSIONAL':
                    iconUrl = process.env.PUBLIC_URL + '/images/professional.png';
                    break;
                default:
                    break;
            }

            return (
                <div key={index} className={`grade`}>
                    <img src={iconUrl} alt={item.member.prfcn} className={`icon ${item.member.prfcn}`} />
                    <div className={`matchTeamMember gradeMember ${item.member.prfcn}`}>
                        <div className="gradeName">{item.member.name}</div>
                    </div>
                </div>
            );
        });
    };

    return (
        <div className="backgroundArea" id={`${matchWaitData[0].party?.gameType}`}>
            <div className='resultContainer'>

                <div className="resultStatiumimg">
                    {/*<img src={matchWaitData.stadium.stdImgUrl} alt="Stadium" />*/}
                </div>
                <div className="result resultTime">매칭된 시간: {matchWaitData[0].party?.applicationDate} <br/> {applicationTimeText}</div>
                <div className="result resultStadium">매칭 구장: {matchWaitData[0].stadium.stdName}</div>
                <div className="result lineUp">Line Up</div>
                <div className="teamView">
                    <div className="aTeam">
                        <div className="uniform">
                            <img className="uniformImg uniformImgA" src="/images/uniformA.png" alt="Ateam" />
                        </div>
                        <div className="resultMember">
                            <div className="teamlist">{renderTeamMembers("A")}</div>
                        </div>
                    </div>
                    <div className="bTeam">
                        <div className="uniform">
                            <img className="uniformImg uniformImgB" src="/images/uniformB.png" alt="Bteam" />
                        </div>
                        <div className="resultMember">
                            <div className="teamlist">{renderTeamMembers("B")}</div>
                        </div>
                    </div>
                </div>
                <button type="button" className="button cancelButton" onClick={cancelMatch}>경기 취소</button>
            </div>
        </div>
    );
};

export default MatchResult;
