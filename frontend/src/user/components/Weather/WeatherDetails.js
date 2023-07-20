import React from 'react';
import '../../styles/Weather/weatherDetails.css'

const WeatherDetails = ({weather}) => {
    console.log(weather);
    return (
        <div>
            <div className="container">
                <div className={`${weather}`}></div>
                {/*<div className="fog"></div>*/}
                {/*<div className="rainy"></div>*/}
                {/*<div className="snowy"></div>*/}
                {/*<div className="thunder"></div>*/}
            </div>
        </div>
    );
}

export default WeatherDetails;
