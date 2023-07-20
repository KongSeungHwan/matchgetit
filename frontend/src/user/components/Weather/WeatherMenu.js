import React, { useState } from "react";
import "../../styles/Weather/Weather.css";
import Weather from "./Weather";

function WeatherMenu() {
    const [isClicked, setIsClicked] = useState(false);
    const [isActive, setIsActive] = useState(false);
    const [weather, setWeather] = useState('');

    const handleWeather=(weather)=>{
        setWeather(weather);
    }

    const handleClick = () => {
        setIsClicked(true);
        setTimeout(() => setIsActive(true), 900);
    };

    const handleClose = () => {
        setIsActive(false);
        setTimeout(() => setIsClicked(false), 300);
    };

    const getImagePath = () => {
        let imgUrl = '';
        switch (weather) {
            case 'sunny':
                imgUrl = process.env.PUBLIC_URL + '/images/Weather/sunny.png';
                break;
            case 'cloudy':
                imgUrl = process.env.PUBLIC_URL + '/images/Weather/cloudy.png';
                break;
            case 'rainy':
                imgUrl = process.env.PUBLIC_URL + '/images/Weather/rainy.png';
                break;
            case 'fog':
                imgUrl = process.env.PUBLIC_URL + '/images/Weather/fog.png';
                break;
            default:
                return (<></>);
        }

        return (<img src={imgUrl} className="weatherImg" alt='날씨' />);
    };

    return (
        <div className="pool">
            <div className={`button-wrapper${isClicked ? " clicked" : ""}`}>
                <div className="layer"></div>
                <button className="wBtn main-button fa fa-plus" onClick={handleClick}>
                    <div className="ripple">
                        {getImagePath()}
                    </div>
                </button>
            </div>
            <div className={`layered-content${isActive ? " active" : ""}`}>
                <button className="wBtn close-button close-button1 fa fa-times" onClick={handleClose}>X</button>
                <div className="content">
                    <Weather handleWeather={handleWeather} weather={weather} />
                </div>
            </div>
        </div>
    );
}

export default WeatherMenu;
