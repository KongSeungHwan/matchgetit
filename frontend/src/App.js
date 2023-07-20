import React, { useState } from 'react';
import Header from "./user/components/Header";
import Main from "./user/components/Main";
import WeatherMenu from "./user/components/Weather/WeatherMenu";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
    };

    return (
        <div className="appCon">
            <WeatherMenu/>
            <Header isLoggedIn={isLoggedIn} onLogout={handleLogout} />
            <Main isLoggedIn={isLoggedIn} onLogin={handleLogin} onLogout={handleLogout} />
        </div>
    );
}

export default App;
