import React, { useEffect, useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../../styles/MatchingPage/History/MatchHistory.css';
import moment from "moment";
import axiosInstance from "../axiosInstance";

function MatchHistory({ session }) {
    const [value, onChange] = useState(new Date());
    const [date, setDate] = useState('');
    const [matchHistory, setMatchHistory] = useState([]);

    const handleDateClick = (date) => {
        const selectedDate = moment(date).format('YYYY-MM-DD');
        setDate(selectedDate);
        axiosInstance.post("/matchGetIt/match/matchHistory", null, { params: { userId: session.userId, date: selectedDate } })
            .then(res => {
                setMatchHistory(res.data);
            }).catch(err => {
            console.log("실패");
        });
    };

    useEffect(() => {
        handleDateClick(value);
    }, []);

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

    const renderResult = (team, score, etc) => {
        if (score === ':') {
            return (
                <>
                    취소
                </>
            );
        } else {
            const [teamAScore, teamBScore] = score.split(':');
            const teamAScoreInt = parseInt(teamAScore);
            const teamBScoreInt = parseInt(teamBScore);

            let result = '';
            let teamScore = '';

            if (team === 'A') {
                if (teamAScoreInt > teamBScoreInt) {
                    result = '승리';
                } else if (teamAScoreInt < teamBScoreInt) {
                    result = '패배';
                } else {
                    result = '무승부';
                }
                teamScore = `${teamAScoreInt} : ${teamBScoreInt}`;
            } else if (team === 'B') {
                if (teamBScoreInt > teamAScoreInt) {
                    result = '승리';
                } else if (teamBScoreInt < teamAScoreInt) {
                    result = '패배';
                } else {
                    result = '무승부';
                }
                teamScore = `${teamBScoreInt} : ${teamAScoreInt}`;
            }

            return (
                <>
                    <div style={{whiteSpace:'nowrap'}}>{result}</div>
                    <div style={{whiteSpace:'nowrap'}}>({teamScore})</div>
                </>
            );
        }
    };

    return (
        <div>
            <div className="basicDiv">
                <Calendar
                    onChange={onChange}
                    value={value}
                    locale="en-EN"
                    className="calendar"
                    onClickDay={handleDateClick} // 날짜를 클릭했을 때 호출될 함수
                />
            </div>
            <div className="matchHistoryContainer">
                <hr />
                <div>내 매칭 내역</div>
                <span className="title">{date}</span>
                <table className={`manager-match ${matchHistory.length > 0 ? "fade-in" : "fade-out"}`}>
                    <colgroup></colgroup>
                    <thead>
                    <tr>
                        <th>시간</th>
                        <th>장소</th>
                        <th>결과</th>
                        <th>세부사항</th>
                    </tr>
                    </thead>
                    <tbody>
                    {matchHistory.map((history, i) => (
                        <tr key={i}>
                            <th>{renderTime(history?.applicationTime)}</th>
                            <th>{history.stadium.stdName}</th>
                            <th>{renderResult(history?.team, history.matchScore)}</th>
                            <th>{history.matchScore === ':' ? history?.etc : null}</th>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default MatchHistory;
