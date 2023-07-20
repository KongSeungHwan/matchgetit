import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ManagerRecruitment.css';

function ManagerRecruitment({ session }) {
  const [showPopup, setShowPopup] = useState(false);
  const [activityZone, setActivityZone] = useState(""); // 활동권역 상태값
  const [showActivityZonePopup, setShowActivityZonePopup] = useState(false); // 활동권역 선택 안내 팝업 상태값
  const [showDuplicatePopup, setShowDuplicatePopup] = useState(false); // 중복 체크 팝업 상태값
  console.log(">>>>>>>>>>>세션", session);
  const [userName, setUserName] = useState(session.name);
  const [phoneNumber, setPhoneNumber] = useState(session.pn);
  const [gender, setGender] = useState(session.gender);

  // 사용자 정보 및 매니저 지원 상태 업데이트 함수
    const updateUserAndSubmitForm = () => {
      if (activityZone === "") {
        setShowActivityZonePopup(true); // 활동권역 선택 안내 팝업 표시
        setTimeout(() => {
          setShowActivityZonePopup(false); // 일정 시간 후 팝업 사라짐
        }, 10000);
        return;
      }

      const formData = {
        activityZone: activityZone,
      };

      axios
        .post(`/matchGetIt/manager/submitForm/${session.userId}`, formData)
        .then((response) => {
          console.log('Form submitted:', response.data);
          if (response.data === '매니저 지원이 완료되었습니다.') {
            setShowPopup(true); // 지원 완료 팝업 표시
            setTimeout(() => {
              setShowPopup(false); // 일정 시간 후 팝업 사라짐
            }, 10000);
          }
        })
        .catch((error) => {
          console.error('Failed to submit form:', error);
          if (error.response && error.response.status === 400) {
            setShowActivityZonePopup(true); // 활동권역 선택 안내 팝업 표시
            setTimeout(() => {
              setShowActivityZonePopup(false); // 일정 시간 후 팝업 사라짐
            }, 10000);
          } else if (error.response && error.response.status === 500) {
            setShowDuplicatePopup(true); // 이미 지원된 지원자 팝업 표시
            setTimeout(() => {
              setShowDuplicatePopup(false); // 일정 시간 후 팝업 사라짐
            }, 10000);
          }
        });
    };


  return (
    <div>
      {/* 지원서 폼 */}
      <div className="ApplyManager">
        <form onSubmit={updateUserAndSubmitForm}>
          <div style={{ marginBottom: '15px' }}>
            <h3 className="form-title">지원 양식</h3>
          </div>

          <div className="form-row">
            <label htmlFor="name">성함: </label>
            <input
              type="text"
              id="name"
              name="name"
              value={userName}
              readOnly
            />
          </div>
          <div className="form-row">
            <label htmlFor="pn">연락처: </label>
            <input
              type="text"
              id="pn"
              name="pn"
              value={phoneNumber}
              readOnly
            />
          </div>
          <div className="form-row">
            <label htmlFor="gender">성별: </label>
            <input
              type="text"
              id="gender"
              name="gender"
              value={gender}
              readOnly
            />
          </div>

          <br />
          <div className="form-row">
            <label htmlFor="region">활동권역: </label>
            <select
              id="region"
              name="region"
              value={activityZone}
              onChange={(e) => setActivityZone(e.target.value)}
            >
              <option value="">선택하세요</option>
              <option value="서울">서울</option>
              <option value="경기">경기</option>
              <option value="인천">인천</option>
            </select>
            <br />
            <br />
          </div>
          <div className="form-row form-row-button">
            <button type="button" onClick={updateUserAndSubmitForm}>
              제출하기
            </button>
          </div>
        </form>
      </div>

      {/* 중복 체크 팝업 */}
      {showDuplicatePopup && (
        <div className="popup">
          <p>이미 지원된 지원자입니다.</p>
        </div>
      )}

      {/* 활동권역 선택 안내 팝업 */}
      {showActivityZonePopup && (
        <div className="popup">
          <p>활동권역이 선택되지 않았습니다. 선택해주세요.</p>
        </div>
      )}

      {/* 지원 완료 팝업 */}
      {showPopup && (
        <div className="popup">
          <p>지원이 완료되었습니다. 합격자 한으로 추후에 개별 연락드리겠습니다. 감사합니다.</p>
        </div>
      )}
    </div>
  );
}

export default ManagerRecruitment;
