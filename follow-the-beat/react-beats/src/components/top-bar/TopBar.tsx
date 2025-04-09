import { useNavigate } from "react-router-dom";
import TopBarButton from "./TopBarButton";
import "./TopBar.css";

const TopBar = () => {
  const navigate = useNavigate();

  return (
    <div className="top-bar">
      <div className="logo-container">
        <h1 className="text-4xl text-white ">Follow your beat</h1>
      </div>
      <div className="menu-container">
        <TopBarButton
          title="Home"
          onClick={() => {
            navigate("/");
          }}
        />
        <TopBarButton
          title="Concerts"
          onClick={() => {
            navigate("/concerts");
          }}
        />
        <TopBarButton
          title="My Lineup"
          onClick={() => {
            navigate("/lineup");
          }}
        />
        <TopBarButton
          title="Statistics"
          onClick={() => {
            navigate("/statistics");
          }}
        />
        <TopBarButton
          title="Profile"
          onClick={() => {
            navigate("/profile");
          }}
        />
      </div>
    </div>
  );
};

export default TopBar;
