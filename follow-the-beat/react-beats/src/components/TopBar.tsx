import { useNavigate } from "react-router-dom";
import TopBarButton from "./top-bar/TopBarButton";

const TopBar = () => {
    const navigate = useNavigate(); 

    return (
        <div className="flex flex-row w-full gap-2 bg-gray-800">
            <div className="top-bar w-full h-20 flex items-center justify-start pl-5">
                <h1 className="text-4xl text-white">Follow the beat..</h1>
            </div>
            <div className="flex flex-row items-center justify-center gap-2 ">
                <TopBarButton title="Home" onClick={() => {
                    navigate("/");
                }}/>
                <TopBarButton title="Events" onClick={() => {
                    navigate("/concerts");
                }}/>
                <TopBarButton title="Profile" onClick={() => {
                    navigate("/profile");
                }}/>
                <TopBarButton title="My Lineup" onClick={() => {
                    navigate("/lineup");
                }}/>
            </div>
        </div>
        
    );
}

export default TopBar;