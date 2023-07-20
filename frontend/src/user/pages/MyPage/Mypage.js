import React, {useEffect, useState} from "react";
import "./Mypage.css";
import "./profile.css";
import "../Payments/CreditCharge.css";
import Profile from "./Profile";
import CreditHistory from "../Payments/CreditHistory";
import CreditCharge from "../Payments/CreditCharge";
import ApplyManager from "../ManagerRecruitment/ApplyManager";
import axiosInstance from "../../components/axiosInstance";
import QABoard from "../QABoard/QABoard";
import ManagerMatchPage from "../ManagerPage/ManagerMatchPage";
import MatchHistory from "../../components/Match/MatchHistory";
import '../../styles/ManagerPage/ManagerGameMg.css';
import '../../styles/CommonFactor/grade.css';



function Mypage({session,logout}) {
    const [isProfileOpen, setProfileOpen] = useState(false);
    const [isChargeOpen, setChargeOpen] = useState(true);
    // const [isAdmin, setIsAdmin] = useState(session && session.loginType.toUpperCase() === "ADMIN");
    const [isAdmin, setIsAdmin] = useState(false);
    const [isMng, setIsMng] = useState(false);

    //임의로 매니저 true로 해놨음 원래로직대로면 매니저신청 >> 관리자 등록 >> role체크 후 true로 바뀌어야 함
    const [isApplyManager, setApplyManager] = useState(false);
    const [isMatchHistoryOpen, setIsMatchHistoryOpen] = useState(false);
    const [isHistoryOpen, setHistoryOpen] = useState(false);
    const [isQABoardOpen, setQABoardOpen] = useState(false);
    const [isMngMenuOpen, setIsMngMenuOpen] = useState(false);
    const [matchCount, setMatchCount] = useState(0);

    useEffect(() => {
        axiosInstance.post("/matchGetIt/match/getMatchCount")
            .then(res => {
                const count = Number(res.data);
                setMatchCount(count);
            })
            .catch(err => {
                console.log('서버 오류');
            });
    }, []);
    useEffect(() => {
        setIsAdmin(session && session.loginType.toUpperCase() === "ADMIN");
        setIsMng(session && session.loginType.toUpperCase() === "MANAGER");
    }, [session])

    const handleApplyManagerToggle = () => {
        setApplyManager(!isApplyManager);
    };

    const handleProfileToggle = () => {
        setProfileOpen(!isProfileOpen);
    };
    const handleChargeToggle = () => {
        setChargeOpen(!isChargeOpen);
    };
    const handleMatchHistoryToggle = () => {
        setIsMatchHistoryOpen(!isMatchHistoryOpen);
    };
    const handleHistoryToggle = () => {
        setHistoryOpen(!isHistoryOpen);
    };
    const handleQABoardToggle = () => {
        setQABoardOpen(!isQABoardOpen);
    };

    const handleMngMenuToggle = () => {
        setIsMngMenuOpen(!isMngMenuOpen);
    };

    const renderGradeImage = ()=>{
        let iconUrl='';

        switch (session.prfcn) {
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
        return iconUrl;
    }
    const renderState = () => {
        let matchStatus = '';

        if (matchCount >= 0 && matchCount < 100) {
            matchStatus = '😀 원활';
        } else if (matchCount >= 100) {
            matchStatus = '😐 혼잡';
        } else {
            matchStatus = '😡 서버 오류';
        }

        return matchStatus;
    };

    return (
        <div className="content-body_left-wrap">
            <section>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <div className="my-profile">
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            <h1 className="my-profile__name">{session.name}</h1>
                        </div>
                        <div className="my-account_type">
                            <p className="text-caption1">{session.userId}</p>
                            <span className="badge badge-kakao sm">{session.accountType}</span>
                        </div>
                    </div>
                </div>
                <div style={{ marginTop: '10px', display: 'none' }}>
                    <p className="my-profile__notice--friend">
                        <a href="/player/17AcO/friend/?tab=pending">0명의 친구 신청이 있어요</a>
                    </p>
                </div>
                <div className="my-status">
                    <li className="my-status_item my-status_item-double">
                        <div className="my-status_label">
                            <p style={{ fontSize: '12px' }}>매칭 서버</p>
                        </div>
                        <div className="my-status_content">
                            <div>{renderState()}</div>
                        </div>
                    </li>
                    <li className="my-status_item my-status_item-double">
                        <div className="my-status_label">
                            <p style={{ fontSize: '12px' }}>숙련도</p>
                        </div>
                        <div className="my-status_content">
                            <div className="lv-system_title">
                                <div className="lv-system_name">
                                    <h4>{session.prfcn}</h4>
                                </div>
                                <div className="prfcnArea">
                                    <img className="myPageIcon" src={renderGradeImage()} alt="레벨" />
                                </div>
                                <div>
                                </div>
                            </div>
                        </div>
                        {/*</a>*/}
                    </li>
                </div>
                {isChargeOpen && (
                    <li className="my-status_item my-status_item-cash">
                        <div>
                            <p style={{ fontSize: '17px', fontWeight: "bold"}}>나의 캐시</p>
                            <p style={{ fontSize: '20px', fontWeight: '700' }}>{session.ownedCrd!=null?session.ownedCrd:0}원</p>
                        </div>

                        <div className="my-cash">
                            <span onClick={handleChargeToggle}>충전하기</span>
                        </div>
                    </li>
                )}
                <div className={`my-status CreditCharge-settings ${!isChargeOpen ? "fade-in" : "fade-out"}`}>
                    {!isChargeOpen && (<div className="CreditCharge-settings">
                        <CreditCharge session={session} />
                        <div className="charge__back" onClick={handleChargeToggle}>
                            닫기
                        </div>
                    </div>)}
                </div>

                <div className="profile-view">
                    <button className="btn sm gray" onClick={handleProfileToggle}>
                        <p>프로필 {isProfileOpen?(<>닫기</>):(<>보기</>)}</p>
                    </button>
                </div>
                <div className={`profile-settings ${isProfileOpen ? "fade-in" : "fade-out"}`}>
                    {isProfileOpen && ( <Profile session={session} logout={logout} />)}
                </div>

            </section>
            <section>
                <div>
                    <div className="my-content-title">my content</div>
                    <div>
                        <div className="content-label" onClick={handleMatchHistoryToggle}>
                            <img src="https://plab-football.s3.amazonaws.com/static/img/ic_myplab_color.svg" alt="신청 내역"/>
                            <p>내 매치 내역</p>
                        </div>
                        <div className={`profile-settings ${isMatchHistoryOpen ? "fade-in" : "fade-out"}`} >
                            {isMatchHistoryOpen && (<MatchHistory session={session}/>)}
                        </div>
                    </div>
                    <div>
                        <div className="content-label" onClick={handleHistoryToggle}>
                            <img src="https://plab-football.s3.amazonaws.com/static/img/ic_point_color.svg" alt="결제 내역"/>
                            <p>결제 내역</p>
                        </div>
                        <div className={`profile-settings ${isHistoryOpen ? "fade-in" : "fade-out"}`} >
                            {isHistoryOpen && (<CreditHistory session={session}/>)}
                        </div>
                    </div>
                    <div>
                        <div className="content-label" onClick={handleQABoardToggle}>
                            <img src="https://plab-football.s3.amazonaws.com/static/img/ic_faq_color.svg" alt="문의 게시판" />
                            <p>문의 게시판</p>
                        </div>
                        <div className={`profile-settings ${isQABoardOpen ? "fade-in" : "fade-out"}`}>
                            {isQABoardOpen && (<QABoard session={session} />)}
                        </div>
                    </div>
                    {!isMng && (
                        <div>
                            <div className="content-label" onClick={handleApplyManagerToggle}>
                                <img src="https://plab-football.s3.amazonaws.com/static/img/ic_manager_color.svg" alt="결제 내역"/>
                                <p>매니저 지원하기</p>
                            </div>
                            <div className={`ApplyManager ${isApplyManager ? "fade-in" : "fade-out"}`}>
                                {isApplyManager && (<ApplyManager session={session}/>)}
                            </div>
                        </div>
                    )}
                    {isAdmin && (
                        <a href="http://localhost:8081/matchGetIt/admin/gate">
                            <div className="content-label">
                                <img src="https://plab-football.s3.amazonaws.com/static/img/ic_myplab_color.svg" alt="매니저 창" />
                                <p>어드민 페이지 이동</p>
                            </div>
                        </a>
                    )}
                    {isMng && (
                        <>
                            <div className="content-label mngMenu" onClick={handleMngMenuToggle}>
                                <p>💼&nbsp;&nbsp; 매니저 메뉴 {isMngMenuOpen?(<>닫기</>):(<>보기</>)}</p>
                            </div>
                            <div className={`profile-settings ${isMngMenuOpen ? "fade-in" : "fade-out"}`}>
                                {isMngMenuOpen && (
                                    <ManagerMatchPage session={session} isMngOpen={isMngMenuOpen} handleMngMenuToggle={handleMngMenuToggle}/>
                                )}
                            </div>
                        </>
                    )}
                </div>
            </section>
        </div>
    );
}


export default Mypage;