import TopBarButton from "./top-bar/TopBarButton";

const TopBar = () => {
    return (
        <div className="flex flex-row w-full gap-2 bg-gray-800">
            <div className="top-bar w-full h-20 flex items-center justify-start pl-5">
                <h1 className="text-4xl text-white">Follow the beat..</h1>
            </div>
            <div className="flex flex-row items-center justify-center gap-2 ">
                <TopBarButton title="Home" onClick={() => {}}/>
                <TopBarButton title="Profile" onClick={() => {}}/>
                <TopBarButton title="My Lineup" onClick={() => {}}/>
            </div>
        </div>
        
    );
}

export default TopBar;