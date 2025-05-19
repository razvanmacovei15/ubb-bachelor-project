import {Outlet} from "react-router-dom";
import TopBar from "./components/top-bar/TopBar";
import "./styles/global.css";
import "./index.css";
import {UserProvider} from "./contexts/UserContext";

function App() {
    return (
        <div className="app">
            <TopBar/>
            <main className="app-main-content">
                <Outlet/>
            </main>
        </div>
    );
}

export default App;
