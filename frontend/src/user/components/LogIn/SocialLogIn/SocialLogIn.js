import React from 'react';
import '../../../styles/LogIn/socialButton.css';

const SocialLogIn = () => {
    const handleKakaoButtonClick = () => {
        const clientId = process.env.REACT_APP_KAKAO_CLIENT_ID;

        const authorizeUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=http://localhost:8081/matchGetIt/kakao&response_type=code`;
        window.location.href = authorizeUrl;
    };
    const handleGoogleBtnClick = () => {
        const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;
        const redirectUri = 'http://localhost:8081/matchGetIt/google';

        const authorizeUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${encodeURIComponent(
            redirectUri
        )}&response_type=code&scope=email profile`;

        window.location.href = authorizeUrl;
    }
    const handleNaverButtonClick = () => {
        const clientId = process.env.REACT_APP_NAVER_CLIENT_ID;
        const redirectUri = 'http://localhost:8081/matchGetIt/naver'; // 콜백 주소를 적절히 수정해주세요

        const authorizeUrl = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${clientId}&redirect_uri=${encodeURIComponent(
            redirectUri
        )}&state=state`;
        window.location.href = authorizeUrl;
    };

    return (
        <>
            <button className="socialButton kakaoButton" onClick={handleKakaoButtonClick}>Kakao
            </button>
            <button className="socialButton googleButton" onClick={handleGoogleBtnClick}>Google
            </button>
            <button className="socialButton naverButton" onClick={handleNaverButtonClick}>Naver
            </button>
        </>
    );
};

export default SocialLogIn;
