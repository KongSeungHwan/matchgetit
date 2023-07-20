import React, { useEffect, useState } from 'react';
import WeatherDetails from "./WeatherDetails";

const Weather = ({handleWeather, weather}) => {
    let [currentCity, setCurrentCity] = useState('');
    const [weatherInfo, setWeatherInfo] = useState('');

    const openWeatherApiKey = process.env.REACT_APP_OPEN_WEATHER_CLIENT_ID;
    const kakaoMapApiKey = process.env.REACT_APP_KAKAO_MAP_CLIENT_ID;

    useEffect(() => {
        const fetchWeatherData = async () => {
            try {
                const position = await getCurrentPosition();
                const { latitude, longitude } = position.coords;
                const currentAddress = await getCurrentAddress(latitude, longitude);
                const weatherData = await fetchWeather(currentAddress);
                const { weatherCode, temperature, rain1h, rainProbability, humidity } = weatherData;
                const translatedWeather = translateWeatherCode(weatherCode);
                let weatherInfo = `날씨: ${translatedWeather}, 온도: ${temperature}°C`;

                if (rain1h) {
                    weatherInfo += `강수량: ${rain1h}mm`;
                }
                if (rainProbability) {
                    weatherInfo += `, 3시간 강수량: ${rainProbability}mm`;
                }
                if (humidity) {
                    weatherInfo += `, 습도: ${humidity}%`;
                }

                setWeatherInfo(weatherInfo);
            } catch (error) {
                console.log(error);
            }
        };

        fetchWeatherData();
    }, []);

    const getCurrentPosition = () => {
        return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(resolve, reject);
        });
    };

    const getCurrentAddress = async (latitude, longitude) => {
        try {
            const response = await fetch(`https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${longitude}&y=${latitude}`, {
                headers: {
                    Authorization: `KakaoAK ${kakaoMapApiKey}`,
                },
            });
            const data = await response.json();
            const region1depthName = data?.documents[0]?.region_1depth_name || '';
            const region2depthName = data?.documents[0]?.region_2depth_name || '';
            const region3depthName = data?.documents[0]?.region_3depth_name || '';
            setCurrentCity(`현재 위치: ${region1depthName} ${region2depthName} ${region3depthName}`);
            return region2depthName;
        } catch (error) {
            throw new Error('Failed to get current city');
        }
    };

    const fetchWeather = (city) => {
        const weatherUrl = `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${openWeatherApiKey}`;
        return fetch(weatherUrl)
            .then(response => response.json())
            .then(data => {
                const weatherCode = data.weather[0].id;
                const temperature = (data.main.temp - 273.15).toFixed(1);
                const rain1h = data.rain && data.rain["1h"];
                const rain3h = data.rain && data.rain["3h"];
                const humidity = data.main.humidity;
                return { weatherCode, temperature, rain1h, rainProbability: rain3h, humidity };
            });
    };

    const translateWeatherCode = (weatherCode) => {
        if (weatherCode >= 200 && weatherCode < 300) {
            handleWeather('thunder');
            return "천둥번개";
        } else if (weatherCode >= 300 && weatherCode < 400) {
            handleWeather('rainy');
            return "☔︎이슬비";
        } else if (weatherCode >= 500 && weatherCode < 600) {
            handleWeather('rainy');
            return "☔︎흐림";
        } else if (weatherCode >= 600 && weatherCode < 700) {
            handleWeather('snowy');
            return "❄︎눈";
        } else if (weatherCode >= 700 && weatherCode < 800) {
            handleWeather('fog');
            return "🌫약간 흐림";
        } else if (weatherCode === 800) {
            handleWeather('sunny');
            return "☀맑음";
        } else if (weatherCode > 800) {
            handleWeather('fog');
            return "☁️구름 많음";
        } else {
            return "날씨 정보 없음";
        }
    };

    return (
        <div>
            <WeatherDetails weather={weather}/>
            <div className="currentCity">{currentCity}</div>
            <div className="weatherInfo">{weatherInfo}</div>
        </div>
    );
};

export default Weather;
