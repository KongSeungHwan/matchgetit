import React, {useEffect, useState} from 'react';
import '../../styles/ManagerPage/ManagerGameMg.css'
import MngViewDetails from './MngViewDetails';
import '../../styles/ManagerPage/ManagerViewDetails.css';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

import moment from "moment/moment";
import axiosInstance from "../../components/axiosInstance";


const ManagerMatchPage = ({session,isMngMenuOpen,handleMngMenuToggle}) => {

    const [isSuccess, setIsSuccess] = useState(false);
    const [value, onChange] = useState(new Date());
    const [dateData, setDateData] = useState([]);
    const [marks, setMarks] = useState([]);
    const [selectDate, setSelectDate] = useState('');
    const [isOpenDetails , setIsOpenDetails] = useState(false);
    const [stadium, setStadium] = useState(null);

    useEffect(() => {
        getStadium();
    }, []);

    const handlePopupBtnClick = () => {
        setIsSuccess(true);
    };
    const getStadium = () =>{
        axiosInstance.post("matchGetIt/manager/getStadium",null,{params:{mngId:session.userId}})
            .then(res=>{
                console.log(res.data);
                if(res.data !==null) setStadium(res.data);
            })
            .catch(err=>{
                console.log(err.data);
            })
    }


    const handleDateClick = (date) => {
        const selectedDate = moment(date).format('YYYY-MM-DD');
        console.log(selectedDate);
        setSelectDate(selectedDate);

        //수정 파트
        axiosInstance.post("/matchGetIt/manager/getMatchList",null,{params:{userId:session.userId,date:selectedDate}})
            .then(res=>{
                setDateData(res.data);
            }).catch(err=>{
                console.log('서버 오류!');
        });


    };

    const tileClassName = ({date, view}) => {
        if (marks.find((x) => x === moment(date).format("YYYY-MM-DD"))) {
            return 'highlight';
        }
        return '';
    };
    const schedule = (mngSchedule) => {

        let applicationTimeText = '';

        switch (mngSchedule) {
            case 'A':
                applicationTimeText = '10시 ~ 12시';
                break;
            case 'B':
                applicationTimeText = '12시 ~ 14시';
                break;
            case 'C':
                applicationTimeText = '14시 ~ 16시';
                break;
            case 'D':
                applicationTimeText = '16시 ~ 18시';
                break;
            case 'E':
                applicationTimeText = '18시 ~ 20시';
                break;
            case 'F':
                applicationTimeText = '20시 ~ 22시';
                break;
            default:
                applicationTimeText = '';
                break;
        }
        return applicationTimeText;
    }
   const handleDetail = () =>{
       setIsOpenDetails(!isOpenDetails);
    }

    return (
        <>
                <div className="MngPage-container">
                    <h3>경기 관리</h3>
                    <div className="record_controller">
                        <div>
                            <div className="basicDiv">
                                <Calendar
                                    onChange={onChange}
                                    value={value}
                                    locale="en-EN"
                                    className="calendar"
                                    tileClassName={tileClassName}
                                    onClickDay={handleDateClick} // 날짜를 클릭했을 때 호출될 함수
                                />

                                <table className={`manager-match ${dateData.length>0 ? "fade-in" : "fade-out"}`}>
                                    <colgroup></colgroup>
                                    <thead>
                                    <tr>
                                        <th>번호</th>
                                        <th>날짜</th>
                                        <th>시간</th>
                                        <th>상세보기</th>

                                    </tr>
                                    </thead>
                                    <tbody>
                                    {dateData.map((con,index)=>
                                    <tr key={index}>
                                        <th>{index+1}</th>
                                        <td>{selectDate}</td>
                                        <td>{schedule(con)}</td>
                                        <td>
                                            <div>
                                                <button className="view" onClick={handlePopupBtnClick}>상세보기</button>
                                                <MngViewDetails isSuccess={isSuccess} setIsSuccess={setIsSuccess} selectTime={con} session={session} selectDate={selectDate} stadium={stadium} handleDetail={handleDetail}/>
                                            </div>
                                        </td>
                                    </tr>
                                    )}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
        </>
    );
};

export default ManagerMatchPage;