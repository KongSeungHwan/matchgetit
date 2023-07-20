import React from 'react';
import DaumPostcode from 'react-daum-postcode';
import axios from 'axios';


const AddressSearch = ({ onSelect, visible, setVisible}) => {

    const handleComplete = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';
        const clientId =process.env.REACT_APP_KAKAO_MAP_CLIENT_ID;

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
            }
            fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
        }

        setVisible(false);

        axios.get(`https://dapi.kakao.com/v2/local/search/address.json`, {
            params: {
                query: fullAddress
            },
            headers: {
                'Authorization': `KakaoAK ${clientId}`
            }
        }).then(response => {
            const { data } = response;
            if (data.documents[0]) {
                const { x, y } = data.documents[0];
                onSelect(fullAddress, y, x);
            } else {
                console.log('No matching address found');
            }
        }).catch(error => {
            console.log(error);
        });
    };

    return (
        <div>
            {visible && (
                <div>
                    <button className="button" title="닫기" onClick={() => setVisible(false)}>주소 창 닫기</button>
                    <div>
                        <DaumPostcode onComplete={handleComplete} height={700} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default AddressSearch;