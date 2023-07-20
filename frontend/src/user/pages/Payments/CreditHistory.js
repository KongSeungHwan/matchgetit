import React, {useEffect, useState} from 'react';
import '../Payments/CreditHistory.css';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import moment from "moment";
import axios from "axios";


function CreditHistory({ session }) {
    const [value, onChange] = useState(new Date());
    const [selectedDateContent, setSelectedDateContent] = useState([]);

    const handleDateClick = (date) => {
        const selectedDate = moment(date).format('YYYY-MM-DD');
        console.log(session.userId);

        axios
            .post('/matchGetIt/payment/userPayHistory', null, {
                params: { userId: session.userId, date: selectedDate }
            })
            .then((response) => {
                console.log(response.data);
                setSelectedDateContent(response.data);
            })
            .catch((error) => {
                console.error('에러:', error);
            });
    };

    useEffect(() => {
        handleDateClick(value); // Simulate initial request for today's date
    }, []); // Empty dependency array to execute the effect only once when the component mounts

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
                <div className={`${selectedDateContent.length > 0 ? "fade-in" : "fade-out"}`}>
                    {selectedDateContent.length > 0 && (
                        <>
                            {selectedDateContent.map((content, index) => (
                                <div className="CreditHistory" key={index}>
                                    <p>{content.transactionDate}</p>
                                    {content.price > 0 ? (
                                        <p>포인트 충전</p>
                                    ) : (
                                        <p>매칭 결제</p>
                                    )}
                                    <h3>{content.price}원</h3>
                                </div>
                            ))}
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default CreditHistory;
